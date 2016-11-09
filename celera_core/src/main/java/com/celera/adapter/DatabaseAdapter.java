package com.celera.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IOverrideConfig;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.RrServer;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.EMessageType;
import com.celera.message.cmmf.ICmmfConst;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.entity.TradeDetail;
import com.celera.mongo.repo.InvoiceRepo;
import com.celera.mongo.repo.TradeConfoRepo;

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

	private final static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER);
	private final static SimpleDateFormat dbSdf = new SimpleDateFormat(DB_DATE_FORMATTER);
	private final static SimpleDateFormat cmmfSdf = new SimpleDateFormat(ICmmfConst.DATE_FMT);

	// private static Map<String, TradeConfo> map = new
	// ConcurrentHashMap<String, TradeConfo>();
	private static Map<String, ICustomizeMongoDocument> map = new ConcurrentHashMap<String, ICustomizeMongoDocument>();
	// private static Map<String, Invoice> invoices = new
	// ConcurrentHashMap<String, Invoice>();
	private static Map<String, IMongoDocument> docs = new ConcurrentHashMap<String, IMongoDocument>();
	private Date lastModified = null;

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

	public void loadAll()
	{
//		loadHistoryTradeConfo();
		loadallTradeConfo();
		loadInvoice();
	}

	public void loadHistoryTradeConfo()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = repo.findBetween(DB_START_DATE, DB_END_DATE);
		l.forEach(c ->
		{
			Date lastModified = c.getLastModified();
			if (lastModified.after(this.lastModified))
			{
				this.lastModified.setTime(lastModified.getTime());
			}
			map.put(c.getKey(), c);
		});
		logger.info("loadHistory tradeconfo {}", l.size());
	}

	public void loadTradeConfo()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = repo.findAfter(this.lastModified);
		l.forEach(c -> map.put(c.getKey(), c));
		logger.info("load tradeConfo {}", l.size());
	}
	
	public void loadallTradeConfo()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = (Collection<TradeConfo>) repo.findAll();
		l.forEach(c -> map.put(c.getKey(), c));
		logger.info("load tradeConfo {}", l.size());
	}

	public void loadInvoice()
	{
		try {
			InvoiceRepo repo = (InvoiceRepo) MongoDbAdapter.instance().get(InvoiceRepo.class);
			Collection<Invoice> l = (Collection<Invoice>) repo.findAll();
			l.forEach(c -> map.put(c.getKey(), c));
			logger.info("load invoice {}", l.size());
		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}

	@SuppressWarnings("unused")
	// private void save(TradeConfo tradeConfo)
	private void save(ICustomizeMongoDocument doc)
	{
		ICustomizeMongoDocument old = map.put(doc.getKey(), doc);
		if (old != null)
		{
			doc.setId(old.getId());
		}
		MongoDbAdapter.instance().save(doc); // save will also do update
		logger.info("save {}", doc);
	}

	@SuppressWarnings("unused")
	public static void save(IMongoDocument doc)
	{
		String id = doc.getId();
		if (id != null)
		{
			IMongoDocument old = docs.put(id, doc);
			if (old != null)
			{
				doc.setId(old.getId());
			}
		}
		MongoDbAdapter.instance().save(doc); // save will also do update
		logger.info("save {}", doc.toString());
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

	public static void main(String[] args)
	{
		int interval = 10000;
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		dba.loadAll();

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
	public void onAdmin(byte[] data)
	{
		logger.debug(data.toString());
	}

	@Override
	public byte[] onQuery(byte[] data)
	{
		// ECommand sender = ECommand.get((char)data[1]);
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		String msg = null;
		switch (cmd)
		{
		case QUERY_ALL_TRADES:
			msg = jsonHistTradeConfo();
			break;
		case QUERY_TRADE_BETWEEN:
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
		case QUERY_ALL_INVOICES:
			msg = jsonInvoices();
			break;
		}
		
		if (msg != null)
		{
			// byte[] res = CmmfBuilder.buildMessage(this.me,
			// EMessageType.RESPONSE, cmd, msg.getBytes());

			return msg.getBytes();
		}
		logger.debug(data.toString());

		return null;
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
		case QUERY_ALL_TRADES:
			msg = jsonHistTradeConfo();
			break;
		case QUERY_TRADE_BETWEEN:
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
		for (ICustomizeMongoDocument t : map.values())
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
		for (ICustomizeMongoDocument t : map.values())
		{
			if (t instanceof Invoice)
			{
				JsonObject o = ((Invoice) t).json();
				arrayBuilder.add(o);
			}
			// sb.append(o.toString());
		}
		ansBuilder.add("sender", "D");
		ansBuilder.add("receiver", "W");
		ansBuilder.add("message_type", "R");
		ansBuilder.add("command", "I");
		ansBuilder.add("invoices", arrayBuilder);
		return ansBuilder.build().toString();
	}

	public static String jsonHistTradeConfo(Date start, Date end)
	{
		StringBuilder sb = new StringBuilder(0);
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

		for (ICustomizeMongoDocument value : map.values())
		{
			if (value instanceof TradeConfo)
			{
				TradeConfo t = (TradeConfo) value;
				try
				{
					Date tradeDate = dbSdf.parse(t.getTradeDate());
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

		for (ICustomizeMongoDocument value : map.values())
		{
			if (value instanceof TradeConfo)
			{
				TradeConfo t = (TradeConfo) value;
				try
				{
					Date tradeDate = dbSdf.parse(t.getTradeDate());
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
}
