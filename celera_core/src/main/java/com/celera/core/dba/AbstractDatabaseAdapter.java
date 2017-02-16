package com.celera.core.dba;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IOverrideConfig;
import com.celera.ipc.ILifeCycle;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EApp;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.entity.IMongoDocument;

public abstract class AbstractDatabaseAdapter extends CmmfApp implements IOverrideConfig, IDatabaseAdapterService, ILifeCycle
{
	final static Logger logger = LoggerFactory.getLogger(AbstractDatabaseAdapter.class);

	private static final int IO_THREAD = 1;

	// private static final String CXF_SPI_PROVIDER;

	protected Map<String, ICustomizeMongoDocument> customizedMap = new ConcurrentHashMap<String, ICustomizeMongoDocument>();
	protected Map<String, IMongoDocument> all = new ConcurrentHashMap<String, IMongoDocument>();
//	private Date lastModified = null;

	final ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public AbstractDatabaseAdapter()
	{
		super(EApp.DBA);

		overrideCfg();
	}

	@Override
	public void overrideCfg()
	{
		this.overrideCxfSpiProvider();
	}

	public void start()
	{
		// mcClient.subscribe(EApp.DBA);
		//
		// try
		// {
		// pool.execute(mcClient);
		// } catch (Exception e)
		// {
		// pool.shutdown();
		// }

	}

	public void stop()
	{
		// mcClient.unsubscribe(EApp.DBA);
		// pool.shutdown(); // Disable new tasks from being submitted
		// try
		// {
		// // Wait a while for existing tasks to terminate
		// if (!pool.awaitTermination(60, TimeUnit.SECONDS))
		// {
		// pool.shutdownNow(); // Cancel currently executing tasks
		// // Wait a while for tasks to respond to being cancelled
		// if (!pool.awaitTermination(60, TimeUnit.SECONDS))
		// logger.error("multicast client did not terminate");
		// }
		// } catch (InterruptedException ie)
		// {
		// // (Re-)Cancel if current thread also interrupted
		// pool.shutdownNow();
		// // Preserve interrupt status
		// Thread.currentThread().interrupt();
		// }
	}

	private void clearAll() 
	{
		all.clear();
		customizedMap.clear();
	}
	
//	public static void loadAll()
//	{
//		UtsTradeConfoSummary.load();
//		
////		loadHistoryTradeConfo();
//		clearAll();
//		loadallTradeConfo();
//		loadallUtsTradeConfoSummary();
//		loadInvoice();
//		loadTodayLog();
//	}
	
	@SuppressWarnings("unused")
	public void create(IMongoDocument doc)
	{
		exec.execute(new Runnable()
		{
			public void run()
			{
				MongoDbAdapter.instance().save(doc); // save will also do update
														// and fill id
				logger.info("create {}", doc.toString());

				String id = doc.getId();
				all.put(id, doc);

				if (doc instanceof ICustomizeMongoDocument)
				{
					String key = ((ICustomizeMongoDocument) doc).getKey();
					customizedMap.put(key, (ICustomizeMongoDocument) doc);
				}
			}
		});
	}
	
	@SuppressWarnings("unused")
	public void update(IMongoDocument doc)
	{
		logger.info("update {}", doc.toString());
	
		exec.execute(new Runnable()
		{
			public void run()
			{		
				IMongoDocument old = null;
				String id = doc.getId();
				if (id != null) {
					old = all.put(id, doc);
				}
				
				if (doc instanceof ICustomizeMongoDocument) 
				{
					String key = ((ICustomizeMongoDocument)doc).getKey();
					if (key != null) {
						old = customizedMap.put(key, (ICustomizeMongoDocument)doc);
						if (old.getId() != null && doc.getId() == null) {
							doc.setId(old.getId());
						}
					}
				}
				MongoDbAdapter.instance().save(doc); // save will also do update and fill id
			}
		});
	}

	@SuppressWarnings("unused")
	public void delete(IMongoDocument doc)
	{
		exec.execute(new Runnable()
		{
			public void run()
			{
				String id = null;
				if (doc instanceof ICustomizeMongoDocument)
				{
					id = ((ICustomizeMongoDocument) doc).getKey();
					if (id != null)
					{
						customizedMap.remove(id);
					}
				}

				id = doc.getId();
				if (id != null)
				{
					all.remove(id);
				}

				MongoDbAdapter.instance().delete(doc); // save will also do
														// update
			}
		});
		logger.info("delete {}", doc.toString());
	}

