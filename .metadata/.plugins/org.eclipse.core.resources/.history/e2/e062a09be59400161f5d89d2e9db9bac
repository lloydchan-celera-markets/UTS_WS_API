package com.celera.adapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.library.javamail.IMailListener;
import com.celera.library.javamail.IMailService;
import com.celera.library.javamail.MailService;

public class EmailServiceImpl
{
	final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static final String DEFAULT_EMAIL_SERVER_PROTO = "imap";
	private static final String DEFAULT_EMAIL_SERVER_IP = "outlook.office365.com";
	private static final String DEFAULT_EMAIL_SERVER_PORT = "993";
	private static final String DEFAULT_EMAIL_SERVER_USER = "Lloyd.Chan@celera-markets.com";
	private static final String DEFAULT_EMAIL_SERVER_PWD = "Ja9XuVDj";
	
	private static final String DEFAULT_EMAIL_POLL_INTERVAL = "10";

	private static String EMAIL_SERVER_PROTO;
	private static String EMAIL_SERVER_IP;
	private static String EMAIL_SERVER_PORT;
	private static String EMAIL_SERVER_USER;
	private static String EMAIL_SERVER_PWD;
	
	private static Integer EMAIL_POLLING_INTERVAL; 

	static
	{
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

	final private IMailService serv;

	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	final PollEmailTask actualTask = new PollEmailTask();
	final AtomicBoolean isPolling = new AtomicBoolean(false);
	
	class PollEmailTask implements Runnable
	{
		@Override
		public void run()
		{
//			boolean res = isPolling.compareAndSet(false, true);
//			if (isPolling.compareAndSet(false, true)) 
//			{
				serv.getAllFromInbox();
//				isPolling.compareAndSet(true, false);
//			}
//			else
//			{
//				logger.debug("polling");
//			}
		}
	}
	
	public EmailServiceImpl(IMailListener listener)
	{
		serv = new MailService(EMAIL_SERVER_PROTO, EMAIL_SERVER_IP, EMAIL_SERVER_PORT, EMAIL_SERVER_USER,
				EMAIL_SERVER_PWD);
		serv.setListener(listener);
	}

	@Override
	public String toString()
	{
		return "EmailServiceImpl [serv=" + serv + "]";
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
					logger.debug("Polling email in progress");
					return;
				}
				lastExecution = executor.submit(actualTask);
			}
		}, 0, EMAIL_POLLING_INTERVAL, TimeUnit.SECONDS);
	}
}