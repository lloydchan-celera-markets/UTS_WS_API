package com.celera.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import com.celera.adapter.EmailServiceImpl;
import com.celera.core.configure.IOverrideConfig;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.IMulticastClient;
import com.celera.ipc.ITcpServer;
import com.celera.ipc.ITcpServerListener;
import com.celera.ipc.MulticastClient;
import com.celera.ipc.RrServer;
import com.celera.library.javamail.IMailListener;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.CmmfBuilder;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.EMessageType;
import com.celera.message.cmmf.ICmmfConst;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.repo.TradeConfoRepo;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class DatabaseAdapter extends CmmfApp implements IOverrideConfig, ITcpServerListener
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
	private final static SimpleDateFormat cmmfSdf = new SimpleDateFormat(ICmmfConst.CMMF_QUERY_DATE_FMT);

	private Map<String, TradeConfo> map = new HashMap<String, TradeConfo>();
	private Date lastModified = null;

	private ILifeCycle serv = null;
//	private IMulticastClient mcClient = null;
//	private ExecutorService pool = Executors.newFixedThreadPool(1);

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
//		mcClient = new MulticastClient(IO_THREAD, IP, MC_PORT, this);
//		mcClient.setTcpListener(this);
	}

	@Override
	public void overrideCfg()
	{
		this.overrideCxfSpiProvider();
	}

	public void start()
	{
//		mcClient.subscribe(EApp.DBA);
//
//		try
//		{
//			pool.execute(mcClient);
//		} catch (Exception e)
//		{
//			pool.shutdown();
//		}
		
		serv.start();
	}

	public void stop()
	{
//		mcClient.unsubscribe(EApp.DBA);
//		pool.shutdown(); // Disable new tasks from being submitted
//		try
//		{
//			// Wait a while for existing tasks to terminate
//			if (!pool.awaitTermination(60, TimeUnit.SECONDS))
//			{
//				pool.shutdownNow(); // Cancel currently executing tasks
//				// Wait a while for tasks to respond to being cancelled
//				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
//					logger.error("multicast client did not terminate");
//			}
//		} catch (InterruptedException ie)
//		{
//			// (Re-)Cancel if current thread also interrupted
//			pool.shutdownNow();
//			// Preserve interrupt status
//			Thread.currentThread().interrupt();
//		}
		
		serv.stop();
	}

	public void loadHistory()
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
			map.put(c.key(), c);
		});
		logger.info("loadHistory tradeconfo {}", l.size());
	}

	public void load()
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = repo.findAfter(this.lastModified);
		l.forEach(c -> map.put(c.key(), c));
		logger.info("load tradeConfo {}", l.size());
	}

	@SuppressWarnings("unused")
	private void save(TradeConfo tradeConfo)
	{
		TradeConfo old = map.put(tradeConfo.key(), tradeConfo);
		if (old != null)
		{
			tradeConfo.setId(old.getId());
		}
		MongoDbAdapter.instance().save(tradeConfo); // save will also do update
		logger.info("save {}", tradeConfo);
	}

	@Override
	public byte[] onMessage(byte[] data)
	{
		EMessageType type = EMessageType.get((char) data[1]);
		byte[] b = null;
		
		switch (type)
		{
		case ADMIN:
			onAdmin(data);
			break;
		case QUERY:
			b = onQuery(data);
			break;
		}
		return b;
	}

	public static void main(String[] args)
	{
		int interval = 10000;
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		dba.loadHistory();

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
// 		ECommand sender = ECommand.get((char)data[1]);
		ECommand cmd = ECommand.get((char) data[2]);
		String msg = null;
		switch (cmd)
		{
		case QUERY_ALL_TRADES:
			msg = getTradeConfo();
			break;
		case QUERY_TRADE_BETWEEN:
			String start = new String(data, 3, 8);
			String end = new String(data, 11, 8);
			try
			{
				Date dStart = cmmfSdf.parse(start);
				Date dEnd = cmmfSdf.parse(end);
				msg = getTradeConfo(dStart, dEnd);
			} catch (ParseException e)
			{
				logger.error("", e);
			}
			break;
		}
		if (msg != null)
		{
			byte[] res = CmmfBuilder.buildMessage(this.me, EMessageType.RESPONSE, cmd, msg.getBytes());
			return res;
		}
		logger.debug(data.toString());

		return null;
	}

	@Override
	public void onResponse(byte[] data)
	{
		// TODO Auto-generated method stub
	}

	public String getTradeConfo()
	{
		StringBuilder sb = new StringBuilder(0);
		for (TradeConfo t : map.values())
		{
			sb.append(t.toString());
		}
		return sb.toString();
	}

	public String getTradeConfo(Date start, Date end)
	{
		StringBuilder sb = new StringBuilder(0);
		for (TradeConfo t : map.values())
		{
			try
			{
				Date tradeDate = dbSdf.parse(t.getTradeDate());
				if (tradeDate.after(start) && tradeDate.before(end))
					sb.append(t.toString());
			} catch (Exception e)
			{
				logger.error("", e);
			}
		}
		return sb.toString();
	}

}