	@SuppressWarnings("unused")
	public void deleteAll(IMongoDocument doc)
	{
		exec.execute(new Runnable()
		{
			public void run()
			{
				MongoDbAdapter.instance().deleteAll(doc); // save will also do update}
			}
		});
		logger.info("delete all {}", doc.toString());
	}
	
//	@Override
//	public void onAdmin(byte[] data)
//	{
//		logger.debug(data.toString());
//	}
//
//	@Override
//	public byte[] onQuery(byte[] data)
//	{
//		logger.debug("{}", new String(data));
//		return null;
//		
//		// ECommand sender = ECommand.get((char)data[1]);
//		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
//		String msg = null;
//		switch (cmd)
//		{
//		case QUERY_ALL_TRADES:
//		{
//			msg = jsonHistTradeConfo();
//			break;
//		}
//		case QUERY_TRADE_BETWEEN:
//		{
//			String start = new String(data, ICmmfConst.HEADER_SIZE, ICmmfConst.DATE_LENGTH);
//			String end = new String(data, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH, ICmmfConst.DATE_LENGTH);
//			try
//			{
//				Date dStart = cmmfSdf.parse(start);
//				Date dEnd = cmmfSdf.parse(end);
//				msg = jsonHistTradeConfo(dStart, dEnd);
//			} catch (ParseException e)
//			{
//				logger.error("", e);
//			}
//			break;
//		}
//		case QUERY_ALL_INVOICES:
//		{
//			msg = jsonInvoices();
//			break;
//		}
//		case LOG:
//		{
//			msg = jsonLogs();
//			break;
//		}
//		case QUERY_UTS_SUMMARY:
//		{
//			msg = jsonInvoiceRegisters();
//			break;
//		}
//		case CREATE_INVOICE:
//		{
//			try
//			{
//				String temp = new String(data);
//				String param = temp.substring(ICmmfConst.HEADER_SIZE);
//				// key = xxxyyyyz...
//				String currency = param.substring(0, 3);
//				String invMonth = param.substring(3, 7);
//				Date dInvMonth = sdf_MMyy.parse(invMonth);
//				String company = param.substring(7);
//				String key = Invoice.key(company, currency, dInvMonth);
//				
//				
//				Calendar start = Calendar.getInstance();
//				start.setTime(dInvMonth);
//				start.add(Calendar.MONTH, -1);	// trade date month
//				
//				start.set(Calendar.DAY_OF_MONTH, 1);
//				start.set(Calendar.HOUR_OF_DAY, 0);
//				start.set(Calendar.MINUTE, 0);
//				start.set(Calendar.SECOND, 0);
//				start.set(Calendar.MILLISECOND, 0);
//
//				int lastDay = start.getActualMaximum(Calendar.DAY_OF_MONTH);
//				Calendar lastDayCal = Calendar.getInstance();
//				lastDayCal.setTime(start.getTime());
//				lastDayCal.set(Calendar.DAY_OF_MONTH, lastDay);
//				lastDayCal.set(Calendar.HOUR_OF_DAY, 23);
//				lastDayCal.set(Calendar.MINUTE, 59);
//				lastDayCal.set(Calendar.SECOND, 59);
//				lastDayCal.set(Calendar.MILLISECOND, 999);
//				
////				if (customizedMap.containsKey(key)) {
////					msg = buildDWRGResponse(null, key + " already exists");
////				}
////				else 
//				{
//					DbCreateInvoice gen = new DbCreateInvoice();
//					gen.setCompany(company);
//					gen.setCurrency(currency);
//					gen.setDate(dInvMonth);
////					Invoice inv = gen.createInvoice(start.getTime(), lastDayCal.getTime());
//					Invoice inv = gen.createInvoice();
//					
//					if (inv != null)
//						msg = buildDWRGResponse(inv, null);
//				}
//				
//			} catch (Exception e)
//			{
//				logger.error("", e);
//			}
//			break;
//		}
//		}
//		
//		if (msg != null)
//		{
//			// byte[] res = CmmfBuilder.buildMessage(this.me,
//			// EMessageType.RESPONSE, cmd, msg.getBytes());
//
//			return msg.getBytes();
//		}
//
////		return new String("D" + data[0] + "R" + cmd + "fail").getBytes();
//		return new String("{result : fail}").getBytes();
//	}

//	@Override
//	public void onTask(byte[] data)
//	{
//		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
//		String msg = null;
//		String start = null;
//		String end = null;
//		switch (cmd)
//		{
//		case EMAIL_INVOICE:
//			msg = new String(data);
//			start = msg.substring(ICmmfConst.HEADER_SIZE, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH);
//			end = msg.substring(ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH,
//					ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH + ICmmfConst.DATE_LENGTH);
//			String client = msg.substring(ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH + ICmmfConst.DATE_LENGTH);
//			logger.info(start + "," + end + "," + client);
//			try
//			{
//				Date dStart = cmmfSdf.parse(start);
//				Date dEnd = cmmfSdf.parse(end);
//
//				// msg = getTradeConfo(dStart, dEnd);
//			} catch (ParseException e)
//			{
//				logger.error("", e);
//			}
//			break;
//		case UPDATE_CLIENT:
//		{
//			msg = new String(data, ICmmfConst.HEADER_SIZE);
//			JsonObject jsnobject = Json.createObjectBuilder().build().getJsonObject(msg);
//			JsonArray jsonArray = jsnobject.getJsonArray("accounts");
//			for (int i = 0; i < jsonArray.size(); i++)
//			{
//				JsonObject o = jsonArray.getJsonObject(i);
//				System.out.println(o.toString());
//			}
//			// String client = msg.substring(ICmmfConst.HEADER_SIZE +
//			// ICmmfConst.DATE_LENGTH + ICmmfConst.DATE_LENGTH);
//			// logger.info(start +"," + end + "," + client);
//			break;
//		}
//		case QUERY_ALL_TRADES:
//			msg = jsonHistTradeConfo();
//			break;
//		case QUERY_TRADE_BETWEEN:
//		{
//			start = new String(data, ICmmfConst.HEADER_SIZE, ICmmfConst.DATE_LENGTH);
//			end = new String(data, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH, ICmmfConst.DATE_LENGTH);
//			try
//			{
//				Date dStart = cmmfSdf.parse(start);
//				Date dEnd = cmmfSdf.parse(end);
//				msg = jsonHistTradeConfo(dStart, dEnd);
//			} catch (ParseException e)
//			{
//				logger.error("", e);
//			}
//			break;
//		}
//		}
//	}
//
//	@Override
//	public void onResponse(byte[] data)
//	{
//		// TODO Auto-generated method stub
//	}

