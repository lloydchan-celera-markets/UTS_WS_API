package com.celera.webtrader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.celera.backoffice.service.SendAttachmentInEmail;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.ESide;
import com.celera.core.dm.ETradeReportType;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.TradeReport;
//import com.celera.exchange.HKExOapi;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineClient;
import com.celera.ipc.RrServer;
import com.celera.ipc.URL;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EAdminCommand;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.EMessageType;
import com.celera.message.cmmf.ICmmfConst;
//import com.uts.tradeconfo.UtsTradeConfoSummary;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.entity.TradeConfo;

import come.celera.core.oms.IOMS;
import come.celera.core.oms.IOMSListener;
import come.celera.core.oms.OMS;

public class WTService extends CmmfApp implements ILifeCycle, IOMSListener
{
	private static final Logger logger = LoggerFactory.getLogger(WTService.class);
	
	private static final String CHANNEL_PULL_PROTOCOL;
	private static final String CHANNEL_PULL_IP;
	private static final Integer CHANNEL_PULL_PORT;
	private static final URL PULL_URL;
	
	private static final String CHANNEL_SINK_PROTOCOL;
	private static final String CHANNEL_SINK_IP;
	private static final Integer CHANNEL_SINK_PORT;
	private static final URL SINK_URL;
	
	private final static SimpleDateFormat cmmfSdf = new SimpleDateFormat(ICmmfConst.MONTH_FORMAT);
	
	private PipelineClient taskChannel;
	private IOMS oms;
	
