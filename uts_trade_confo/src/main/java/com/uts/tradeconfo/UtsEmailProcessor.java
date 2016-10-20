package com.uts.tradeconfo;

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

public class UtsEmailProcessor implements IMailListener, IOverrideConfig
{
	final static Logger logger = LoggerFactory.getLogger(UtsEmailProcessor.class);
	
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_UTF16 = "UTF-16";
	private static final String CHARSET_UTF32 = "UTF-32";
	private static final String CHARSET_ASCII = "US-ASCII";
	
	private static final String DATE_FORMATTER = "yyyy-MM-dd";
	private static final String DB_DATE_FORMATTER = "dd-MMM-yy";
	
	private static final String DEFAULT_PREFIX_EMAIL_SUBJECT = "Trade Confirmation";
	private static final String DEFAULT_EMAIL_SENDER = "eqd@celera-markets.com";
	private static final String DEFAULT_PREFIX_ATTACHMENT = "CELERAEQ-";
	private static final String DEFAULT_ATTACHMENT_FILE_EXT = ".pdf";
	
//	private static final String CXF_SPI_PROVIDER;
	
	private static Date EMAIL_START_DATE; 
	private static Date EMAIL_END_DATE; 
	private static String DB_START_DATE; 
	private static String DB_END_DATE; 
	
	private static String PREFIX_EMAIL_SUBJECT = null;
	private static String EMAIL_SENDER = null;
	private static String PREFIX_ATTACHMENT = null;
	private static String ATTACHMENT_FILE_EXT = null;
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER);
	private final static SimpleDateFormat dbSdf = new SimpleDateFormat(DB_DATE_FORMATTER);
	
	private Date last = new Date();
	
	private EmailServiceImpl service;
	private Map<String, TradeConfo> map = new HashMap<String, TradeConfo>();
	
	static 
	{
		PREFIX_EMAIL_SUBJECT = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_SUBJECT_PREFIX,
				DEFAULT_PREFIX_EMAIL_SUBJECT);
		EMAIL_SENDER = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_SENDER, DEFAULT_EMAIL_SENDER);
		PREFIX_ATTACHMENT = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_ATTACHMENT_PREFIX,
				DEFAULT_PREFIX_ATTACHMENT);
		ATTACHMENT_FILE_EXT = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_ATTACHMENT_EXT,
				DEFAULT_ATTACHMENT_FILE_EXT);

//		CXF_SPI_PROVIDER = ResourceManager.getProperties(IResourceProperties.PROP_CXF_SPI_PROVIDER);
//		if (CXF_SPI_PROVIDER != null)
//		{
//			String oldSpi = System.getProperty("javax.xml.ws.spi.Provider");
//			System.setProperty("javax.xml.ws.spi.Provider", CXF_SPI_PROVIDER);
//			logger.info("override javax.xml.ws.spi.Provider old[{}] new[{}]", oldSpi, CXF_SPI_PROVIDER);
//		}
		
		try
		{
			EMAIL_START_DATE = sdf.parse(ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_FILTER_STARTDATE));
			DB_START_DATE = dbSdf.format(EMAIL_START_DATE);
		} catch (ParseException e)
		{
			logger.error("{} invalid patter", IResourceProperties.PROP_EMAIL_FILTER_STARTDATE, e);
			System.exit(-1);
		}
		try
		{
			EMAIL_END_DATE = sdf.parse(ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_FILTER_ENDDATE));
			DB_END_DATE = dbSdf.format(EMAIL_END_DATE);
		} catch (ParseException e)
		{
			logger.error("{} invalid patter", IResourceProperties.PROP_EMAIL_FILTER_ENDDATE, e);
			System.exit(-1);
		}
		logger.debug("{},{},{},{},{},{}", PREFIX_EMAIL_SUBJECT, EMAIL_SENDER, PREFIX_ATTACHMENT, ATTACHMENT_FILE_EXT,
				EMAIL_START_DATE, EMAIL_END_DATE);
	}
	
	public UtsEmailProcessor()
	{
		last = EMAIL_START_DATE;
		overrideCfg();
		service = new EmailServiceImpl(this);
//		logger.info(a.toString());
	}
	
	@Override
	public void overrideCfg()
	{
		this.overrideCxfSpiProvider();
	}
	
	@Override
	public void onEmail(Message message)
	{
		try
		{
			// if (!message.getFlags().contains(Flags.Flag.SEEN))
			// {
			if (message.getSentDate().after(last) && message.getSentDate().before(EMAIL_END_DATE))
			{
				Address[] fromAddresses = message.getFrom();
				logger.debug(message.getSentDate().toString() + "," + fromAddresses[0].toString() + ","
						+ message.getSubject());
				if (/* isFrom(fromAddresses[0].toString()) && */ isSubjectMatched(message.getSubject()))
				{
					try
					{
						parseAttachment(message);
					}
					catch (IOException e)
					{
						logger.error("", e);
					}
				}
				last.setTime(message.getSentDate().getTime());
			}
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
	}

	public void loadTradeConfo() 
	{
		TradeConfoRepo repo = (TradeConfoRepo) MongoDbAdapter.instance().get(TradeConfoRepo.class);
		Collection<TradeConfo> l = repo.findBetween(DB_START_DATE, DB_END_DATE);
		l.forEach(c -> map.put(((TradeConfo)c).key(), c));
		logger.info("load {} tradeConfo", l.size());
	}
	
	public void poll()
	{
		service.start();
	}
	
	public void parseAttachment(Message message) throws IOException, MessagingException
	{
		Multipart multipart = (Multipart) message.getContent();
		int numberOfParts = multipart.getCount();

		for (int partCount = 0; partCount < numberOfParts; partCount++)
		{
			MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
			{
				// this part is attachment
				String fileName = part.getFileName();
				if (fileName.startsWith(PREFIX_ATTACHMENT) && fileName.endsWith(ATTACHMENT_FILE_EXT))
				{
					// fileName = fileName.replaceAll("/", "_");
					// part.saveFile(SAVE_DIRECTORY + File.separator +
					// fileName);
					// parsePdf(SAVE_DIRECTORY + File.separator + fileName);
					parsePdf(part);
				}
			}
		}
	}

	public void parsePdf(MimeBodyPart part) throws IOException, MessagingException
	{
		InputStream is = part.getInputStream();
		PdfReader reader = new PdfReader(is);

		for (int page = 1; page <= 1; page++)
		{
			byte[] b = PdfTextExtractor.getTextFromPage(reader, page, new MyTextExtractionStrategy())
					.getBytes(CHARSET_UTF8);
			// byte[] b1 = reader.getPageContent(0);
			// for (int i =0; i<b.length; i++) {
			// System.out.print(b[i] + ",");
			// }

			String sPdf = new String(b);
			// System.out.println(sPdf);
			UtsTradeConfoDetail t = new UtsTradeConfoDetail();
			t.parsePdf(sPdf);
			logger.info(t.toString());
			
			writeDb(t);
		}
	}
	
	private boolean isSubjectMatched(String sub)
	{
		if (sub == null)
			return false;
		if (sub.contains(PREFIX_EMAIL_SUBJECT))
			return true;
		return false;
	}
	
	private void writeDb(UtsTradeConfoDetail detail) 
	{
		TradeConfo tradeConfo = detail.convert();
		TradeConfo old = map.put(tradeConfo.key(), tradeConfo);
		if (old != null) {
			tradeConfo.setId(old.getId());
		}
		MongoDbAdapter.instance().save(tradeConfo);	// save will also do update
	}
}