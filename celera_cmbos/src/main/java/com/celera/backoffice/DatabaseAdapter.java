package com.celera.backoffice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.celera.core.configure.IOverrideConfig;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.RrServer;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.ICmmfConst;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.BOData;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.InvoiceRegister;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.repo.InvoiceRegisterRepo;
import com.celera.mongo.repo.InvoiceRepo;
import com.celera.mongo.repo.TradeConfoRepo;
import com.uts.tradeconfo.UtsTradeConfoSummary;

public class DatabaseAdapter extends CmmfApp implements IOverrideConfig
{
	final static Logger logger = LoggerFactory.getLogger(DatabaseAdapter.class);

	private static final String DATE_FORMATTER = "yyyy-MM-dd";
	private static final String DB_DATE_FORMATTER = "dd-MMM-yy";

	private static final int IO_THREAD = 1;
	private static final String IP = "127.0.0.1";
	private static final int MC_PORT = 5555;
	private static final int TCP_PORT = 6555;

	// private static final String CXF_SPI_PROVIDER;

	private static Date START_DATE;
	private static Date END_DATE;
	private static String DB_START_DATE;
	private static String DB_END_DATE;

	private final static SimpleDateFormat sdf_MMyy = new SimpleDateFormat("MMyy");
	private final static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER);
	private final static SimpleDateFormat dbSdf = new SimpleDateFormat(DB_DATE_FORMATTER);
	private final static SimpleDateFormat cmmfSdf = new SimpleDateFormat(ICmmfConst.DATE_FMT);

	// private static Map<String, TradeConfo> map = new
	// ConcurrentHashMap<String, TradeConfo>();
	private static Map<String, ICustomizeMongoDocument> customizedMap = new ConcurrentHashMap<String, ICustomizeMongoDocument>();
	// private static Map<String, Invoice> invoices = new
	// ConcurrentHashMap<String, Invoice>();
	private static Map<String, IMongoDocument> all = new ConcurrentHashMap<String, IMongoDocument>();
	private static Date lastModified = null;

	private ILifeCycle serv = null;
	// private IMulticastClient mcClient = null;
	// private ExecutorService pool = Executors.newFixedThreadPool(1);

	static
	{
		try
		{
			START_DATE = sdf.parse(ResourceManager.getProperties(IResourceProperties.PROP_CM_DBA_STARTDATE));
			DB_START_DATE = dbSdf.format(START_DATE);
		} catch (ParseException e)
		{
			logger.error("{} invalid patter", IResourceProperties.PROP_CM_DBA_STARTDATE, e);
			System.exit(-1);
		}
		try
		{
			END_DATE = sdf.parse(ResourceManager.getProperties(IResourceProperties.PROP_CM_DBA_ENDDATE));
			DB_END_DATE = dbSdf.format(END_DATE);
		} catch (ParseException e)
		{
			logger.error("{} invalid patter", IResourceProperties.PROP_CM_DBA_ENDDATE, e);
			System.exit(-1);
		}
		logger.debug("{},{}", DB_START_DATE, DB_END_DATE);
	}

	public DatabaseAdapter()
	{
		super(EApp.DBA);

		overrideCfg();
		lastModified = START_DATE;
		serv = new RrServer(IO_THREAD, IP, TCP_PORT, this);
		serv.init();
		// mcClient = new MulticastClient(IO_THREAD, IP, MC_PORT, this);
		// mcClient.setTcpListener(this);
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

		serv.start();
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

		serv.stop();
	}

	public static void clearAll() 
	{
		all.clear();
		customizedMap.clear();
	}
	
	public static void loadAll()
	{
		UtsTradeConfoSummary.load();
		
//		loadHistoryTradeConfo();
		clearAll();
		loadallTradeConfo();
		loadInvoice();
		loadallUtsTradeConfoSummary();
	}
	
	public static void loadHistoryTradeConfo()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l;
		try
		{
			l = repo.findBetween(sdf.parse(DB_START_DATE), sdf.parse(DB_END_DATE));
			l.forEach(c ->
			{
				Date lastModified = c.getLastModified();
				if (lastModified.after(DatabaseAdapter.lastModified))
				{
					DatabaseAdapter.lastModified.setTime(lastModified.getTime());
				}
				customizedMap.put(c.getKey(), c);
				all.put(c.getId(), c);
			});
			logger.info("loadHistory tradeconfo {}", l.size());
		} catch (ParseException e)
		{
			logger.error("{}", e);
		}

	}

	public static  void loadTradeConfo()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = repo.findAfter(DatabaseAdapter.lastModified);
		for (TradeConfo c : l) {
			customizedMap.put(c.getKey(), c);
			all.put(c.getId(), c);
		};
		logger.info("load tradeConfo {}", l.size());
	}
	
	public static void loadallTradeConfo()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = (Collection<TradeConfo>) repo.findAll();
		l.forEach(c -> {
			customizedMap.put(c.getKey(), c);
			all.put(c.getId(), c);
		});
		logger.info("load tradeConfo {}", l.size());
	}
	
	public static void loadallUtsTradeConfoSummary()
	{
		InvoiceRegisterRepo repo = (InvoiceRegisterRepo) MongoDbAdapter.instance().get(InvoiceRegisterRepo.class);
		Collection<InvoiceRegister> l = (Collection<InvoiceRegister>) repo.findAll();
		l.forEach(c -> {
			customizedMap.put(c.getKey(), c);
			all.put(c.getId(), c);
		});
		logger.info("load uts tradeconfo summary {}", l.size());
	}

	public static void loadInvoice()
	{
		try {
			InvoiceRepo repo = (InvoiceRepo) MongoDbAdapter.instance().get(InvoiceRepo.class);
			Collection<Invoice> l = (Collection<Invoice>) repo.findAll();
			l.forEach(c -> {
				try {
					customizedMap.put(c.getKey(), c);
					all.put(c.getId(), c);
					logger.info("load key[{}] id[{}] [{}] ", c.getKey(), c.getId(), c);
				}
				catch (NullPointerException e) {
					logger.error("{}", c, e);
				}
			});
			logger.info("load invoice {}, {}, {}", l.size(), all.size(), customizedMap.size());
		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}
	
//	public void saveInvoice(Invoice update)
//	{
//		try {
//			MongoDbAdapter.instance().save(update);
//			
////			ICustomizeMongoDocument o = map.get(update.getId());
//			InvoiceRepo repo = (InvoiceRepo) MongoDbAdapter.instance().get(InvoiceRepo.class);
//			Collection<Invoice> l = (Collection<Invoice>) repo.save(update);
//			
//			logger.info("update invoice db {}", update);
//		}
//		catch (Exception e)
//		{
//			logger.error("", e);
//		}
//	}

	@SuppressWarnings("unused")
	public static void create(IMongoDocument doc)
	{
		MongoDbAdapter.instance().save(doc); // save will also do update and fill id
		logger.info("create {}", doc.toString());
		
		String id = doc.getId();
		all.put(id, doc);
		
		if (doc instanceof ICustomizeMongoDocument) 
		{
			String key = ((ICustomizeMongoDocument)doc).getKey();
			customizedMap.put(key, (ICustomizeMongoDocument)doc);
		}
	}
	
	@SuppressWarnings("unused")
	public static void update(IMongoDocument doc)
	{
		logger.info("update {}", doc.toString());
		
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

	@SuppressWarnings("unused")
	public static void delete(IMongoDocument doc)
	{
		String id = null;
		if (doc instanceof ICustomizeMongoDocument) {
			id = ((ICustomizeMongoDocument)doc).getKey();
			if (id != null) {
				customizedMap.remove(id);
			}
		}

		id = doc.getId();
		if (id != null) {
			all.remove(id);
		}

		MongoDbAdapter.instance().delete(doc); // save will also do update
		logger.info("delete {}", doc.toString());
	}

	public static void deleteAll(IMongoDocument doc)
	{
		MongoDbAdapter.instance().deleteAll(doc); // save will also do update
		logger.info("delete all {}", doc.toString());
	}
	
	// @Override
	// public byte[] onMessage(byte[] data)
	// {
	// EMessageType type = EMessageType.get((char)
	// data[ICmmfConst.HEADER_MESSAGE_TYPE_POS]);
	// byte[] b = null;
	//
	// switch (type)
	// {
	// case ADMIN:
	// onAdmin(data);
	// break;
	// case QUERY:
	// b = onQuery(data);
	// break;
	// case TASK:
	// onTask(data);
	// break;
	// }
	// return b;
	// }

	@Override
	public void onAdmin(byte[] data)
	{
		logger.debug(data.toString());
	}

	@Override
	public byte[] onQuery(byte[] data)
	{
		logger.debug("{}", new String(data));
		
		// ECommand sender = ECommand.get((char)data[1]);
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		String msg = null;
		switch (cmd)
		{
		case QUERY_ALL_TRADES:
		{
			msg = jsonHistTradeConfo();
			break;
		}
		case QUERY_TRADE_BETWEEN:
		{
			String start = new String(data, ICmmfConst.HEADER_SIZE, ICmmfConst.DATE_LENGTH);
			String end = new String(data, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH, ICmmfConst.DATE_LENGTH);
			try
			{
				Date dStart = cmmfSdf.parse(start);
				Date dEnd = cmmfSdf.parse(end);
				msg = jsonHistTradeConfo(dStart, dEnd);
			} catch (ParseException e)
			{
				logger.error("", e);
			}
			break;
		}
		case QUERY_ALL_INVOICES:
		{
			msg = jsonInvoices();
			break;
		}
		case QUERY_UTS_SUMMARY:
		{
			msg = jsonInvoiceRegisters();
			break;
		}
		case CREATE_INVOICE:
		{
			try
			{
				String temp = new String(data);
				String param = temp.substring(ICmmfConst.HEADER_SIZE);
				// key = xxxyyyyz...
				String currency = param.substring(0, 3);
				String invMonth = param.substring(3, 7);
				Date dInvMonth = sdf_MMyy.parse(invMonth);
				String company = param.substring(7);
				String key = Invoice.key(company, currency, dInvMonth);
				
				
				Calendar start = Calendar.getInstance();
				start.setTime(dInvMonth);
				start.add(Calendar.MONTH, -1);	// trade date month
				
				start.set(Calendar.DAY_OF_MONTH, 1);
				start.set(Calendar.HOUR_OF_DAY, 0);
				start.set(Calendar.MINUTE, 0);
				start.set(Calendar.SECOND, 0);
				start.set(Calendar.MILLISECOND, 0);

				int lastDay = start.getActualMaximum(Calendar.DAY_OF_MONTH);
				Calendar lastDayCal = Calendar.getInstance();
				lastDayCal.setTime(start.getTime());
				lastDayCal.set(Calendar.DAY_OF_MONTH, lastDay);
				lastDayCal.set(Calendar.HOUR_OF_DAY, 23);
				lastDayCal.set(Calendar.MINUTE, 59);
				lastDayCal.set(Calendar.SECOND, 59);
				lastDayCal.set(Calendar.MILLISECOND, 999);
				
//				if (customizedMap.containsKey(key)) {
//					msg = buildDWRGResponse(null, key + " already exists");
//				}
//				else 
				{
					DbCreateInvoice gen = new DbCreateInvoice();
					gen.setCompany(company);
					gen.setCurrency(currency);
					gen.setDate(dInvMonth);
					Invoice inv = gen.createInvoice(start.getTime(), lastDayCal.getTime());
					
					if (inv != null)
						msg = buildDWRGResponse(inv, null);
				}
				
			} catch (Exception e)
			{
				logger.error("", e);
			}
			break;
		}
		}
		
		if (msg != null)
		{
			// byte[] res = CmmfBuilder.buildMessage(this.me,
			// EMessageType.RESPONSE, cmd, msg.getBytes());

			return msg.getBytes();
		}

//		return new String("D" + data[0] + "R" + cmd + "fail").getBytes();
		return new String("{result : fail}").getBytes();
	}

	@Override
	public void onTask(byte[] data)
	{
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		String msg = null;
		String start = null;
		String end = null;
		switch (cmd)
		{
		case EMAIL_INVOICE:
			msg = new String(data);
			start = msg.substring(ICmmfConst.HEADER_SIZE, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH);
			end = msg.substring(ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH,
					ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH + ICmmfConst.DATE_LENGTH);
			String client = msg.substring(ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH + ICmmfConst.DATE_LENGTH);
			logger.info(start + "," + end + "," + client);
			try
			{
				Date dStart = cmmfSdf.parse(start);
				Date dEnd = cmmfSdf.parse(end);

				// msg = getTradeConfo(dStart, dEnd);
			} catch (ParseException e)
			{
				logger.error("", e);
			}
			break;
		case UPDATE_CLIENT:
		{
			msg = new String(data, ICmmfConst.HEADER_SIZE);
			JsonObject jsnobject = Json.createObjectBuilder().build().getJsonObject(msg);
			JsonArray jsonArray = jsnobject.getJsonArray("accounts");
			for (int i = 0; i < jsonArray.size(); i++)
			{
				JsonObject o = jsonArray.getJsonObject(i);
				System.out.println(o.toString());
			}
			// String client = msg.substring(ICmmfConst.HEADER_SIZE +
			// ICmmfConst.DATE_LENGTH + ICmmfConst.DATE_LENGTH);
			// logger.info(start +"," + end + "," + client);
			break;
		}
		case QUERY_ALL_TRADES:
			msg = jsonHistTradeConfo();
			break;
		case QUERY_TRADE_BETWEEN:
		{
			start = new String(data, ICmmfConst.HEADER_SIZE, ICmmfConst.DATE_LENGTH);
			end = new String(data, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH, ICmmfConst.DATE_LENGTH);
			try
			{
				Date dStart = cmmfSdf.parse(start);
				Date dEnd = cmmfSdf.parse(end);
				msg = jsonHistTradeConfo(dStart, dEnd);
			} catch (ParseException e)
			{
				logger.error("", e);
			}
			break;
		}
		}
		
	}

	@Override
	public void onResponse(byte[] data)
	{
		// TODO Auto-generated method stub
	}

	public void emailInvoice()
	{

	}

	public static String jsonHistTradeConfo()
	{
		JsonObjectBuilder ansBuilder = Json.createObjectBuilder();
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		// StringBuilder sb = new StringBuilder(0);
		for (ICustomizeMongoDocument t : customizedMap.values())
		{
			if (t instanceof TradeConfo)
			{
				JsonObject o = ((TradeConfo) t).json();
				arrayBuilder.add(o);
			}
			// sb.append(o.toString());
		}
		ansBuilder.add("sender", "D");
		ansBuilder.add("receiver", "W");
		ansBuilder.add("message_type", "R");
		ansBuilder.add("command", "A");
		ansBuilder.add("tradeconf", arrayBuilder);
		return ansBuilder.build().toString();
		// return sb.toString();
	}
	
	public static String jsonInvoices()
	{
		JsonObjectBuilder ansBuilder = Json.createObjectBuilder();
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		// StringBuilder sb = new StringBuilder(0);
		int count = 0;
		for (IMongoDocument t : customizedMap.values())
		{
			if (t instanceof Invoice)
			{
				JsonObject o = ((Invoice) t).json();
				arrayBuilder.add(o);
				count++;
			}
			// sb.append(o.toString());
		}
		logger.debug("======count[{}] cusMap.size[{}]", count, customizedMap.size());
		ansBuilder.add("sender", "D");
		ansBuilder.add("receiver", "W");
		ansBuilder.add("message_type", "R");
		ansBuilder.add("command", "I");
		ansBuilder.add("invoices", arrayBuilder);
		return ansBuilder.build().toString();
	}
	
	public static String jsonInvoiceRegisters()
	{
		JsonObjectBuilder ansBuilder = Json.createObjectBuilder();
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		// StringBuilder sb = new StringBuilder(0);
		for (IMongoDocument t : customizedMap.values())
		{
			if (t instanceof InvoiceRegister)
			{
				JsonObject o = ((InvoiceRegister) t).json();
				arrayBuilder.add(o);
			}
			// sb.append(o.toString());
		}
		ansBuilder.add("sender", "D");
		ansBuilder.add("receiver", "W");
		ansBuilder.add("message_type", "R");
		ansBuilder.add("command", "S");
		ansBuilder.add("summary", arrayBuilder);
		return ansBuilder.build().toString();
	}

	public static String buildDWRGResponse(Invoice inv, String reason)
	{
		JsonObjectBuilder ansBuilder = Json.createObjectBuilder();
		ansBuilder.add("sender", "D");
		ansBuilder.add("receiver", "W");
		ansBuilder.add("message_type", "R");
		ansBuilder.add("command", "G");
		if (inv != null)
			ansBuilder.add("invoice", inv.json());
		else 
			ansBuilder.add("invoice", reason);
		return ansBuilder.build().toString();
	}
	
	public static String jsonHistTradeConfo(Date start, Date end)
	{
		StringBuilder sb = new StringBuilder(0);
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

		for (ICustomizeMongoDocument value : customizedMap.values())
		{
			if (value instanceof TradeConfo)
			{
				TradeConfo t = (TradeConfo) value;
				try
				{
//					Date tradeDate = dbSdf.parse(t.getTradeDate());
					Date tradeDate = t.getTradeDate();
					if (tradeDate.after(start) && tradeDate.before(end))
					{
						JsonObject o = t.json();
						arrayBuilder.add(o);
					}
				} catch (Exception e)
				{
					logger.error("", e);
				}
			}
		}
		return sb.toString();
	}

	public static List getHistTradeConfo(Date start, Date end)
	{
		List<TradeConfo> l = new ArrayList<TradeConfo>();

		for (ICustomizeMongoDocument value : customizedMap.values())
		{
			if (value instanceof TradeConfo)
			{
				TradeConfo t = (TradeConfo) value;
				try
				{
//					Date tradeDate = dbSdf.parse(t.getTradeDate());
					Date tradeDate = t.getTradeDate();
					if (tradeDate.after(start) && tradeDate.before(end))
					{
						l.add(t);
					}
				} catch (Exception e)
				{
					logger.error("", e);
				}
			}
		}
		return l;
	}

	public static List<Invoice> getAllInvoice()
	{
		List<Invoice> l = new ArrayList<Invoice>();
		
		for (ICustomizeMongoDocument value : customizedMap.values())
		{
			if (value instanceof Invoice)
			{
				Invoice t = (Invoice) value;
				l.add(t);
			}
		}
		return l;
	}
	
	public static String getMaxInvoiceNumber()
	{
		InvoiceRepo repo = (InvoiceRepo) MongoDbAdapter.instance().get(InvoiceRepo.class);
		PageRequest request = new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "invoice_number"));
		Invoice l = repo.findInvoiceNumberNotEmpty(request).getContent().get(0);
		return l.getInvoice_number();
	}
	
	public static IMongoDocument get(String id) {
System.out.println(all.size());
		IMongoDocument o = customizedMap.get(id);
		if (o == null) {
			o = all.get(id);
		}
		return o;
	}
	
