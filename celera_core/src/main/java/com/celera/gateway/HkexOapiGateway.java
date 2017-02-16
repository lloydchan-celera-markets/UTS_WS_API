package com.celera.gateway;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOGAdmin;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EOrderType;
import com.celera.core.dm.ESide;
import com.celera.core.dm.EStatus;
import com.celera.core.dm.IBlockTradeReport;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;
import com.celera.core.dm.Instrument;
import com.celera.core.dm.Order;
import com.celera.core.dm.Trade;
import com.celera.core.mds.IMarketDataService;
import com.celera.core.mds.MarketDataService;
import com.celera.core.oms.OMS;
import com.celera.core.service.staticdata.IStaticDataService;
import com.celera.core.service.staticdata.StaticDataService;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineServer;
import com.celera.ipc.PipelineSinkCollector;
import com.celera.ipc.URL;
import com.celera.message.cmmf.CmmfBuilder;
import com.celera.message.cmmf.CmmfParser;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.EFoCommand;
import com.celera.message.cmmf.EMessageType;
import com.celera.message.cmmf.ICmmfListener;
import com.celera.message.cmmf.ICmmfProcessor;
//import com.itextpdf.text.pdf.StringUtils;

public class HkexOapiGateway implements ICmmfListener, ICmmfProcessor, IOrderGateway
{
	Logger logger = LoggerFactory.getLogger(HkexOapiGateway.class);

	private static final int CMMF_QUERY_ADMIN_SIZE = 5;
	private static final int CMMF_QUERY_INSTRUMENT_SIZE = 4;
	private static final int CMMF_LOGIN_ADMIN_SIZE = 68;
	private static final int CMMF_PASSWORD_SIZE = 32;
	
	public static final URL PUSH_URL;// = "tcp://10.100.1.119:20010";
	public static final URL SINK_URL;// = "tcp://10.100.1.119:20011";

	private final PipelineSinkCollector sink;
	private final PipelineServer server;
	
	private AtomicBoolean isWaitAdminResp = new AtomicBoolean(false);

	private Set<String> m_tradableInstr = new HashSet<String>();
	private Map<Long, ITradeReport> m_tradeReportMap = new ConcurrentHashMap<Long, ITradeReport>();
	private Map<Long, ITrade> m_tradeMap = new ConcurrentHashMap<Long, ITrade>();
	
	private IOrderGatewayListener m_oms = OMS.instance();
	
	private AtomicBoolean isReady = new AtomicBoolean(true);
	
	private Object sync = new Object();
	private final IMarketDataService m_mds = new MarketDataService();
	