	static
	{
		CHANNEL_PULL_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_CHL_PULL_PROT);
		CHANNEL_PULL_IP = ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_CHL_PULL_IP);
		CHANNEL_PULL_PORT = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_CHL_PULL_PORT));
		PULL_URL = new URL(CHANNEL_PULL_PROTOCOL, CHANNEL_PULL_IP, CHANNEL_PULL_PORT);

		CHANNEL_SINK_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_CHL_SINK_PROT);
		CHANNEL_SINK_IP = ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_CHL_SINK_IP);
		CHANNEL_SINK_PORT = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_CHL_SINK_PORT));
		SINK_URL = new URL(CHANNEL_SINK_PROTOCOL, CHANNEL_SINK_IP, CHANNEL_SINK_PORT);
	}
	
	public WTService()
	{
		super(EApp.WEB_TRADER);
		
		oms = new OMS();
		oms.addListener(this);
	}
	
	public byte[] onQuery(byte[] data)
	{
		String msg = new String(data);
		logger.debug("onQuery {}", msg);
		
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		switch (cmd)
		{
		case TRADE_REPORT:
		{
			List<IOrder> l = oms.getAllTradeReport();
			for (IOrder order : l) {
				onBlockTradeReport(order);
			}
			
//			try
//			{
//				Thread.sleep(1000);
//				this.taskChannel.sink(new String("helloworld").getBytes());
//				oms.sendTradeReport(null);
				
//				List<Invoice> list = new ArrayList<Invoice>();
//				for (String id : params) {
//					Invoice inv = (Invoice) DatabaseAdapter.get(id);
//					if (!inv.getHasSent()) {
//						logger.debug("email invoice {}", id);
//						list.add(inv);
//						inv.setHasSent(true);
//						DatabaseAdapter.update(inv);
//					}
//					else {
//						logger.debug("email has been sent before. Please manual send the invoice {}", inv);
//					}
//				}
//				
//				SendAttachmentInEmail.sendEmail(list);
//			} catch (Exception e)
//			{
//				logger.error("", e);
//			}
			break;
		}
		default: break;
		}
		return null;
	}

	public void onAdmin(byte[] data)
	{
		String msg = new String(data);
		logger.debug("onAdmin {}", msg);
		
		EAdminCommand cmd = EAdminCommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		switch (cmd)
		{
		case SOD:
		{
//			taskChannel.connect();
//			taskChannel.sink("0".getBytes());
			break;
		}
		default: break;
		}
	}

	public void onResponse(byte[] data)
	{
	}

	public void onTask(byte[] data)
	{
		String msg = new String(data);
		logger.debug("onTask {}", msg);
		
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		switch (cmd)
		{
		case TRADE_REPORT:
		{
			String temp = new String(data).substring(ICmmfConst.HEADER_SIZE);
			JSONObject jsonbject = new JSONObject(temp);
			
			try {
//				JSONObject message = jsonbject.getJSONObject("message");
				Long refId = jsonbject.getLong("RefId");
				String sTrType = jsonbject.getString("TrType");
				ETradeReportType trType = ETradeReportType.get(sTrType);
				String sSide = jsonbject.getString("Side");
				ESide side = ESide.get(sSide);
				String cp = jsonbject.getString("Cp");
				
				// log
				Integer qty = jsonbject.getInt("Qty");
				String futMat = jsonbject.getString("FutMat");
//				Double strike = jsonbject.getDouble("strike");
				Double delta = jsonbject.getDouble("Delta");
				String symbol = jsonbject.getString("Symbol");
				
				switch (trType) {
				case T1_SELF_CROSS: {
					break;
				}
				case T2_COMBO_CROSS: {
					JSONArray ary = jsonbject.getJSONArray("Legs");

//					public Derivative(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE,
//							String rIC, Double strike, String expiry, Double price,
//							Boolean isPriceInPercent, Double delta)
					
					IInstrument deriv = new Derivative("HK", symbol, null, null, null, null, null, null, futMat, null, false, delta);
					BlockTradeReport block = new BlockTradeReport(deriv, EOrderStatus.SENT, trType, qty, null,
							null, refId, side, "HKCEL", cp);
					
					List<IOrder> l = new ArrayList<IOrder>();
					for (Object o: ary) 
					{
						JSONObject jo = (JSONObject)o;
						
						String ul = jo.getString("UL");
						String sType = ul.split(" " )[1];
						EInstrumentType instrType = EInstrumentType.get(sType);
						
						Double price = jo.getDouble("Price");
						qty = jo.getInt("Qty");
						sSide = jo.getString("Side");
						side = ESide.get(sSide);
						symbol = jo.getString("Instrument");
						String expiry = jo.getString("Expiry");
						
						Double strike = null;
						try {
							strike = jo.getDouble("Strike");	// future no strike
						}
						catch (Exception e) {}
						IInstrument instr = new Derivative("HK", symbol, instrType, ul, "", "",
								"", strike, expiry, price, false, null);
						
						TradeReport tr = new TradeReport(instr, EOrderStatus.SENT, trType, qty, price, null, refId, side, "HKCEL", cp);
						block.add(tr);
					}
					oms.sendBlockTradeReport(block);
					break;
				}
				case T4_INTERBANK_CROSS: {
					break;
				}
					default :
				}
				

			}
			catch (Exception e) {
				logger.error("{}", e);
			}
			break;
		}
		default: break;
		}
//		logger.debug(data.toString());
	}
	
	public void init()
	{
		taskChannel = new PipelineClient(PULL_URL, SINK_URL, this);
		taskChannel.init();
	}

	public void start()
	{
		taskChannel.start();
	}

	public void stop()
	{
		taskChannel.stop();		
	}
	
	public void onSink(byte[] data)
	{
		System.out.println(new String(data));
	}
	
	public void onOrder(IOrder o)
	{
		taskChannel.sink("1,succ".getBytes());
	}

	public void onQuote(IOrder q)
	{
		// TODO Auto-generated method stub
		
	}

	public void onTrade(ITrade t)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void onTradeReport(IOrder l) {
		
	}
	
	public void onBlockTradeReport(IOrder blockTradeReport)
	{
		String msg = buildTradeReport(blockTradeReport, EMessageType.RESPONSE);
		this.taskChannel.sink(msg.getBytes());
	}
	
	public String buildTradeReport(IOrder o, EMessageType type)
	{
		JsonObjectBuilder ansBuilder = Json.createObjectBuilder();
		ansBuilder.add("sender", EApp.OMS.toString());
		ansBuilder.add("receiver", EApp.WEB_TRADER.toString());
		ansBuilder.add("message_type", type.toString());
		ansBuilder.add("command", ECommand.TRADE_REPORT.toString());
		ansBuilder.add("tradeReport", o.json());
		return ansBuilder.build().toString();
	}
	
