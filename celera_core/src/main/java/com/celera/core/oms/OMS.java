package com.celera.core.oms;

import java.util.List;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.json.JsonObject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.ESessionState;
import com.celera.core.dm.ESide;
import com.celera.core.dm.ETradeReportType;
import com.celera.core.dm.IBlockTradeReport;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;
import com.celera.core.dm.TradeReport;
import com.celera.core.mds.IMarketDataService;
import com.celera.core.mds.MarketDataService;
import com.celera.core.service.staticdata.IStaticDataListener;
import com.celera.core.service.staticdata.IStaticDataService;
import com.celera.core.service.staticdata.StaticDataService;
import com.celera.gateway.IOrderGatewayListener;
import com.celera.gateway.OrderGatewayManager;
import com.celera.ipc.ILifeCycle;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.gateway.IOrderGateway;

public class OMS implements IOMS, IOrderGatewayListener, ILifeCycle
{
	final static Logger logger = LoggerFactory.getLogger(OMS.class);

	private Map<Long, IOrder> orders = new ConcurrentHashMap<Long, IOrder>();
	private Map<Long, ITrade> trades = new ConcurrentHashMap<Long, ITrade>();
	private Map<Long, ITradeReport> m_tradeReports = new ConcurrentHashMap<Long, ITradeReport>();

	private Map<Long, Long> m_leg2Block = new ConcurrentHashMap<Long, Long>();

	private List<IOMSListener> listeners = new ArrayList<IOMSListener>();
	
	static private OMS INSTANCE = null;

	private OrderLoggerServer ols = null;
	
	private AtomicLong id = null;

	private static final Date c_start;
	private static final Date c_end;
	
	private static final Double m_priceLimitAbs;
	private static final Integer m_qtyLimit;

	private final IMarketDataService m_mds = MarketDataService.instance();
	
