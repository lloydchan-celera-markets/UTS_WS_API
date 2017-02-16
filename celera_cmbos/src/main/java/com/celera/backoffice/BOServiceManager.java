package com.celera.backoffice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.backoffice.service.SendAttachmentInEmail;
import com.celera.backoffice.service.SendByGmail;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineClient;
import com.celera.ipc.URL;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.EBoCommand;
import com.celera.message.cmmf.ICmmfConst;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.mongo.entity.Invoice;

public class BOServiceManager extends CmmfApp implements ILifeCycle
{
	private static final Logger logger = LoggerFactory.getLogger(BOServiceManager.class);
	
	private static final String CHANNEL_PULL_PROTOCOL;
	private static final String CHANNEL_PULL_IP;
	private static final Integer CHANNEL_PULL_PORT;
	private static final URL PULL_URL;
	
	private static final String CHANNEL_SINK_PROTOCOL;
	private static final String CHANNEL_SINK_IP;
	private static final Integer CHANNEL_SINK_PORT;
	private static final URL SINK_URL;
	
	private final static SimpleDateFormat cmmfSdf = new SimpleDateFormat(ICmmfConst.MONTH_FORMAT);
	private final static SimpleDateFormat cmmfLogSdf = new SimpleDateFormat(ICmmfConst.JSON_LOG_DATE_FORMAT);
	
	private ILifeCycle taskChannel;