//	public static void updateInvoiceKeytoUpper() {
//		for (IMongoDocument o : customizedMap.values()) {
//			if (o instanceof Invoice) {
//				((Invoice) o).setKey(((Invoice) o).getKey());
//				MongoDbAdapter.instance().save(o); // save will also do update
//				logger.info("update invoce key {}", o.toString());
//			}
//		}
//	}
	
	public static void main(String[] arg)
	{
//		byte[] b = {87, 68, 84, 85, 123, 34, 105, 100, 34, 58, 34, 53, 56, 50, 51, 101, 53, 100, 56, 51, 50, 55, 99, 54, 102, 56, 50, 51, 50, 51, 54, 102, 99, 49, 99, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 110, 117, 109, 98, 101, 114, 34, 58, 34, 67, 69, 76, 45, 116, 101, 115, 116, 34, 44, 34, 105, 110, 118, 111, 105, 99, 101, 95, 100, 97, 116, 101, 34, 58, 34, 48, 57, 32, 74, 117, 108, 121, 44, 32, 50, 48, 49, 54, 34, 44, 34, 97, 109, 111, 117, 110, 116, 34, 58, 34, 85, 83, 36, 49, 44, 57, 48, 57, 34, 44, 34, 115, 105, 122, 101, 34, 58, 34, 49, 44, 52, 48, 48, 34, 44, 34, 104, 101, 100, 103, 101, 34, 58, 34, 49, 54, 53, 34, 44, 34, 105, 115, 80, 97, 105, 100, 34, 58, 34, 102, 97, 108, 115, 101, 34, 44, 34, 104, 97, 115, 83, 101, 110, 116, 34, 58, 34, 102, 97, 108, 115, 101, 34, 125};
		String s = "WDQGHKD1016UBS AG London Branch";
		byte[] b = s.getBytes();
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		dba.loadAll();
		dba.onQuery(b);
		
		String temp = new String(b).substring(ICmmfConst.HEADER_SIZE);
		System.out.println(temp);
		JSONObject jsnobject = new JSONObject(temp);
		
		System.out.println(jsnobject);
	}
	
	public static void main2(String[] args)
	{
		int interval = 10000;
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		dba.loadAll();

//		DatabaseAdapter.updateInvoiceKeytoUpper();
		
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

	@Override
	public void onSink(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

}
