package com.uts.tradeconfo.main;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.adapter.EmailServiceImpl;
import com.uts.tradeconfo.UtsEmailProcessor;

public class Application
{
	final static Logger logger = LoggerFactory.getLogger(Application.class);
	
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
		cal.add(Calendar.DATE, -32);
		Date from = cal.getTime();
		// serv.getBetween(from, to);
		UtsEmailProcessor proc = new UtsEmailProcessor();
		proc.loadTradeConfo();
		proc.poll();
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
