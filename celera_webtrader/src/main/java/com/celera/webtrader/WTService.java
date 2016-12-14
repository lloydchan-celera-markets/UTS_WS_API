package com.celera.webtrader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.celera.backoffice.service.SendAttachmentInEmail;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
//import com.celera.exchange.HKExOapi;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineClient;
import com.celera.ipc.URL;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.ICmmfConst;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.TradeConfo;
//import com.uts.tradeconfo.UtsTradeConfoSummary;

import come.celera.core.oms.OMS;

public class WTService extends CmmfApp implements ILifeCycle
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
	
	private ILifeCycle taskChannel;
	private OMS oms;
	
	
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
	}
	
	public byte[] onQuery(byte[] data)
	{
		return null;
	}

	public void onAdmin(byte[] data)
	{
	}

	public void onResponse(byte[] data)
	{
	}

	public void onTask(byte[] data)
	{
		String msg = new String(data);
		logger.debug("{}", msg);
		
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		switch (cmd)
		{
		case TRADE_REPORT:
		{
			String param = msg.substring(ICmmfConst.HEADER_SIZE);
			String[] params = param.split(",");
			
			try
			{
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
			} catch (Exception e)
			{
				logger.error("", e);
			}
			break;
		}
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
	
	public static void main(String[] args)
	{
		int interval = 100000;
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
				logger.debug("sleep {}", interval);
				Thread.sleep(interval);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		// dba.stop();
	}
	
	public static void main_test(String[] arg)
	{
//		byte[] b = {87, 68, 84, 71, 50, 34, 105, 100, 34, 58, 34, 53, 56, 50, 51, 101, 53, 100, 56, 51, 50, 55, 99, 54, 102, 56, 50, 51, 50, 51, 54, 102, 99, 49, 99, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 110, 117, 109, 98, 101, 114, 34, 58, 34, 67, 69, 76, 45, 116, 101, 115, 116, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 100, 97, 116, 101, 34, 58, 34, 48, 57, 32, 74, 117, 108, 121, 44, 32, 50, 48, 49, 54, 34, 44, 34, 97, 109, 111, 117, 110, 116, 34, 58, 34, 85, 83, 36, 49, 44, 57, 48, 57, 34, 44, 34, 115, 105, 122, 101, 34, 58, 34, 49, 44, 52, 48, 48, 34, 44, 34, 104, 101, 100, 103, 101, 34, 58, 34, 49, 54, 53, 34, 44, 34, 105, 115, 80, 97, 105, 100, 34, 58, 34, 102, 97, 108, 115, 101, 34, 44, 34, 104, 97, 115, 83, 101, 110, 116, 34, 58, 34, 102, 97, 108, 115, 101, 34, 125};
//		byte[] b = {87, 68, 84, 71, 50, 48, 49, 54, 49, 50};
		
//		String s = "WDQGHKD1216BNP Paribas Arbitrage S.N.C.*";
//		String s = "WDTE582d69b8327c2418e8c3782c,582d69f1327c2418e8c3782d,582d5738327cb2495d00abc2";
		String s = "WDQGHKD1016UBS AG London Branch";
		byte[] b = s.getBytes();
//		DatabaseAdapter dba = new DatabaseAdapter();
//		dba.start();
//		DatabaseAdapter.loadAll();
//dba.onQuery(b);
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			WTService sm = new WTService();
			sm.onMessage(b);
		}
//		Object o = DatabaseAdapter.get("5823e5db327c6f823236fe83");
//	
//		BOServiceManager bo = new BOServiceManager();
//		bo.onMessage(b);
		
//		System.out.println(o);
	}
	
}