	static
	{
		String protocol = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_PUSH_PROT);
		String ip = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_PUSH_IP);
		Integer port = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_PUSH_PORT));
		PUSH_URL = new URL(protocol, ip, port);

		protocol = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_SINK_PROT);
		ip = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_SINK_IP);
		port = Integer.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_SINK_PORT));
		SINK_URL = new URL(protocol, ip, port);
	}
	
	public HkexOapiGateway()
	{
		sink = new PipelineSinkCollector(SINK_URL.toString(), this);
		server = new PipelineServer(PUSH_URL.toString(), this);
		m_tradableInstr.add(HkexOapiGateway.class.getName());
	}
	
	@Override
	public byte[] onMessage(byte[] buf)
	{
		return null;
	}

	@Override
	public byte[] onQuery(byte[] data)
	{
		return null;
	}

	@Override
	public void onAdmin(byte[] data)
	{
	}

	@Override
	public void onResponse(byte[] data)
	{
		synchronized (sync) {
			EFoCommand cmd = EFoCommand.get((char)data[2]);
			switch (cmd) {
			case ORDER_REQUEST: 
			{
				CmmfParser.parseCmmfOrderResponse(data, this);
				break;
			}
			case TRADE_REPORT: 
			case BLOCK_TRADE_REPORT: 
			{
				CmmfParser.parseCmmfTradeReportResponse(data, this);
				break;
			}
			case TRADE: 
			{
				CmmfParser.parseCmmfTradeResponse(data, this);
				break;
			}
			case SOD: {
				logger.info("SOD");
				isReady.set(true);
				break;
			}
			case ADMIN_REQUEST: {
				boolean result = CmmfParser.parseCmmfOgAdminResponse(data);
				isWaitAdminResp.set(false);
				break;
			}
			case UPDATE_INSTRUMENT: {
//test_Print_Bytes(data);			
				CmmfParser.parseCmmfInstrumentUpdateResponse(data, this);
				break;
			}
			case LAST_PRICE: {
//test_Print_Bytes(data);			
				CmmfParser.parseCmmfLastPriceResponse(data, this);
				break;
			}
			default: {
				break;
			}
			}
		}
	}

	@Override
	public void onTask(byte[] data)
	{
		EFoCommand cmd = EFoCommand.get((char)data[2]);
		switch (cmd) {
		case ORDER_REQUEST: {
			onResponse(data);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onSink(byte[] data)
	{
		test_Print_Bytes(data);
//		logger.debug("{}", new String(data));
		
		EMessageType type = EMessageType.get((char)data[1]);
		switch (type) {
		case RESPONSE: {
			onResponse(data);
			break;
		}
//		case TASK: {
//			onResponse(data);
//			break;
//		}
		default:
			break;
		}
	}

	@Override
	public void init()
	{
		sink.init();
//		server.init();
		server.connect();
	}

	@Override
	public void stop()
	{
		server.stop();
		sink.stop();
	}
	
	public void start()
	{
		sink.start();
		server.start();

		sleep(3000);
		// TODO: testing temp remove
//		this.login("geniumtesting");
		this.getAllInstrument();
//		test();
	}

	
	
	@Override
	public void createOrder(IOrder o)
	{
		try
		{
			o.setStatus(EOrderStatus.PENDING_NEW);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, EFoCommand.ORDER_REQUEST, b);
//			JsonObject json = o.json();
//			byte b[] = json.toString().getBytes();
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void createTradeReport(IOrder o)
	{
		try
		{
			o.setStatus(EOrderStatus.PENDING_NEW);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, EFoCommand.TRADE_REPORT, b);
//			JsonObject json = o.json();
//			byte b[] = json.toString().getBytes();
//test_Print_Bytes(msg);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void createBlockTradeReport(IBlockTradeReport block)
	{
		m_tradeReportMap.put(block.getOrderId(), block);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try
		{
			block.setStatus(EOrderStatus.PENDING_NEW);
			
			byte b[] = block.toCmmf();
			buf.put(b);
			
			buf.flip();
			int size = buf.limit();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, EFoCommand.BLOCK_TRADE_REPORT, buf.array(), size);
				
//test_Print_Bytes(msg);
			server.send(msg);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void test_Print_Bytes(byte[] msg) {
		for (int i=0; i<msg.length; i++) {
			System.out.print((int)msg[i] + ",");
		}
		System.out.println("");
	}
	
	@Override
	public void cancelTradeReport(IOrder o)
	{
		cancel(o, EFoCommand.TRADE_REPORT);
	}
	
	@Override
	public void modifyOrder(IOrder o)
	{
		try
		{
			o.setStatus(EOrderStatus.PENDING_AMEND);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, EFoCommand.ORDER_REQUEST, b);
CmmfParser.print(msg);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void cancelOrder(IOrder o)
	{
		cancel(o, EFoCommand.ORDER_REQUEST);
	}

	public void cancel(IOrder o, EFoCommand cmd) {
		try
		{
			o.setStatus(EOrderStatus.PENDING_CANCEL);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, cmd, b);
CmmfParser.print(msg);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void createQuote(IQuote quote)
	{
	}

	@Override
	public void modifyQuote(IQuote q)
	{
	}

	@Override
	public void cancelQuote(IQuote q)
	{
	}
	
	
	public static void main(String[] args) 
	{
		HkexOapiGateway gw = new HkexOapiGateway();
		gw.init();
		gw.start();
		
//		EOrderStatus status, IInstrument instr, EOrderType type, Long id, String entity,
//		Double price, Integer qty
		String giveup = "";
		IInstrument instr = new Derivative("HK", "HHI9800O7", EInstrumentType.EP, "European Put", null, null, null,
				9800d, "O7", null, false, 0.5);
		IOrder order = new Order(EOrderStatus.SENT, instr, EOrderType.LIMIT, null, 1l, "", 439d, 100, ESide.BUY,
				giveup);
		
		while(true)
		{
			try
			{
				gw.createOrder(order);
				Thread.sleep(10000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void getAllInstrument()
	{
		try
		{
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.QUERY, EFoCommand.UPDATE_INSTRUMENT, null);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void changePassword(String password, String newPassword)
	{
		try
		{
			String padPwd = StringUtils.rightPad(password, CMMF_PASSWORD_SIZE);
			String padNewPwd = StringUtils.rightPad(newPassword, CMMF_PASSWORD_SIZE);
			
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.CHANGE_PASSWORD.getChar());
			buf.put(padPwd.getBytes());
			buf.put(padNewPwd.getBytes());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, EFoCommand.ADMIN_REQUEST, b);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void logout()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.LOGOUT.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, EFoCommand.ADMIN_REQUEST, b);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	public void sleep(long millisecond) {
		try	{
			Thread.sleep(millisecond);
			logger.info("waiting for response ...");
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}
	
	public void start(String password)
	{
		login(password);
		isWaitAdminResp.set(true);
		int count = 0;
		while (isWaitAdminResp.get()) {
			if (count > 5) {
//				login(password);
				count = 0;
			}
			sleep(1000);
			count++;
		};
		SOD();
//if (!ResourceManager.IS_TESTING) {
		isWaitAdminResp.set(true);
		count = 0;
		while (isWaitAdminResp.get()) {
//			if (count > 5) {
//				SOD();
//				count = 0;
//			}
			sleep(10000);
//			count++;
		};
		subscribeMarketData();
//}
	}
	
	@Override
	public void login(String password)
	{
		try
		{
			String padRightSpace = StringUtils.rightPad(password, CMMF_PASSWORD_SIZE);
			
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.LOGIN.getChar());
			
			buf.put(padRightSpace.getBytes());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, EFoCommand.ADMIN_REQUEST, b);
//			for (int i=0; i<msg.length; i++) {
//				System.out.print((int)msg[i] + ",");
//			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void SOD()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.SOD.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, EFoCommand.ADMIN_REQUEST, b);
//			for (int i=0; i<msg.length; i++) {
//				System.out.print((int)msg[i] + ",");
//			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	public void subscribeMarketData()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.SUBSCRIBE_MARKET_DATA.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, EFoCommand.ADMIN_REQUEST, b);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void unsubscribeMarketData()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.UNSUBSCRIBE_MARKET_DATA.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, EFoCommand.ADMIN_REQUEST, b);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void query(String command)
	{
		try
		{
			String padCmd = StringUtils.rightPad(command, CMMF_QUERY_ADMIN_SIZE);
			ByteBuffer buf = ByteBuffer.allocate(CMMF_QUERY_ADMIN_SIZE);
			buf.put(padCmd.getBytes());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.QUERY, EFoCommand.OG_QUERY, b);
//			for (int i=0; i<msg.length; i++) {
//				System.out.print((int)msg[i] + ",");
//			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	@Override
	public boolean isTradedSymbol(String symbol)
	{
		return m_tradableInstr.contains(symbol);
	}
	
	@Override
	public boolean isReady() {
		return isReady.get();
	}

//	@Override
//	public void onInstrumentUpdate(IInstrument i)
//	{
//		String symbol = i.getSymbol();
////		IInstrument old = m_tradeInstr.get(symbol);
////		if (old == null) {
////			old = i;
//		m_tradableInstr.put(symbol, i);
//		
//		m_oms.onInstrumentUpdate(i);
////		}
//	}

	public void addTradableInstrument(String symbol) {
		m_tradableInstr.add(symbol);
	}
	
	@Override
	public void onInstrumentUpdate(String _symbol, EStatus status)
	{
		String symbol = _symbol.trim();
//		logger.info("symbol[{}] status[{}]", symbol, status.name());

//if (!ResourceManager.IS_TESTING) {
		addTradableInstrument(symbol);
		EInstrumentType type = EInstrumentType.bySymbol(symbol);
		IInstrument i = new Instrument("HK", symbol, type, type.getName(), null, null, null);
		i.setStatus(status);
	
		IStaticDataService sds = StaticDataService.instance();
		sds.onInstrumentUpdate(i);
//}
	}

	@Override
	public void onTradeReport(Long id, EOrderStatus status, String reason, Integer giveupNum)
	{
//		ITradeReport tr = m_tradeReportMap.get(id);
//		if (tr != null) {
//			tr.setStatus(status);
//			tr.setRemark(reason);
//			m_oms.onTradeReport(tr);
//		}
//		else {
//			logger.error("trade report id[{}] not found", id);
//		}
		m_oms.onTradeReport(id, status, reason, giveupNum);
	}

	@Override
	public void onTrade(Long ordId, Long tradeId, double price, int qty, EOrderStatus status, Integer giveupNum)
	{
		ITrade t = new Trade(tradeId, ordId, price,  qty, status, "", giveupNum);
		m_oms.onTrade(t);
	}
	
	@Override
	public void startTestSOD(String password)
	{
		this.start(password);
	}
	
	public void test() {
		if (!ResourceManager.IS_TESTING) 
			return;
		
	String testSymbol = "HHI7700B7,HSI24400R7,HHI10000R7,HSI20000R7,HHI10400B7,HSI18000L7,HSI20200X7,HSI22600I7"
			+ ",HSI15800L7,HHI9600L7,HSI16400I7,HSI24000L8,HHI10800N7,HHI8200I7,HSI19200X7,HSI15400N7,HSI18200R7,"  
			+ "HSI24800B7,HSI24000F7,HHI7100A7,HSI20400B7,HSI26200M7,HSI19000R7,HSI26000U7,HSI25000O7,HSI16600A7," 
			+ "HHI7500R7,HHI7200M7,HSI16200C7,HHI7200C7,HHI9200O7,HSI22800O7,HHI10200X7,HHI11000X7,HSI25400M7," 
			+ "HSI17400A7,HSI15000B7,HHI9400R7,HSI21600N7,HSI21800I7,HHI10000X8,HSI19400B7,HSI21800F7,HHI9000A7,"
			+ "HSI23600R7,HSI18400X7,HHI7900O7,HSI17000F7,HSI19800N7,HSI23200F7,HSI23400I7,HHI11000M7,HSI17400O7,"
			+ "HSI17800B7,HSI24200A7,HHI7700L7,HSI15600F7,HSI25000A7,HSI22800A7,HSI16600O7,HSI23000N7,HHI8000O7,HSI23600A7,"
			+ "HSI25200R7,HSI18600B7,HSI20800N7,HHI9000U7,HSI16400F7,HSIJ7,HHI7800M7,HSI16200N7,HSI15600I7,HSI22800L7,HHI7300N7,"
			+ "HSI24400U7,HSI20800C7,HHI9800O7,HSI17600X7,HSI18200O7,HHI7900F7,HSI20000O7,HSI16800R7,HSI15800I7,HHI8000F7,"
			+ "HHI8400L7,HSI16000N7,HSI17400R7,HSI19800C7,HHI10400M7,HSI18000I7,HHI8400B7,HSI17800X7,HSI24600U7,HHI11000B7,"
			+ "HSI25200U7,HSI21000X7,HSI24000C7,HSI16600R7,HSI17200I7,HSI23800U7,HSI23000C7,HSI21600C7,HSI21800L7,HHI7800C7,"
			+ "HHI9200F7,HSI23000F8,HHI7100U7,HSI24000R9,HSI23600U7,HSI22400C7,HHI9400I7,HSI23200C7,HHI9600B7,HSI17000I7,"
			+ "HSI21800C7,HSI22600L7,HHI7300X7,HSI25000I7,HHI7200N7,HSI16600I7,HSI15600L7,HSI24000L7,HSI24600B7,HSI26000M7,"
			+ "HSI17200O7,HHI8400U7,HHI10200B7,HSI16800A7,HSI16800O7,HHI8800A7,HSI18800B7,HSI19800F7,HSI25600M7,HHI7700M7,"
			+ "HHI10400X7,HSI24000F8,HHIF7,HSI17200A7,HSI25400U7,HHI7600I7,HHI7800O7,HSI22400I7,HSI23800M7,HSI24000L9,HHI7200X7,"
			+ "HHI7700C7,HHI10600N7,HSI23400F7,HSI23200I7,HHIZ7,HSI17600O7,HHI8600O7,HSI21000B7,HSI19000O7,HHI8400A7,HSI22000M7,"
			+ "HSI26200U7,HSI16400O7,HHI11000U7,HHI8800R7,HSI18400B7,HHI7800L7,HSI25400B7,HHI10000U7,HHI9200N7,HSI20800F7,HHIK7,"
			+ "HSI19200B7,HHI10200M7,HSI24200I7,HSI21600F7,HSI20200B7,HSI23000F7,HHI7300F7,HSI23800B7,HHI7500I7,HSI22800I7,HSI18000O7,"
			+ "HHI8200R7,HSI15800O7,HHI10200U7,HSI17000L7,HHI7100C7,HSI16800L7,HSI23000L8,HHI9200X7,HSI15600A7,HSI22400F7,HSI16600L7,"
			+ "HSI25000F7,HSI21600I7,HSI24800M7,HSI23000L9,HSI21400B7,HSI15600O7,HHI10400U7,HSI22600F7,HSI15800A7,HHI9600C7,"
			+ "HSI19600B7,HSI17200L7,HHI7800B7,HHI10800C7,HHI8600R7,HSI24000I7,HSI25800C7,HSI24600M7,HSI22000B7,HHI7400F7,HSI21200B7,"
			+ "HSI16400L7,HHI7300M7,HSI17400L7,HSI22800F7,HHI10000X7,HSI24200F7,HSI20600B7,HHI8800U7,HSIQ7,HSI16400A7,HSI20000L7,"
			+ "HHI9800F7,HHI9600M7,HHI10600C7,HSI20800I7,HSI25200M7,HSI19000L7,HHIX7,HSI23000I7,HSI17600L7,HSI22200B7,HSI24400M7,"
			+ "HHI7300C7,HHI7500F7,HSI19800I7,HSI18200L7,HSI23600M7,HSI23400U7,HSI19000I7,HSI16600X7,HHIV7,HSI23600F7,HHI7900C7,"
			+ "HHI9400A7,HHIQ7,HHI11000O7,HSI24200C7,HSI21000X9,HSI19200O7,HHIG7,HSI22400A7,HSI17400F7,HSI15200M7,HSI21800R7,HHI7300L7,"
			+ "HHI7700X7,HSI22000F8,HSI17200U7,HSI21200N7,HSI25400A7,HHI9400U7,HHI10800I7,HSI20600F7,HHI7600C7,HSI20400R7,HSI22000U7,"
			+ "HSI22400O7,HSI22800X7,HSI24400I7,HSI18000N7,HSI20000B7,HSI24800U7,HSI22200F7,HSI15800N7,HSI18600R7,HSI16800I7,HSI25800F7,"
			+ "HHI9800M7,HHI8800L7,HSI15400L7,HSI24000O7,HSI16400N7,HSI25200B7,HHI8600N7,HHI9600O7,HSI18800C7,HHI10600X7,HHI7100R7,"
			+ "HHI7100M7,HSI25800I7,HSI21400X7,HSI23000L7,HHI7200F7,HSI17600B7,HHI9600I7,HSI26000I7,HSI25400R7,HHI9200C7,HSI21200C7,"
			+ "HHI7100X7,HSI18800F7,HSI15200B7,HSI23800I7,HSI21800A7,HSI16000I7,HHI9600N7,HSI21800U7,HSI20600C7,HHI8200A7,HSI17400I7,"
			+ "HSI24200N7,HSI20800L7,HSI18200B7,HSI19000F7,HSI16600M7,HSI22600N7,HSI24000A7,HHI7600L7,HSI23600C7,HSI24800R7,HHI10000I7,"
			+ "HSI16800F7,HSI22600C7,HHI7400X7,HSI22000X7,HHI8400N7,HSI16200B7,HHI8200F7,HSI22400L7,HSI23000R9,HSI20400O7,HSI18600O7,"
			+ "HHI10400R7,HHI10800F7,HHI8000M7,HHI10600M7,HSI19600X7,HSI15000N7,HHI8200U7,HSI17200R7,HSI15400I7,HSI20400X7,HHI9000C7,"
			+ "HSI15600R7,HSI21000O7,HSI22800U7,HHI7900R7,HHI11000R7,HSI25000U7,HSI17400N7,HHI8800F7,HHI9600A7,HSI24800A7,HSI23000X9,"
			+ "HSI23000A7,HSI22200C7,HSI15400F7,HSI15200U7,HHI7700R7,HSI23200N7,HSI19600U7,HSI16400C7,HSI20800O7,HSI23000O7,HHI7900X7,"
			+ "HSI22000R7,HHI9000O7,HHI8000N7,HSI24000U7,HSI18600X7,HHI10400A7,HHI7100L7,HHI10600B7,HHI10800O7,HSIV7,HSI21000U7,"
			+ "HSI21000F8,HSI17000N7,HSIN7,HSI17200X7,HSI17800C7,HHI9400B7,HSI24400A7,HHI8400F7,HSI20000X7,HHI8000U7,HSI17200M7,"
			+ "HSI15600U7,HSI17600R7,HSI16000C7,HSI20600N7,HSI23400O7,HSI15800F7,HSI23400A7,HHI9800N7,HHI8800M7,HHI7500X7,HHI9600U7,"
			+ "HSI25000R7,HHIU7,HSI15000C7,HHI7200L7,HHI7600X7,HHI8200M7,HSI18000F7,HSI19400C7,HSI23800R7,HSI26000R7,HHI9200I7,"
			+ "HHI10000L7,HSI19200R7,HHI10400O7,HSI18200X7,HHI10800A7,HSI21400U7,HSI16200M7,HHI9000B7,HHI8400M7,HHI7600R7,HSI21800O7,"
			+ "HSI18600U7,HHI7800X7,HSI17400C7,HSI22200N7,HSI18400N7,HHIN7,HSI24000X9,HHI10400L7,HSI23800A7,HSI21000R7,HSI22800R7,"
			+ "HSI16600U7,HSI20400U7,HHI9200B7,HSI26000A7,HSI24400O7,HSI19000N7,HSI15400C7,HSI19600R7,HHI10000O7,HSI25800O7,HSI23400R7,"
			+ "HSI15600X7,HHI8600M7,HHI11000L7,HSI26000O7,HSI17800N7,HSI17000C7,HSI16000F7,HSI19200U7,HSI18000C7,HSI18800N7,HSI24000R7,"
			+ "HHI9000I7,HSI16800C7,HHI10000A7,HHI7300R7,HSI21400R7,HHI9400O7,HSI19000C7,HSI16200X7,HSI18200U7,HSI24800O7,HHI10800L7,"
			+ "HHI7400R7,HSI15800C7,HHI11000A7,HSI22400R7,HSI15200X7,HSI19400N7,HHI8600F7,HSI20000U7,HSI23800O7,HSI20200N7,HSI25400O7,"
			+ "HSI25800A7,HSI15000F7,HSI22000X9,HSI17600U7,HSI23000R7,HSIG7,HSI20800R7,HHI9800U7,HHI10400I7,HSI22200R7,HSI20400I7,HSI22400X7,"
			+ "HHI7100F7,HHI10600O7,HSI24800I7,HHI10600A7,HHI7600O7,HSI18600I7,HSI20800U7,HHI8600U7,HHI9800A7,HSI17600N7,HSI22600B7,"
			+ "HSI24000M7,HSI26200F7,HSI17000X7,HHI10200C7,HSI16800N7,HSI22000R8,HSI23000X8,HHI8200N7,HSI20600O7,HSI18400C7,HHI7500M7,"
			+ "HSI23200M7,HSI16000R7,HHI9000F7,HHI7200R7,HSI19200C7,HSI19400I7,HSI21600U7,HHI7900A7,HSI15000I7,HHI7500C7,HHIJ7,HSI23800C7,"
			+ "HHI7400U7,HHI8200X7,HSI23000U7,HSI24600C7,HSI17000M7,HSI16200U7,HSI26000C7,HSI15600M7,HSI21000C7,HSI20200C7,HSIU7,"
			+ "HSI25400C7,HSI15400U7,HSI22000L7,HSI19800U7,HHI11000F7,HSIZ7,HSI21200L7,HSI21000L9,HSI19800X7,HHI9400N7,HSI20200F7,"
			+ "HSI18600F7,HHI8000A7,HSI24400N7,HSI15200R7,HSI22000O7,HSI16400M7,HSI18000B7,HSI22000A7,HSI20600L7,HHI10800R7,"
			+ "HHI7300I7,HHI7400L7,HHI8600B7,HSI15000L7,HSI15400R7,HHI9400X7,HSI22200O7,HHI7800R7,HSI26200I7,HSI20400F7,HSI21600X7,"
			+ "HHI10000N7,HSI22400U7,HHI10600R7,HSI21400O7,HSIK7,HSI20800X7,HSI25400I7,HSI15800B7,HSI17200B7,HSI23800N7,HHI8600L7,"
			+ "HSI25800R7,HSI21000F7,HSI19200F7,HSI19600O7,HSI21200O7,HHI11000I7,HSI18400F7,HSI25600I7,HSI15600B7,HSI17000B7,"
			+ "HSI24600N7,HSI23600N7,HSI18800O7,HHI7400B7,HSI19400F7,HSI16400B7,HHI9200A7,HSI22200A7,HSI17800F7,HHI8800O7,HSI25200N7,"
			+ "HSI15000O7,HSI17800L7,HHI8200O7,HSI22400B7,HSI19400O7,HSI18800I7,HHI7500L7,HSI24400C7,HHI9800B7,HSI16600B7,HHI10000C7,"
			+ "HSI25400N7,HSI25000B7,HSI16200R7,HSI23400M7,HHI8000I7,HHI9200U7,HSI23000X7,HSI21400L7,HSI15000A7,HHI7500B7,HSI20200I7,"
			+ "HSI21200F7,HSI22200X7,HHI7900I7,HSI24600I7,HSI25600F7,HSI21000I7,HHI9600F7,HHI9800L7,HSI26200C7,HSI21800X7,HSI22000R9,"
			+ "HSI24200B7,HHI7700F7,HSI22000F7,HSI24800C7,HSI19600L7,HSI15200L7,HSI22800B7,HSI22200L7,HSI25200C7,HHI10800X7,HSI23000M7,"
			+ "HHI11000N7,HSI25800U7,HSI19200I7,HHI10400F7,HSI24000X7,HSI22600X7,HSI21000R9,HHI10200N7,HSI20400L7,HSI18400I7,HSI16000L7,"
			+ "HSI19800B7,HHI8800N7,HSI24600F7,HHI8000L7,HSI19200L7,HHI10200F7,HHI9000X7,HSI16000A7,HSI16000O7,HHI8200L7,HHI8400R7,"
			+ "HSI25600C7,HSI18800L7,HSI22600M7,HSI17800I7,HHI8200B7,HSI23600B7,HSI19400L7,HSI21000L7,HSI24000B7,HHI11000C7,HSI24800F7,"
			+ "HSI19600I7,HHI10000F7,HHI9800I7,HSI21000L8,HHI9400M7,HHI8800X7,HSI22400M7,HSI18600L7,HHI9400C7,HSI23400B7,HSI16200O7,"
			+ "HHI10000F8,HSI15400O7,HHI10600U7,HSI21800M7,HSI15200A7,HSI21800B7,HHIM7,HSI24200M7,HSIF7,HHI7800I7,HHI7900L7,HSI22200I7,"
			+ "HHI7400M7,HSI18400L7,HHI7600F7,HSI25400F7,HSI21200I7,HHI7400C7,HHI9000N7,HSI23200B7,HHI7700I7,HSI15200O7,HHI10800U7,HHI7900B7,"
			+ "HSI20600I7,HSI22000I7,HSI15400A7,HSI24400F7,HHI8000B7,HHI8600A7,HHI10400C7,HSI20800B7,HSI23800F7,HSI23000B7,HSI16200A7,"
			+ "HSI26000F7,HSI25200F7,HSI21400I7,HSI21600B7,HSI25000M7,HSI22800M7,HSI22000L8,HSI17000A7,HSI25600U7,HSI21600L7,HSI24000F9,"
			+ "HSI25800M7,HHI9800R7,HHI7400I7,HSI22000L9,HSI21400F7,HSIM7,HSI21200U7,HHI9400F7,HHI7800U7,HHI8000C7,HHI8400O7,HHI7400N7,"
			+ "HSI17400M7,HSI22000C7,HSI24600A7,HSI15200F7,HSI17000O7,HSIH7,HSI25000C7,HSI16800B7,HHI10000B7,HSI19600F7,HSI23400N7,"
			+ "HSI19400U7,HHI7300B7,HSI19000B7,HSI23200A7,HSI18000U7,HHI8800B7,HSI18200F7,HHI7800A7,HSI20000I7,HHI10600F7,HHI9800C7,"
			+ "HSI26200R7,HHI10200L7,HSI20200L7,HHI10000M7,HHI10200O7,HHI10800B7,HSI16000M7,HHI10400N7,HSI18200I7,HSI15800X7,HSI16200L7,"
			+ "HSI25200I7,HSI19800L7,HSI17400X7,HSI15000R7,HHI7600M7,HSI24400B7,HSI22200M7,HSI17200N7,HHI7800F7,HSI17600I7,HHI8600I7,"
			+ "HSI17800R7,HSI23200O7,HSI15400B7,HSI23600I7,HSI24000X8,HHI7500O7,HHI9000M7,HSI19600C7,HHI7100N7,HHI7600B7,HHI9200M7,"
			+ "HHI9000R7,HSI16600F7,HHI7200A7,HSI23400C7,HSI17800O7,HHI8600X7,HHI7100I7,HSI22800C7,HHI9200R7,HSI25600R7,HSI17400B7,"
			+ "HSI21800N7,HHI8800C7,HSI15200I7,HSI16000B7,HSI21400C7,HSI19800O7,HHI7200U7,HSI18400O7,HSI24800N7,HSI19400X7,HSI17000R7,"
			+ "HHI9600X7,HSI20200O7,HSI15000U7,HSI16800M7,HSIX7,HSI22600U7,HSI22800N7,HHI8000R7,HSI25000N7,HSI20600X7,HHI8400I7,"
			+ "HSI18000R7,HSI21000R8,HHI10200I7,HSI20000F7,HSI16400R7,HSI21200X7,HHI10200R7,HSI18800X7,HSI24200U7,HHIH7,HHI10000L8,"
			+ "HHI7700O7,HHI8400X7,HHI7900M7,HHI10800M7,HSI16200I7,HSI19000X7,HSI19600N7,HSI18400R7,HSI15400M7,HHI7300A7,HSI22600A7,"
			+ "HSI15200N7,HSI25200O7,HHI7200I7,HSI17600F7,HSI17400U7,HSI25200A7,HSI15800R7,HSI18600C7,HSI21000N7,HHI10600I7,HHI7900N7,"
			+ "HSI16400X7,HSI24200R7,HSI22000F9,HSI20000C7,HHI10000R8,HSI23200U7,HSI15800M7,HHI8000X7,HHI8200C7,HSI15600N7,HHI7200B7,"
			+ "HSI24000N7,HSI23000R8,HHI7300U7,HSI20400C7,HSI18800R7,HSI17000U7,HSI16600N7,HSI21000X8,HHI7700A7,HHI7100B7,HSI22600O7,"
			+ "HHI7600N7,HSI16200F7,HSI24600R7,HSI24000R8,HSI16000U7,HSI22000X8,HHI9800X7,HHI9400L7,HSI21400N7,HSI22200U7,HSI21600O7,"
			+ "HHI7900U7,HSI22400N7,HSI16800X7,HSI20200R7,HHI7400O7,HSI17200F7,HHI7500N7,HSI20600U7,HSI15200C7,HSI20200U7,HSI26200A7,"
			+ "HSI16400U7,HSI24200O7,HHI7700U7,HSI19400R7,HSI20000N7,HSI23200R7,HHI8600C7,HSI15400X7,HHI8800I7,HSI23000F9,HSI18000X7,"
			+ "HHI7300O7,HHI7500U7,HSI20600R7,HHI7600A7,HSI17600C7,HSI22000N7,HSI18800U7,HSI15800U7,HSI20400N7,HSI21200R7,HSI23600O7,"
			+ "HHI7400A7,HHI9000L7,HSI18200N7,HSI15600C7,HSI22600R7,HHI9200L7,HSI24600O7,HHI7100O7,HHI7700N7,HSI15000X7,HSI15000M7,"
			+ "HHI10200A7,HSI19200N7,HSI16800U7,HSI19800R7,HSI25600O7,HHI7200O7,HSI18200C7,HSI17800U7,HSI25600A7,HSI26200O7,HHI9600R7,"
			+ "HSI17200C7,HSI18400U7,HHI8400C7,HSI19000U7,HSI21000F9,HHI7600U7,HSI21600R7,HHI7500A7,HSI18600N7,HSI16000X7,HHI7800N7,HHI10600L7,HSI16600C7";
for (String s : testSymbol.split(","))
	this.addTradableInstrument(s);
	}

	@Override
	public void onLastPrice(String symbol, double price)
	{
		m_mds.onLastPrice(symbol,  price);
	}
}
