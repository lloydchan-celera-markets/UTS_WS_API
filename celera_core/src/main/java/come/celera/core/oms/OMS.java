package come.celera.core.oms;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.JsonObject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.ESide;
import com.celera.core.dm.ETradeReportType;
import com.celera.core.dm.IBlockTradeReport;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;
import com.celera.core.dm.TradeReport;
import com.celera.core.service.staticdata.IStaticDataListener;
import com.celera.core.service.staticdata.IStaticDataService;
import com.celera.core.service.staticdata.StaticDataService;
import com.celera.gateway.IOrderGatewayListener;
import com.celera.gateway.OrderGatewayManager;
import com.celera.gateway.IOrderGateway;

public class OMS implements IOMS, IOrderGatewayListener
{
	final static Logger logger = LoggerFactory.getLogger(OMS.class);

	private Map<Long, IOrder> orders = new ConcurrentHashMap<Long, IOrder>();
	private Map<Long, ITrade> trades = new ConcurrentHashMap<Long, ITrade>();
	private Map<Long, ITradeReport> m_tradeReports = new ConcurrentHashMap<Long, ITradeReport>();

	private Map<Long, Long> m_leg2Block = new ConcurrentHashMap<Long, Long>();

	private List<IOMSListener> listeners = new ArrayList<IOMSListener>();
	
	static private OMS INSTANCE = null;

	private Long id = 1l;