	public IMongoDocument get(String id) {
//System.out.println(all.size());
		IMongoDocument o = customizedMap.get(id);
		if (o == null) {
			o = all.get(id);
		}
		return o;
	}
	
	@Override
	public void onSink(byte[] data)
	{
	}

//	public static void main(String[] arg)
//	{
////		byte[] b = {87, 68, 84, 85, 123, 34, 105, 100, 34, 58, 34, 53, 56, 50, 51, 101, 53, 100, 56, 51, 50, 55, 99, 54, 102, 56, 50, 51, 50, 51, 54, 102, 99, 49, 99, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 110, 117, 109, 98, 101, 114, 34, 58, 34, 67, 69, 76, 45, 116, 101, 115, 116, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 100, 97, 116, 101, 34, 58, 34, 48, 57, 32, 74, 117, 108, 121, 44, 32, 50, 48, 49, 54, 34, 44, 34, 97, 109, 111, 117, 110, 116, 34, 58, 34, 85, 83, 36, 49, 44, 57, 48, 57, 34, 44, 34, 115, 105, 122, 101, 34, 58, 34, 49, 44, 52, 48, 48, 34, 44, 34, 104, 101, 100, 103, 101, 34, 58, 34, 49, 54, 53, 34, 44, 34, 105, 115, 80, 97, 105, 100, 34, 58, 34, 102, 97, 108, 115, 101, 34, 44, 34, 104, 97, 115, 83, 101, 110, 116, 34, 58, 34, 102, 97, 108, 115, 101, 34, 125};
//		String s = "WDQGHKD1216Vivienne Court Trading Pty Ltd";
//		byte[] b = s.getBytes();
//		DatabaseAdapter dba = new DatabaseAdapter();
//		dba.start();
//		dba.loadAll();
//		dba.onQuery(b);
//		
//		String temp = new String(b).substring(ICmmfConst.HEADER_SIZE);
//		System.out.println(temp);
//		JSONObject jsnobject = new JSONObject(temp);
//		
//		System.out.println(jsnobject);
//	}
//	public static void main1(String[] arg)
//	{
////		byte[] b = {87, 68, 84, 85, 123, 34, 105, 100, 34, 58, 34, 53, 56, 50, 51, 101, 53, 100, 56, 51, 50, 55, 99, 54, 102, 56, 50, 51, 50, 51, 54, 102, 99, 49, 99, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 110, 117, 109, 98, 101, 114, 34, 58, 34, 67, 69, 76, 45, 116, 101, 115, 116, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 100, 97, 116, 101, 34, 58, 34, 48, 57, 32, 74, 117, 108, 121, 44, 32, 50, 48, 49, 54, 34, 44, 34, 97, 109, 111, 117, 110, 116, 34, 58, 34, 85, 83, 36, 49, 44, 57, 48, 57, 34, 44, 34, 115, 105, 122, 101, 34, 58, 34, 49, 44, 52, 48, 48, 34, 44, 34, 104, 101, 100, 103, 101, 34, 58, 34, 49, 54, 53, 34, 44, 34, 105, 115, 80, 97, 105, 100, 34, 58, 34, 102, 97, 108, 115, 101, 34, 44, 34, 104, 97, 115, 83, 101, 110, 116, 34, 58, 34, 102, 97, 108, 115, 101, 34, 125};
//		String s = "WDQGHKD1016UBS AG London Branch";
//		byte[] b = s.getBytes();
//		DatabaseAdapter dba = new DatabaseAdapter();
//		dba.start();
//		dba.loadAll();
//		dba.onQuery(b);
//		
//		String temp = new String(b).substring(ICmmfConst.HEADER_SIZE);
//		System.out.println(temp);
//		JSONObject jsnobject = new JSONObject(temp);
//		
//		System.out.println(jsnobject);
//	}
//	
//	public static void main2(String[] args)
//	{
//		int interval = 10000;
//		DatabaseAdapter dba = new DatabaseAdapter();
//		dba.start();
//		dba.loadAll();
//
////		DatabaseAdapter.updateInvoiceKeytoUpper();
//		
//		for (;;)
//		{
//			try
//			{
//				logger.debug("sleep {}", interval);
//				Thread.sleep(interval);
//			} catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		// dba.stop();
//	}
}