	static {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		c_start = cal.getTime();
		cal.add(Calendar.DATE, 1);
		c_end = cal.getTime();
		
		Double priceLimitPercent = Double.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_COMMON_LIMIT_PRICE_PERCENT));
		m_priceLimitAbs = priceLimitPercent * 0.01;
		m_qtyLimit = Integer.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_COMMON_LIMIT_QTY));
		logger.info("start[{}] end[{}] price limit[{}%] qty limit[{}]", c_start, c_end, priceLimitPercent, m_qtyLimit);
	}
	
	public OMS() {
		ols = new OrderLoggerServer(c_start, c_end);
	}
	
	static synchronized public OMS instance()
	{
		if (INSTANCE == null)
			INSTANCE = new OMS();
		return INSTANCE;
	}

	public void addListener(IOMSListener l) {
		listeners.add(l);
	}
	
	public IOrder get(Long id)
	{
		return orders.get(id);
	}

	@Override
	public void init()
	{
		ols.init();
		long maxId = 0l;
		List<ITradeReport> l = ols.getAllTradeReports();
		for (ITradeReport tr : l)
		{
			Long id = tr.getOrderId();
			if (id > maxId) {
				maxId = id;
			}
			m_tradeReports.put(id, tr);
			if (tr instanceof IBlockTradeReport)
			{
				List<ITradeReport> splits = ((IBlockTradeReport) tr).split2SingleBlock();
				for (ITradeReport o : splits)
				{
					long legId = o.getOrderId();
					m_leg2Block.put(legId, id);
					m_tradeReports.put(legId, o);
				}
			}
		}
		maxId++;
		this.id = new AtomicLong(maxId);
		
		logger.debug("set next order id {}", this.id);
	}

	@Override
	public void start()
	{
	}

	@Override
	public void stop()
	{
		ols.stop();
	}
	
	public void onCoreOrder(IOrder o)
	{
		Long id = o.getOrderId();
		
		IOrder old = orders.get(id);
		if (old != null) {
			EOrderStatus status = o.getStatus();
			if (status == EOrderStatus.AMEND) {
				old.update(o.getPrice(), o.getQty(), null, null);
			}
			old.setStatus(o.getStatus());
			old.setRemark(o.getRemark());
		}
		else {
			orders.put(id,  o);
			old = o;
		}
		logger.info("{}", old.toString());
		
		for (IOMSListener l : listeners) {
			l.onOrder(old);
		}
	}

	public void onQuote(IOrder o)
	{
		Long id = o.getOrderId();
		if (!orders.containsKey(id))
		{
			orders.put(id, o);
		}
		logger.info("onQuote: " + o.toString());
	}

	public void onCoreTrade(ITrade t)
	{
		Long id = t.getTradeId();
		ITrade old = trades.put(id, t);
		if (old != null)
		{
			logger.info("onTrade update: " + t.toString());
		}
		else
		{
			logger.info("onTrade new: " + t.toString());
		}

		Long ordId = t.getOrderId();
		IOrder order = orders.get(ordId);
		
		// check if trade report
		ITradeReport tr = m_tradeReports.get(id);
		if (tr == null) {
			return;
		}
		
		onCoreTradeReport(id, t.getStatus(), "", t.getGiveupId());
	}
	
	public void sendOrder(IOrder order){
		boolean isSucc = true;
		String rej = null;
		
		// must filled order ID
		long id = this.id.getAndIncrement();
		order.setOrderId(id);
		orders.put(id, order);
		
		Integer qty = order.getQty();
		Double price = order.getPrice();
		
		String symbol = order.getInstr().getSymbol();
		
		rej = validate(symbol, price, qty);
		if ( rej == null) {
			IOrderGateway gw = OrderGatewayManager.instance().getOrderGateway(symbol);
	
			if (gw == null) {
				rej = "no gateway can trade instrument[" + symbol + "]";
				isSucc = false;
			}
			else if (!gw.isReady()) {
				rej = "order gateway[" + gw.getClass().getName() + "] not ready";
				isSucc = false;
			}
			else {
				order.setStatus(EOrderStatus.SENT);
				gw.createOrder(order);
			}
		}
		else {
			isSucc = false;
		}
		
		if (!isSucc) 
		{
			order.setStatus(EOrderStatus.REJECTED);
			order.setRemark(rej);
			this.onCoreOrder(order);
			logger.error("{}", rej);
		}
	}
	
	public boolean amendOrder(Long id, Double price, Integer qty, String giveup, ESessionState state)
	{
		boolean isSucc = true;
		String rej = null;
		IOrder myOrder = orders.get(id);

		if (myOrder == null) {
			rej = "order[" + id + "] not found ";
			myOrder = m_tradeReports.get(id);
			if (myOrder != null) {
				rej = "cannot amend trade report[" + id + "]. please cancel and add new";
				this.onCoreTradeReport(myOrder.getOrderId(), EOrderStatus.AMEND_REJECTED, rej, null);
				return false;
			}
			isSucc = false;
		} 
		else {
			String symbol = myOrder.getInstr().getSymbol();
			rej = validate(symbol, price, qty);
			if ( rej == null) {
				IOrderGateway gw = OrderGatewayManager.instance().getOrderGateway(symbol);
				if (gw == null) {
					rej = "not gateway can trade instrument[" + symbol + "]";
					isSucc = false;
				}
				else if (!gw.isReady()) {
					rej = "order gateway[" + gw.getClass().getName() + "] not ready";
					isSucc = false;
				}
				else {
					myOrder.setStatus(EOrderStatus.PENDING_AMEND);
					IOrder clone = myOrder.clone();
					clone.update(price, qty, giveup, state);
					gw.modifyOrder(clone);
				}
			}
			else {
				isSucc = false;
			}
		}
		
		if (!isSucc) {
			myOrder.setStatus(EOrderStatus.AMEND_REJECTED);
			myOrder.setRemark(rej);
			this.onCoreOrder(myOrder);
			logger.error("{}", rej);
		}
		return isSucc;
	}

	public boolean cancelOrder(Long id)
	{
		boolean isSucc = true;
		String rej = null;
		IOrder myOrder = orders.get(id);
		if (myOrder == null) {
			rej = "order[" + id + "] not found ";
			myOrder = m_tradeReports.get(id);
			if (myOrder != null) {
				return cancelTradeReport(id);
			}
			isSucc = false;
		} 
		else {
			String symbol = myOrder.getInstr().getSymbol();
			IOrderGateway gw = OrderGatewayManager.instance().getOrderGateway(symbol);
			
			if (gw == null) {
				rej = "instrument[" + symbol + "] not tradable";
				isSucc = false;
				logger.error("Cannot find gateway to trade [{}]", symbol);
			}
			else if (!gw.isReady()) {
				rej = "order gateway not ready";
				isSucc = false;
				logger.error("Order gateway not ready [{}]", gw.getClass().getName());
			}
			else {
				myOrder.setStatus(EOrderStatus.PENDING_CANCEL);
				gw.cancelOrder(myOrder);
			}
		}
		
		if (!isSucc) {
			myOrder.setStatus(EOrderStatus.CANCEL_REJECTED);
			myOrder.setRemark(rej);
			this.onCoreOrder(myOrder);
		}
		return isSucc;
	}
	
	public boolean cancelTradeReport(Long id)
	{
		boolean isSucc = true;
		String rej = null;
		ITradeReport myOrder = m_tradeReports.get(id);
		if (myOrder == null) {
			rej = "cannot find trade report[" + id + "]";
			isSucc = false;
		} 
		else if (myOrder.getTradeReportType() != ETradeReportType.T4_INTERBANK_CROSS) {
			rej = "only allow cancel T4";
			isSucc = false;
		}
		else {
			String symbol;
			if (myOrder instanceof IBlockTradeReport) {
				symbol = ((BlockTradeReport) myOrder).getFirstLegSymbol();
			}
			else {
				symbol = myOrder.getInstr().getSymbol();
			}
			IOrderGateway gw = OrderGatewayManager.instance().getOrderGateway(symbol);
			
			if (gw == null) {
				rej = "instrument[" + symbol + "] not tradable";
				isSucc = false;
				logger.error("Cannot find gateway to trade [{}]", symbol);
			}
			else if (!gw.isReady()) {
				rej = "order gateway not ready";
				isSucc = false;
				logger.error("Order gateway not ready [{}]", gw.getClass().getName());
			}
			else {
				if (myOrder instanceof IBlockTradeReport) {
					((IBlockTradeReport)myOrder).setBlockStatus(EOrderStatus.PENDING_CANCEL, "", myOrder.getOrderId());
				}
				else {
					myOrder.setStatus(EOrderStatus.PENDING_CANCEL);
				}
				gw.cancelTradeReport(myOrder);
			}
		}
		
		if (!isSucc) {
			this.onCoreTradeReport(myOrder.getOrderId(), EOrderStatus.CANCEL_REJECTED, rej, null);
		}
		return isSucc;
	}
	
	public boolean sendTradeReport(ITradeReport order)
	{
		boolean isSucc = true;
		String rej = null;
		long id = this.id.getAndIncrement();
		m_tradeReports.put(id, order);
		order.setOrderId(id);

		Integer qty = order.getQty();
		Double price = order.getPrice();
		String symbol = order.getInstr().getSymbol();
		
		rej = validate(symbol, price, qty);
		if ( rej == null) {
			IOrderGateway gw = OrderGatewayManager.instance().getOrderGateway(symbol);
	
			if (gw == null) {
				rej = "no gateway can trade instrument[" + symbol + "]";
				isSucc = false;
			}
			else if (!gw.isReady()) {
				rej = "order gateway[" + gw.getClass().getName() + "] not ready";
				isSucc = false;
			}
			else { 
				order.setStatus(EOrderStatus.SENT);
				gw.createTradeReport(order);
			}
		}
		else {
			isSucc = false;
		}
		
		if (!isSucc) {
			order.setStatus(EOrderStatus.REJECTED);
			order.setRemark(rej);
			this.onCoreTradeReport(order.getOrderId(), EOrderStatus.REJECTED, rej, null);
			logger.error("{}", rej);
		}
		
		// TODO: test remove
//		IMongoDocument doc = order.toEntityObject();
//		ols.create(doc);
		
		return isSucc;
	}
	