	static
	{
		CHANNEL_PULL_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_PULL_PROT);
		CHANNEL_PULL_IP = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_PULL_IP);
		CHANNEL_PULL_PORT = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_PULL_PORT));
		PULL_URL = new URL(CHANNEL_PULL_PROTOCOL, CHANNEL_PULL_IP, CHANNEL_PULL_PORT);

		CHANNEL_SINK_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_SINK_PROT);
		CHANNEL_SINK_IP = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_SINK_IP);
		CHANNEL_SINK_PORT = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_SINK_PORT));
		SINK_URL = new URL(CHANNEL_SINK_PROTOCOL, CHANNEL_SINK_IP, CHANNEL_SINK_PORT);
	}
	
	public BOServiceManager()
	{
		super(EApp.BOServiceManager);
	}
	
	public byte[] onQuery(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void onAdmin(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

	public void onResponse(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

	public void onTask(byte[] data)
	{
		String msg = new String(data);
		logger.debug("{}", msg);
		
		EBoCommand cmd = EBoCommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		switch (cmd)
		{
		case EMAIL_INVOICE:
		{
			String param = msg.substring(ICmmfConst.HEADER_SIZE);
			String[] params = param.split(",");
			
			try
			{
				List<Invoice> list = new ArrayList<Invoice>();
				logger.debug("email invoice {}", param);
				for (String id : params) {
					Invoice inv = (Invoice) DatabaseAdapter.get(id);
					list.add(inv);
					inv.setHasSent(true);
					DatabaseAdapter.update(inv);
				}
				
				SendAttachmentInEmail.sendEmail(list, 0, 0);
//				SendByGmail.sendEmail(list);
			} catch (Exception e)
			{
				logger.error("", e);
			}
			break;
		}
		case EMAIL_BATCH_INVOICE:
		{
			String param = msg.substring(ICmmfConst.HEADER_SIZE);
			String[] params = param.split(",");
			
			try
			{
				Integer batch = Integer.valueOf(params[0]);
				Integer total = Integer.valueOf(params[1]);
				List<Invoice> list = new ArrayList<Invoice>();
				logger.debug("email invoice {}", param);
				for (int i = 2; i<params.length; i++){ 
					String id = params[i];
					Invoice inv = (Invoice) DatabaseAdapter.get(id);
					list.add(inv);
					inv.setHasSent(true);
					DatabaseAdapter.update(inv);
				}
				
				SendAttachmentInEmail.sendEmail(list, batch, total);
//				SendByGmail.sendEmail(list);
			} catch (Exception e)
			{
				logger.error("", e);
			}
			break;
		}
		case CREATE_INVOICE:
		{
			try
			{
				String temp = new String(data);
				String key = temp.substring(ICmmfConst.HEADER_SIZE);
				
				DbCreateInvoice gen = new DbCreateInvoice(key);
				gen.createInvoice();
				
			} catch (Exception e)
			{
				logger.error("", e);
			}
			break;
		}
		case UPDATE_INVOICE:
		{
			String id = null;
			IMongoDocument o = null;
			try
			{
//				msg = new String(data, ICmmfConst.HEADER_SIZE);
				String temp = new String(data).substring(ICmmfConst.HEADER_SIZE);
				JSONObject jsnobject = new JSONObject(temp);

				id = jsnobject.getString("id");
				o = (IMongoDocument) DatabaseAdapter.get(id);
				Invoice inv = (Invoice) o;
				inv.setId(id);
				
				try
				{
					String invoice_number = jsnobject.getString("invoice_number");
					inv.setInvoice_number(invoice_number);
				} catch (Exception e)
				{}
				try
				{
					String invoice_date = jsnobject.getString("invoice_date");
					inv.setInvoice_date(invoice_date);
				} catch (Exception e)
				{}
				try
				{
					String amount = jsnobject.getString("amount");
					inv.setAmount(amount);
					inv.setAmount_due(amount);
				} catch (Exception e)
				{}
				try
				{
					String size = jsnobject.getString("size");
					inv.setSize(size);
				} catch (Exception e)
				{}
				try
				{
					String hedge = jsnobject.getString("hedge");
					inv.setHedge(hedge);
				} catch (Exception e)
				{}
				try
				{
					Boolean isPaid = jsnobject.getBoolean("isPaid");
					inv.setIsPaid(isPaid);
				} catch (Exception e)
				{}
//				try
//				{
//					Boolean hasSent = jsnobject.getBoolean("hasSent");
//					inv.setHasSent(hasSent);
//				} catch (Exception e)
//				{}

				logger.info("update invoice - {}", inv);
				
//				DatabaseAdapter.update(inv);
			} catch (ClassCastException e)
			{
				logger.error("Not invoice object - {}", o, e);
			} catch (NullPointerException e)
			{
				logger.error("Invoice no exist - {}", id, e);
			} catch (Exception e)
			{
				logger.error("{}", id, e);
			}
			break;
		}
		case REMOVE_INVOICE:
		{
			String ids = null;
			IMongoDocument o = null;
			try
			{
//				msg = new String(data, ICmmfConst.HEADER_SIZE);
				ids = new String(data).substring(ICmmfConst.HEADER_SIZE);
				for (String id : ids.split(",")) 
				{
					o = (IMongoDocument) DatabaseAdapter.get(id);
					
					if (o != null) {
						DatabaseAdapter.delete(o);
						logger.info("delete invoice object - {}", o);
					}
				}
			} catch (ClassCastException e)
			{
				logger.error("Not invoice object - {}", o, e);
//			} catch (NullPointerException e)
//			{
//				logger.error("Invoice no exist - {}", id, e);
			} catch (Exception e)
			{
				logger.error("{}", ids, e);
			}
			break;
		}
		case LOG:
		{
			try
			{
				String temp = new String(data).substring(ICmmfConst.HEADER_SIZE);
				JSONObject jsnobject = new JSONObject(temp);
				try
				{
					String time = jsnobject.getString("time");
					String message = jsnobject.getString("message");
					com.celera.mongo.entity.Log l = new com.celera.mongo.entity.Log();
					l.setMessage(message);
					l.setLastModified(cmmfLogSdf.parse(time));
					DatabaseAdapter.create(l);
					logger.info("save log {}", l);
				} catch (Exception e)
				{}
			} catch (Exception e)
			{
				logger.error("", e);
			}
			break;
		}
		}
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
	
//	public static void main_run(String[] args)
	public static void main(String[] args)
	{
		int interval = 100000;
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		DatabaseAdapter.loadAll();
		
		BOServiceManager sm = new BOServiceManager();
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
	
//	public static void main(String[] arg)
	public static void main_test(String[] arg)
	{
//		byte[] b = {87, 68, 84, 71, 50, 34, 105, 100, 34, 58, 34, 53, 56, 50, 51, 101, 53, 100, 56, 51, 50, 55, 99, 54, 102, 56, 50, 51, 50, 51, 54, 102, 99, 49, 99, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 110, 117, 109, 98, 101, 114, 34, 58, 34, 67, 69, 76, 45, 116, 101, 115, 116, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 100, 97, 116, 101, 34, 58, 34, 48, 57, 32, 74, 117, 108, 121, 44, 32, 50, 48, 49, 54, 34, 44, 34, 97, 109, 111, 117, 110, 116, 34, 58, 34, 85, 83, 36, 49, 44, 57, 48, 57, 34, 44, 34, 115, 105, 122, 101, 34, 58, 34, 49, 44, 52, 48, 48, 34, 44, 34, 104, 101, 100, 103, 101, 34, 58, 34, 49, 54, 53, 34, 44, 34, 105, 115, 80, 97, 105, 100, 34, 58, 34, 102, 97, 108, 115, 101, 34, 44, 34, 104, 97, 115, 83, 101, 110, 116, 34, 58, 34, 102, 97, 108, 115, 101, 34, 125};
//		byte[] b = {87, 68, 84, 71, 50, 48, 49, 54, 49, 50};
		
//		String s = "WDQGHKD1216BNP Paribas Arbitrage S.N.C.*";
//		String s = "WDTE582d69b8327c2418e8c3782c,582d69f1327c2418e8c3782d,582d5738327cb2495d00abc2";
//		String s = "WDQGHKD1016UBS AG London Branch";
		String s = "WATE583ea790bebc95fe10b0206d,583ea790bebc95fe10b02074";

		byte[] b = s.getBytes();
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		DatabaseAdapter.loadAll();
//dba.onQuery(b);
//		DatabaseAdapter.jsonInvoices();
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			BOServiceManager sm = new BOServiceManager();
			sm.onMessage(b);
		}
//		Object o = DatabaseAdapter.get("5823e5db327c6f823236fe83");
//	
//		BOServiceManager bo = new BOServiceManager();
//		bo.onMessage(b);
		
//		System.out.println(o);
	}

	@Override
	public void onSink(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}
}
