package com.uts.tradeconfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
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

import com.celera.library.javamail.IMailListener;
import com.celera.library.javamail.IMailService;
import com.celera.library.javamail.MailService;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class UtsTradeConfoApp implements IMailListener, Runnable
{
	// private final String protocol;
	// private final String host;
	// private final String port;
	// private final String userName;
	// private final String password;
	private final String PREFIX_SUBJECT = "Trade Confirmation";
	private final String FROM_SENDER = "eqd@celera-markets.com";
	private final String PREFIX_TRADE_CONFO_FILENAME = "CELERAEQ-";
	private final String TRADE_CONFO_FILE_EXT = ".pdf";

//	private final String SAVE_DIRECTORY = "/home/idbs/workspace/uts/build/UTS_WS_API/data";

	private Date last = null;

	final private IMailService serv;

	private AtomicBoolean isWorking = new AtomicBoolean(false);

	public UtsTradeConfoApp(String protocol, String host, String port, String userName, String password, Date last)
	{
		this.last = last;
		serv = new MailService(protocol, host, port, userName, password);
		serv.setListener(this);
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
				if (isFrom(fromAddresses[0].toString()) && isMatchSubject(message.getSubject()))
				{
					try
					{
						parseAttachment(message);
					}
					catch (IOException e)
					{
						e.printStackTrace();
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
				if (fileName.startsWith(PREFIX_TRADE_CONFO_FILENAME) && fileName.endsWith(TRADE_CONFO_FILE_EXT))
				{
//					fileName = fileName.replaceAll("/", "_");
//					part.saveFile(SAVE_DIRECTORY + File.separator + fileName);
					// parsePdf(SAVE_DIRECTORY + File.separator + fileName);
					parsePdf(part);
				}
			}
		}
	}

	public void parsePdf(MimeBodyPart part) throws IOException, MessagingException
	{
		System.out.println("=====parsePdf==");
		InputStream is = part.getInputStream();
		PdfReader reader = new PdfReader(is);
		for (int page = 1; page <= 1; page++)
		{
			byte[] b = PdfTextExtractor.getTextFromPage(reader, page).getBytes("UTF-8");
			String sPdf = new String(b);
			UtsTradeConfoDetail t = new UtsTradeConfoDetail();
			t.parsePdf(sPdf);
			System.out.println(t.toString());
		}
		System.out.println("=====end==");
	}

	@Override
	public void run()
	{
		boolean isWait = this.isWorking.compareAndSet(false, true);
		if (isWait)
		{
//			System.out.println("=======retrieve email======");
			serv.getAllFromInbox();
		}
	}

	private boolean isMatchSubject(String sub)
	{
		if (sub == null)
			return false;
		if (sub.startsWith(PREFIX_SUBJECT))
			return true;
		return false;
	}

	private boolean isFrom(String from)
	{
		if (FROM_SENDER.equals(from))
			return true;
		return false;
	}

	public static void main(String[] args)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		// Date to = cal.getTime();
		cal.add(Calendar.DATE, -1);
		Date from = cal.getTime();
		// serv.getBetween(from, to);
		Runnable a = new UtsTradeConfoApp("imap", "outlook.office365.com", "993", "Lloyd.Chan@celera-markets.com", "Ja9XuVDj",
				from);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(a, 10, 10, TimeUnit.SECONDS);
		// scheduler.schedule(new Runnable()
		// {
		// public void run()
		// {
		// beeperHandle.cancel(true);
		// }
		// }, 60 * 60, TimeUnit.SECONDS);
	}
}