//	public void sendBlockTradeReport(IBlockTradeReport temp)
//	{
//		String symbol = null;
//		long id = this.id++;
//		temp.setId(id);
//		m_tradeReports.put(id, temp);
//		IOrderGateway gw = null;
//		try
//		{
//			List<ITradeReport> splits = this.split2SingleBlock(temp);
//			for (ITradeReport o : splits) {
//				// first leg to determine which ordergateway to send
//				symbol = o.getInstr().getSymbol();
//				gw = OrderGatewayManager.instance().getOrderGateway(symbol);
//				if (gw == null) {
//					o.setStatus(EOrderStatus.REJECTED);
//					o.setRemark("instrument[" + symbol + "] not tradable");
//				}
//				else {
//					if (o instanceof IBlockTradeReport) {
//						gw.createBlockTradeReport((IBlockTradeReport)o);
//					}
//					else { 
//						gw.createTradeReport(o);
//					}
//				}
//			}
//			if (gw == null) {
//				temp.setStatus(EOrderStatus.REJECTED);
//			}
//		}
//		catch (Exception e)
//		{
//			logger.error("Symbol[{}]  Block[{}] ", temp, symbol, e);
//		}
//	}
	
	public List<ITradeReport> getAllTradeReport() {
		List<ITradeReport> l = new ArrayList<ITradeReport>();
		for (ITradeReport order : this.m_tradeReports.values()) {
			if (order instanceof ITradeReport) {
				long legId = order.getOrderId();
				if (!m_leg2Block.containsKey(legId))	// skip leg as reported as Block
					l.add(order);
			}
		}
		Collections.sort(l, new Comparator<ITradeReport>() {
			public int compare(ITradeReport o1, ITradeReport o2) {
				return o1.getLastUpdateTime().compareTo(o2.getLastUpdateTime());
			}
		});
		return l;
	}

	public List<IInstrument> getAllInstrument() {
		IStaticDataService sds = StaticDataService.instance();
		List<IInstrument> l = sds.getAllInstruments();
		return l;
	}

	@Override