	public OMS() {
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

	public void onOrder(IOrder o)
	{
		Long id = o.getId();
		orders.put(id, o);
		logger.info("onOrder: " + o.toString());
	}

	public void onQuote(IOrder o)
	{
		Long id = o.getId();
		if (!orders.containsKey(id))
		{
			orders.put(id, o);
		}
		logger.info("onQuote: " + o.toString());
	}

	public void onTrade(ITrade o)
	{
		Long id = o.getId();
		ITrade t = trades.put(id, o);
		if (t != null)
		{
			logger.info("onTrade update: " + o.toString());
		}
		else
		{
			logger.info("onTrade new: " + o.toString());
		}
	}
	
	public void sendOrder(IOrder order){}
	public void updateOrder(IOrder order){}

	public boolean sendTradeReport(ITradeReport order)
	{
		boolean isSucc = true;
		String remark = "";
		long id = this.id++;
		// TODO: simulate result for testing
		
//			Thread.sleep(1000);
		order.setId(id);
		order.setStatus(EOrderStatus.SENT);

		String symbol = order.getInstr().getSymbol();
		IOrderGateway gw = OrderGatewayManager.instance().getOrderGateway(symbol);

		if (gw == null) {
			remark = "instrument[" + symbol + "] not tradable";
			isSucc = false;
		}
		else if (!gw.isReady()) {
			remark = "order gateway not ready";
			isSucc = false;
		}
		else {
			gw.createTradeReport(order);
		}
		
		m_tradeReports.put(id, order);
		
		if (!isSucc)
		{
			order.setStatus(EOrderStatus.REJECTED);
			order.setRemark(remark);
		}
		
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
				long legId = order.getId();
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
	public void onTradeReport(Long _id, EOrderStatus status, String remark)
	{
		ITradeReport tr = m_tradeReports.get(_id);
		if (tr == null) {
			logger.error("trade report [{}] not found" , _id);
			return;
		}
		
		Long id = tr.getId();
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
	
	public List<ITradeReport> split2SingleBlock(IBlockTradeReport block) {
		List<ITradeReport> splitsBlocks = new ArrayList<ITradeReport>();
		if (!block.hasSplit())
		{
			long id = block.getId();
			m_tradeReports.put(id, block);
			splitsBlocks.add(block);
			return splitsBlocks;
		}
		
		Long blockId = block.getId();
		try
		{
			long id = 0;
			Map<Long, List<ITradeReport>> map = new HashMap<Long, List<ITradeReport>>();
			List<ITradeReport> split = block.getList();
			for (ITradeReport o : split) {
				Long groupId = o.getId();
				List l = map.get(groupId);		// group id of legs
				if (l == null) {
					l = new ArrayList<ITradeReport>();
					map.put(groupId, l);
				}
				l.add(o);
			}
			
			// build new BlockTradeReport by group of legs
			for (List<ITradeReport> legsSplit : map.values()) {
				// build a new block trade
				if (legsSplit.size() > 1) {
					IInstrument instr = block.getInstr();
					BlockTradeReport newBlock = new BlockTradeReport(instr, EOrderStatus.SENT, block.getTradeReportType(), block.getQty(), null,
							null, block.getRefId(), block.getBuyer(), block.getSeller());
					id = this.id++;
					newBlock.setId(id);
					m_tradeReports.put(id, newBlock);
					// put the leg -> update also block status
					m_leg2Block.put(id, blockId);
					
					for (int i =0; i<legsSplit.size(); i++) {
						ITradeReport o = legsSplit.get(i);
						if (i ==0) {	// use first leg for block instr
							instr.setSymbol(o.getInstr().getSymbol());
						}
						newBlock.add(o);
					}
					splitsBlocks.add(newBlock);
				}
				// single trade report
				else {
					for (ITradeReport o : legsSplit) {
						id = this.id++;
						o.setId(id);
						o.setTradeReportType(ETradeReportType.T1_SELF_CROSS);	// block trade with only one leg -> cross as T1
						m_tradeReports.put(id, o);
						
						splitsBlocks.add(o);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("{}", e);
		}
		return splitsBlocks;
	}

	// TODO unit test
	@Override
	public boolean sendBlockTradeReport(BlockTradeReport block ,Map<Long, List<ITradeReport>> map)
	{
		long id = this.id++;
		logger.debug("add order id {}", id);
		
		block.setId(id);
		
		if (map.keySet().size() > 1) {
			for (Map.Entry<Long, List<ITradeReport>> e : map.entrySet()) {
				List<ITradeReport> l = e.getValue();
				Long groupId = e.getKey();
				if (l.size() > 1) {
					// clone block trade report info
					BlockTradeReport subBlock = new BlockTradeReport(block);
					id = this.id++;
					subBlock.setId(id);
					subBlock.setGroupId(groupId);
					
					for (ITradeReport tr : l) {
						tr.setId(id);
						subBlock.add(tr);
					}
					block.add(subBlock);
				}
				else {	// cross as single leg
					ITradeReport tr = l.get(0);
					tr.setId(this.id++);
					tr.setGroupId(groupId);
					tr.setTradeReportType(ETradeReportType.T1_SELF_CROSS);
					block.add(tr);	// single leg trade report
				}
			}
		}
		else {
			for (Map.Entry<Long, List<ITradeReport>> e : map.entrySet()) {
				for (ITradeReport tr : e.getValue()) {
					Long ordId = block.getId();
					tr.setId(ordId);
					block.add(tr);
				}
			}
		}
		return sendBlockTradeReport(block);
	}
	
	public boolean sendBlockTradeReport(IBlockTradeReport block) {
		IOrderGateway gw = null;
		String symbol = null;
		boolean isSucc = true;
		String remark = "";
		
		try
		{
//			if (block.hasSplit()) {
				List<ITradeReport> splits = this.split2SingleBlock(block);
				for (ITradeReport o : splits) {
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
							remark = "instrument[" + symbol + "] not tradable";
//							o.setRemark("instrument[" + symbol + "] not tradable");
						}
						else {
							remark = "order gateway not open";
//							o.setRemark("order gateway not open");
						}
						if (o instanceof IBlockTradeReport) {
							((IBlockTradeReport)o).setBlockStatus(EOrderStatus.REJECTED, remark, groupId);	
						}
						else {
							o.setStatus(EOrderStatus.REJECTED);
						}
						isSucc = false;
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
		
		oms.onTradeReport(ordId, EOrderStatus.FILLED, "");
	}
}
