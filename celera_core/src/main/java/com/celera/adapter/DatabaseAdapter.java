package com.celera.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.celera.library.javamail.IMailListener;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.repo.TradeConfoRepo;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class DatabaseAdapter implements IOverrideConfig
{
	final static Logger logger = LoggerFactory.getLogger(DatabaseAdapter.class);
	
	private static final String DATE_FORMATTER = "yyyy-MM-dd";
	private static final String DB_DATE_FORMATTER = "dd-MMM-yy";
	
//	private static final String CXF_SPI_PROVIDER;
	
	private static Date START_DATE; 
	private static Date END_DATE; 
	private static String DB_START_DATE; 
	private static String DB_END_DATE; 
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER);
	private final static SimpleDateFormat dbSdf = new SimpleDateFormat(DB_DATE_FORMATTER);
	
	private Map<String, TradeConfo> map = new HashMap<String, TradeConfo>();
	
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
		overrideCfg();
	}
	
	@Override
	public void overrideCfg()
	{
		this.overrideCxfSpiProvider();
	}
	
	public void loadTradeConfo() 
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = repo.findBetween(DB_START_DATE, DB_END_DATE);
		l.forEach(c -> map.put(((TradeConfo)c).key(), c));
		logger.info("load {} tradeConfo", l.size());
	}
	
	@SuppressWarnings("unused")
	private void save(TradeConfo tradeConfo) 
	{
		TradeConfo old = map.put(tradeConfo.key(), tradeConfo);
		if (old != null) {
			tradeConfo.setId(old.getId());
		}
		MongoDbAdapter.instance().save(tradeConfo);	// save will also do update
		logger.info("save {}", tradeConfo);
	}
	
	public static void main(String[] args)
	{
		int interval = 10000;
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.loadTradeConfo();
		
		for(;;) 
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
	}
}