//	public void onTradeReport(ITradeReport t)
	public void onCoreTradeReport(Long _id, EOrderStatus status, String remark, Integer giveupNum)
	{
		ITradeReport tr = m_tradeReports.get(_id);
		if (tr == null) {
			logger.error("trade report [{}] not found" , _id);
			return;
		}
		
		Long id = tr.getOrderId();
		Long groupId = tr.getGroupId();
//		EOrderStatus status = status;
//		String remark = t.getRemark();
		ITradeReport block = null;
		
//		if (tr instanceof IBlockTradeReport) {
			try {
				// update block status
				Long blockId = m_leg2Block.get(id);		// either leg is block or single
				if (blockId != null) {	// split block
					block = m_tradeReports.get(blockId);
					if (block instanceof IBlockTradeReport) {
						((IBlockTradeReport)block).setBlockStatus(status, remark, groupId);
					}
					else {
						block.setStatus(status);
					}
					block.setRemark(remark);
				}
				else {
					block = m_tradeReports.get(id);
					if (block instanceof IBlockTradeReport) {
						((IBlockTradeReport)block).setBlockStatus(status, remark, groupId);
					}
					else {
						block.setStatus(status);
					}
					block.setRemark(remark);
				}
				if (giveupNum != null && giveupNum > 0)
					block.setGiveupNumber(giveupNum);
				
				for (IOMSListener l : listeners) {
					l.onTradeReport(block);
				}
			} catch (Exception e) {
				logger.error("{}", e);
			}
//		}
//		else {
//			ITradeReport single = m_tradeReports.get(id);
//			single.setStatus(status);
//			single.setRemark(remark);
//			for (IOMSListener l : listeners) {
//				l.onTradeReport(single);
//			}
//		}
			
		logger.debug("{}", tr);
	}
	
