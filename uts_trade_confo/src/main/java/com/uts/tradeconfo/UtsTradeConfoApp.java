package com.uts.tradeconfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.ResourceManager;
import com.celera.library.javamail.IMailListener;
import com.celera.library.javamail.IMailService;
import com.celera.library.javamail.MailService;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.vectails.common.IResourceProperties;

public class UtsTradeConfoApp implements IMailListener
{
	final static Logger logger = LoggerFactory.getLogger(UtsTradeConfoApp.class);

	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_UTF16 = "UTF-16";
	private static final String CHARSET_UTF32 = "UTF-32";
	private static final String CHARSET_ASCII = "US-ASCII";

	private static final String DEFAULT_PREFIX_EMAIL_SUBJECT = "Trade Confirmation";
	private static final String DEFAULT_EMAIL_SENDER = "eqd@celera-markets.com";
	private static final String DEFAULT_PREFIX_ATTACHMENT = "CELERAEQ-";
	private static final String DEFAULT_ATTACHMENT_FILE_EXT = ".pdf";

	private static final String DEFAULT_EMAIL_SERVER_PROTO = "imap";
	private static final String DEFAULT_EMAIL_SERVER_IP = "outlook.office365.com";
	private static final String DEFAULT_EMAIL_SERVER_PORT = "993";
	private static final String DEFAULT_EMAIL_SERVER_USER = "Lloyd.Chan@celera-markets.com";
	private static final String DEFAULT_EMAIL_SERVER_PWD = "Ja9XuVDj";
	
	private static final String DEFAULT_EMAIL_POLL_INTERVAL = "10";

	private static String PREFIX_EMAIL_SUBJECT = null;
	private static String EMAIL_SENDER = null;
	private static String PREFIX_ATTACHMENT = null;
	private static String ATTACHMENT_FILE_EXT = null;

	private static String EMAIL_SERVER_PROTO;
	private static String EMAIL_SERVER_IP;
	private static String EMAIL_SERVER_PORT;
	private static String EMAIL_SERVER_USER;
	private static String EMAIL_SERVER_PWD;
	
	private static Integer EMAIL_POLLING_INTERVAL; 

	static
	{
		PREFIX_EMAIL_SUBJECT = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_SUBJECT_PREFIX,
				DEFAULT_PREFIX_EMAIL_SUBJECT);
		EMAIL_SENDER = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_SENDER, DEFAULT_EMAIL_SENDER);
		PREFIX_ATTACHMENT = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_ATTACHMENT_PREFIX,
				DEFAULT_PREFIX_ATTACHMENT);
		ATTACHMENT_FILE_EXT = ResourceManager.getProperties(IResourceProperties.PROP_UTS_EMAILTC_ATTACHMENT_EXT,
				DEFAULT_ATTACHMENT_FILE_EXT);

		EMAIL_SERVER_PROTO = ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_SERVER_PROTO,
				DEFAULT_EMAIL_SERVER_PROTO);
		EMAIL_SERVER_IP = ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_SERVER_IP,
				DEFAULT_EMAIL_SERVER_IP);
		EMAIL_SERVER_PORT = ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_SERVER_PORT,
				DEFAULT_EMAIL_SERVER_PORT);
		EMAIL_SERVER_USER = ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_SERVER_USER,
				DEFAULT_EMAIL_SERVER_USER);
		EMAIL_SERVER_PWD = ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_SERVER_PWD,
				DEFAULT_EMAIL_SERVER_PWD);
		EMAIL_POLLING_INTERVAL = Integer.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_EMAIL_POLL_INTERVAL,
				DEFAULT_EMAIL_POLL_INTERVAL));
	}

	private Date last = null;
	final private IMailService serv;

	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	final PollEmailTask actualTask = new PollEmailTask();

	class PollEmailTask implements Runnable
	{
		@Override
		public void run()
		{
			serv.getAllFromInbox();
		}
	}
	
	public UtsTradeConfoApp(Date last)
	{
		this.last = last;
		serv = new MailService(EMAIL_SERVER_PROTO, EMAIL_SERVER_IP, EMAIL_SERVER_PORT, EMAIL_SERVER_USER,
				EMAIL_SERVER_PWD);
		serv.setListener(this);
	}

	@Override
	public String toString()
	{
		return "UtsTradeConfoApp [last=" + last + ", serv=" + serv + "]";
	}

	@Override
	public void onEmail(Message message)
	{
		try
		{
			// if (!message.getFlags().contains(Flags.Flag.SEEN))
			// {
			if (message.getSentDate().after(last))
			{
				Address[] fromAddresses = message.getFrom();
				logger.debug(message.getSentDate().toString() + "," + fromAddresses[0].toString() + ","
						+ message.getSubject());
				if (/* isFrom(fromAddresses[0].toString()) && */ isMatchSubject(message.getSubject()))
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
		}
	}

	public void start()
	{
		logger.info("Start poll email every {} seconds", EMAIL_POLLING_INTERVAL);
		
		executorService.scheduleAtFixedRate(new Runnable()
		{
			private final ExecutorService executor = Executors.newSingleThreadExecutor();
			private Future<?> lastExecution;

			@Override
			public void run()
			{
				if (lastExecution != null && !lastExecution.isDone())
				{
					logger.debug("Polling email not done");
					return;
				}
				lastExecution = executor.submit(actualTask);
			}
		}, 0, EMAIL_POLLING_INTERVAL, TimeUnit.SECONDS);
	}

	private boolean isMatchSubject(String sub)
	{
		if (sub == null)
			return false;
		if (sub.contains(PREFIX_EMAIL_SUBJECT))
			return true;
		return false;
	}

	private boolean isFrom(String from)
	{
		if (EMAIL_SENDER.equals(from))
			return true;
		return false;
	}

	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		logger.info("Read BasicConfigurator");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		// Date to = cal.getTime();
		cal.add(Calendar.DATE, -7);
		Date from = cal.getTime();
		// serv.getBetween(from, to);
		UtsTradeConfoApp a = new UtsTradeConfoApp(from);
		logger.info(a.toString());
		a.start();

		for (;;)
		{
			try
			{
				Thread.sleep(100000);
			}
			catch (InterruptedException e)
			{
				// e.printStackTrace();
			}
		}

		// scheduler.schedule(new Runnable()
		// {
		// public void run()
		// {
		// beeperHandle.cancel(true);
		// }
		// }, 60 * 60, TimeUnit.SECONDS);
	}
}