//	public static void main_test(String[] arg)
//	{
////		byte[] b = {87, 68, 84, 71, 50, 34, 105, 100, 34, 58, 34, 53, 56, 50, 51, 101, 53, 100, 56, 51, 50, 55, 99, 54, 102, 56, 50, 51, 50, 51, 54, 102, 99, 49, 99, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 110, 117, 109, 98, 101, 114, 34, 58, 34, 67, 69, 76, 45, 116, 101, 115, 116, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 100, 97, 116, 101, 34, 58, 34, 48, 57, 32, 74, 117, 108, 121, 44, 32, 50, 48, 49, 54, 34, 44, 34, 97, 109, 111, 117, 110, 116, 34, 58, 34, 85, 83, 36, 49, 44, 57, 48, 57, 34, 44, 34, 115, 105, 122, 101, 34, 58, 34, 49, 44, 52, 48, 48, 34, 44, 34, 104, 101, 100, 103, 101, 34, 58, 34, 49, 54, 53, 34, 44, 34, 105, 115, 80, 97, 105, 100, 34, 58, 34, 102, 97, 108, 115, 101, 34, 44, 34, 104, 97, 115, 83, 101, 110, 116, 34, 58, 34, 102, 97, 108, 115, 101, 34, 125};
////		byte[] b = {87, 68, 84, 71, 50, 48, 49, 54, 49, 50};
//		
////		String s = "WDQGHKD1216BNP Paribas Arbitrage S.N.C.*";
////		String s = "WDTE582d69b8327c2418e8c3782c,582d69f1327c2418e8c3782d,582d5738327cb2495d00abc2";
//		String s = "WDQGHKD1016UBS AG London Branch";
//		byte[] b = s.getBytes();
////		DatabaseAdapter dba = new DatabaseAdapter();
////		dba.start();
////		DatabaseAdapter.loadAll();
////dba.onQuery(b);
//		try
//		{
//			Thread.sleep(2000);
//		} catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			WTService sm = new WTService();
//			sm.onMessage(b);
//		}
////		Object o = DatabaseAdapter.get("5823e5db327c6f823236fe83");
////	
////		BOServiceManager bo = new BOServiceManager();
////		bo.onMessage(b);
//		
////		System.out.println(o);
//	}
	
	public static void main(String[] args)
	{
		int interval = 10000;
//		DatabaseAdapter dba = new DatabaseAdapter();
//		dba.start();
//		DatabaseAdapter.loadAll();
		
		WTService sm = new WTService();
		sm.init();
		sm.start();

		for (;;)
		{
			try
			{
//				logger.debug("sleep {}", interval);
				Thread.sleep(interval);
//				sm.onTask("TOTR".getBytes());
//				[TOTR{"UL":"HSI Call","Instrument":"HSI22000L7","Expiry":"DEC17","Strike":"22000","Qty":100,"Side":"Buy","Multiplier":1,"noPrice":false,"isValidate":false,"isEditable":true,"Price":"500"},{"UL":"HSI Call","Instrument":"HSI24000L7","Expiry":"DEC17","Strike":"24000","Qty":125,"Side":"Sell","Multiplier":-1.25,"noPrice":false,"isValidate":false,"isEditable":false,"Price":392},{"UL":"HSI Future","Instrument":"HSIF7","Expiry":"JAN17","Strike":"","Qty":20,"Side":"Buy","Price":22825,"Multiplier":0,"noPrice":false,"isValidate":false,"isEditable":false}]);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		// dba.stop();
	}
}