//	public List<ITradeReport> split2SingleBlock(IBlockTradeReport block) {
//		List<ITradeReport> splitsBlocks = new ArrayList<ITradeReport>();
//		if (!block.hasSplit())
//		{
//			long id = block.getId();
//			m_tradeReports.put(id, block);
//			splitsBlocks.add(block);
//			return splitsBlocks;
//		}
//		
//		Long blockId = block.getId();
//		try
//		{
//			long id = 0;
//			Map<Long, List<ITradeReport>> map = new HashMap<Long, List<ITradeReport>>();
//			List<ITradeReport> split = block.getList();
//			for (ITradeReport o : split) {
//				Long groupId = o.getId();
//				List l = map.get(groupId);		// group id of legs
//				if (l == null) {
//					l = new ArrayList<ITradeReport>();
//					map.put(groupId, l);
//				}
//				l.add(o);
//			}
//			
//			// build new BlockTradeReport by group of legs
//			for (List<ITradeReport> legsSplit : map.values()) {
//				// build a new block trade
//				if (legsSplit.size() > 1) {
//					IInstrument instr = block.getInstr();
//					BlockTradeReport newBlock = new BlockTradeReport(instr, EOrderStatus.SENT, block.getTradeReportType(), block.getQty(), null,
//							null, block.getRefId(), block.getBuyer(), block.getSeller());
//					id = this.id++;
//					newBlock.setId(id);
//					m_tradeReports.put(id, newBlock);
//					// put the leg -> update also block status
//					m_leg2Block.put(id, blockId);
//					
//					for (int i =0; i<legsSplit.size(); i++) {
//						ITradeReport o = legsSplit.get(i);
//						if (i ==0) {	// use first leg for block instr
//							instr.setSymbol(o.getInstr().getSymbol());
//						}
//						newBlock.add(o);
//					}
//					splitsBlocks.add(newBlock);
//				}
//				// single trade report
//				else {
//					for (ITradeReport o : legsSplit) {
//						id = this.id++;
//						o.setId(id);
//						o.setTradeReportType(ETradeReportType.T1_SELF_CROSS);	// block trade with only one leg -> cross as T1
//						m_tradeReports.put(id, o);
//						
//						splitsBlocks.add(o);
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			logger.error("{}", e);
//		}
//		return splitsBlocks;
//	}

	// TODO unit test
	@Override
	public boolean sendBlockTradeReport(BlockTradeReport block ,Map<Long, List<ITradeReport>> map)
	{
		long id = this.id.getAndIncrement();
		block.setOrderId(id);
		
		if (map.keySet().size() > 1) {
			for (Map.Entry<Long, List<ITradeReport>> e : map.entrySet()) {
				List<ITradeReport> l = e.getValue();
				Long groupId = e.getKey();
				id = this.id.getAndIncrement();
				if (l.size() > 1) {
					// clone block trade report info
					BlockTradeReport subBlock = new BlockTradeReport(block);
//					id = this.id++;

					subBlock.setOrderId(id);
					subBlock.setGroupId(groupId);
					
					for (ITradeReport tr : l) {
						tr.setOrderId(id);
						tr.setGroupId(groupId);
						subBlock.add(tr);
					}
					block.add(subBlock);
				}
				else {	// cross as single leg
					ITradeReport tr = l.get(0);
					tr.setOrderId(id);
					tr.setGroupId(groupId);
					tr.setTradeReportType(ETradeReportType.T1_SELF_CROSS);
					block.add(tr);	// single leg trade report
				}
			}
		}
		else {
			for (Map.Entry<Long, List<ITradeReport>> e : map.entrySet()) {
				for (ITradeReport tr : e.getValue()) {
					Long ordId = block.getOrderId();
					tr.setOrderId(ordId);
					block.add(tr);
				}
			}
		}
		boolean res = sendBlockTradeReport(block);
		IMongoDocument doc = block.toEntityObject();
		ols.create(doc);
		
		return res;
		
	}
	
	private boolean sendBlockTradeReport(IBlockTradeReport block) {
		
		logger.debug("send block {}", block);
		
		IOrderGateway gw = null;
		String symbol = null;
		boolean isSucc = true;
		String remark = "";
		
		long blockId = block.getOrderId();
		m_tradeReports.put(blockId, block);
		
		try
		{
//			if (block.hasSplit()) {
//				List<ITradeReport> splits = this.split2SingleBlock(block);
				List<ITradeReport> splits = block.split2SingleBlock();
				for (ITradeReport o : splits) {
					long legId = o.getOrderId();
					m_leg2Block.put(legId, blockId);
					m_tradeReports.put(legId, o);
					
					long groupId = o.getGroupId();
					
					// first leg to determine which ordergateway to send
					if (o instanceof IBlockTradeReport) {
						symbol = ((IBlockTradeReport) o).getList().get(0).getInstr().getSymbol();
					}
					else {
						symbol = o.getInstr().getSymbol();
					}
					gw = OrderGatewayManager.instance().getOrderGateway(symbol);
					if (gw == null || !gw.isReady()) {
						if (gw == null) { 
							remark = "no gateway can trade instrument[" + symbol + "]";
//							o.setRemark("instrument[" + symbol + "] not tradable");
						}
						else {
							remark = "order gateway[" + gw.getClass().getName() + "] not ready";
//							o.setRemark("order gateway not open");
						}
						if (o instanceof IBlockTradeReport) {
							((IBlockTradeReport)o).setBlockStatus(EOrderStatus.REJECTED, remark, groupId);	
						}
						else {
							o.setStatus(EOrderStatus.REJECTED);
							o.setRemark(remark);
						}
						isSucc = false;
						this.onCoreTradeReport(legId, EOrderStatus.REJECTED, remark, null);
						logger.error("{}", remark);
					}
					else {
						if (o instanceof IBlockTradeReport) {
							gw.createBlockTradeReport((IBlockTradeReport)o);
						}
						else { 
							gw.createTradeReport(o);
						}
					}
				}
//			}
//			else {
//				// first leg to determine which ordergateway to send
//				symbol = block.getInstr().getSymbol();
//				gw = OrderGatewayManager.instance().getOrderGateway(symbol);
//				if (gw == null) {
//					block.setBlockStatus(EOrderStatus.REJECTED, 1l);	
//					block.setRemark("instrument[" + symbol + "] not tradable");
//				}
//				else {
//					gw.createBlockTradeReport(block);
//				}
//			}
		}
		catch (Exception e)
		{
			logger.error("Symbol[{}]  Block[{}] ", symbol, block, e);
			isSucc = false;
		}
		if (!isSucc) {
			block.setBlockStatus(EOrderStatus.REJECTED, remark, 1l);
		}
		return isSucc;
		
	}
	
	@Override
	public void onInstrumentUpdate(IInstrument instr)
	{
		for (IOMSListener l : listeners) {
			l.onInstrumentUpdate(instr);
		}
	}
	
	private String checkPrice(String symbol, Double price) {
		Double last = m_mds.getLastPrice(symbol);
		if (last != null) {
			Double low = last * (1 - m_priceLimitAbs); 
			Double high = last * (1 + m_priceLimitAbs);
			if (price < low || price > high)
				return "price out of range [" + low + "," + high + "]";
		}
		return null;
	}
	
	private String checkQty(Integer qty) {
		if (m_qtyLimit < qty)
			return "breach quantity threshold " + m_qtyLimit;
		return null;
	}
	
	public String validate(String symbol, Double price, Integer qty) {
		String rej = checkQty(qty);
		if ( rej != null ) {
			return rej;
		}
		
		rej = checkPrice(symbol, price);
		return rej;
	}
	
	public static void main(String[] args) {
//		String temp = "BlockTradeReport [instr=Instrument [market=HK, symbol=HSI22000L7, type=null, name=null, ISIN=null, BLOOMBERG_CODE=null, RIC=null, status=CLOSE, lastUpdate=2017-01-23]Derivative [strike=null, expiry=MAR17, price=null, isPriceInPercent=false, Legs[]] , status=UNSENT, tradeReportType=T2_COMBO_CROSS, qty=100, price=null, id=2, refId=1485142437272, buyer=HKCEL, seller=HKTOM, remark=OG not ready                                      , lastUpdateTime=1485142466385, map={1=[TradeReport [instr=Instrument [market=HK, symbol=HSI22000L7, type=CALL, name=HSI Call, ISIN=, BLOOMBERG_CODE=, RIC=, status=CLOSE, lastUpdate=2017-01-23]Derivative [strike=22000.0, expiry=DEC17, price=20.0, isPriceInPercent=false, Legs[]] , status=REJECTED, tradeReportType=T2_COMBO_CROSS, side=CROSS, price=20.0, qty=100, id=1, refId=1485142437272, buyer=HKCEL, seller=HKTOM, remark=null, lastUpdateTime=1485142437287], TradeReport [instr=Instrument [market=HK, symbol=HSI24000L7, type=CALL, name=HSI Call, ISIN=, BLOOMBERG_CODE=, RIC=, status=CLOSE, lastUpdate=2017-01-23]Derivative [strike=24000.0, expiry=DEC17, price=8.0, isPriceInPercent=false, Legs[]] , status=REJECTED, tradeReportType=T2_COMBO_CROSS, side=CROSS, price=8.0, qty=125, id=1, refId=1485142437272, buyer=HKTOM, seller=HKCEL, remark=null, lastUpdateTime=1485142437287], TradeReport [instr=Instrument [market=HK, symbol=HSIH7, type=FUTURE, name=HSI Future, ISIN=, BLOOMBERG_CODE=, RIC=, status=CLOSE, lastUpdate=2017-01-23]Derivative [strike=null, expiry=MAR17, price=22825.0, isPriceInPercent=false, Legs[]] , status=REJECTED, tradeReportType=T2_COMBO_CROSS, side=CROSS, price=22825.0, qty=10, id=1, refId=1485142437272, buyer=HKTOM, seller=HKCEL, remark=null, lastUpdateTime=1485142439801]]}]";
//		JSONObject jsonbject = new JSONObject(temp);
//		JSONObject.getNames("Instrument");
		
		Long ordId = 1l;
		Long groupId = 1l;
		String buyer = "HKCEL", seller = "HKTOM";
		Long refId = new Date().getTime();
		IInstrument deriv = new Derivative("HK", "HSI22000L7", null, null, null, null, null, null, "MAR17", null, false, 10d);
		BlockTradeReport block = new BlockTradeReport(deriv, EOrderStatus.UNSENT, ETradeReportType.T2_COMBO_CROSS, 100, null,
				ordId, refId, buyer, seller);
		block.setGroupId(1l);
		
		IInstrument instr1 = new Derivative("HK", "HSI22000L7", EInstrumentType.CALL, "HSI Call", "", "",
				"", 22000.0, "DEC17", 20.0, false, null);
		TradeReport tr1 = new TradeReport(instr1, EOrderStatus.SENT, ETradeReportType.T2_COMBO_CROSS, ESide.CROSS, 100, 20d,
				ordId, refId, buyer, seller);
		tr1.setGroupId(groupId);
		
		IInstrument instr2 = new Derivative("HK", "HSI24000L7", EInstrumentType.CALL, "HSI Call", "", "",
				"", 22000.0, "DEC17", 20.0, false, null);
		TradeReport tr2 = new TradeReport(instr2, EOrderStatus.SENT, ETradeReportType.T2_COMBO_CROSS, ESide.CROSS, 100, 20d,
				ordId, refId, seller, buyer);
		tr2.setGroupId(groupId);

		IInstrument instr3 = new Derivative("HK", "HSIH7", EInstrumentType.FUTURE, "HSI Future", "", "",
				"", 22000.0, "DEC17", 22825.0, false, null);
		TradeReport tr3 = new TradeReport(instr3, EOrderStatus.SENT, ETradeReportType.T2_COMBO_CROSS, ESide.CROSS, 10, 22825d,
				ordId, refId, seller, buyer);
		tr2.setGroupId(groupId);
		
		Map<Long, List<ITradeReport>> split = new HashMap<Long, List<ITradeReport>>();
		ArrayList<ITradeReport> l = new ArrayList<ITradeReport>();
		l.add(tr1);
		l.add(tr2);
		l.add(tr3);
		split.put(groupId, l);
		
		OMS oms = new OMS();
		oms.sendBlockTradeReport(block, split);
		
		oms.onCoreTradeReport(ordId, EOrderStatus.FILLED, "", 0);
	}

}
