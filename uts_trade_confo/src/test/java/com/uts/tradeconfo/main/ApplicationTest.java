package com.uts.tradeconfo.main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.adapter.EmailServiceImpl;
import com.celera.library.javamail.IMailService;
import com.celera.mongo.entity.IMongoDocument;
import com.uts.tradeconfo.UtsEmailProcessor;
import com.uts.tradeconfo.UtsEmailServiceImpl;

public class ApplicationTest
{
	final static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		logger.info("Read BasicConfigurator");

		UtsEmailProcessor proc = new UtsEmailProcessor();
		proc.loadTradeConfo();
//		proc.poll();
		
		UtsEmailServiceImpl serv = new UtsEmailServiceImpl(proc);
		IMailService mailServ = serv.getEmailService();
		List<IMongoDocument> list = mailServ.getAllFromInbox();
		
//		EmailServiceImpl a = new EmailServiceImpl(proc);
//		logger.info(a.toString());
//		a.start();

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
