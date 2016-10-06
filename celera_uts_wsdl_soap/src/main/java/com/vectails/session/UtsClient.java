package com.vectails.session;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.ResourceManager;
import com.vectails.common.IResourceProperties;
import com.vectails.message.UtsMessageBuilder;
import com.vectails.message.processor.UtsMessageProcessor;
import com.vectails.xml.IXmlNode;
import com.vectalis.B2TDataModel;
import com.vectalis.B2TDataModelSoap;

public final class UtsClient implements Runnable
{
	final static Logger logger = LoggerFactory.getLogger(UtsClient.class);
	
	public static final String DEFAULT_ENTITY_CODE = "CELERA";
	public static final String DEFAULT_CLIENT_CODE = "DACTEST";
	public static final String DEFAULT_PASSWORD = "uat";
	public static final String DEFAULT_SESSION_ID = "7df96e02-e058-4212-a822-bd3cce2a87db";
	public static final String DEFAULT_CLIENT_VERSION = "UtsDacV1.8";
	public static final String DEFAULT_POLL_FREQ = "3000";

	public static final String ENTITY_CODE;
	public static final String CLIENT_CODE;
	public static final String PASSWORD;
	public static final String SESSION_ID;
	public static final String CLIENT_VERSION;
	public static final Long POLL_FREQ;
	
	public static final String WSDL_FILE;
	
	public static final String CXF_SPI_PROVIDER;
	
	final private UtsSession sess;
	
	static 
	{
		ENTITY_CODE = ResourceManager.getProperties(IResourceProperties.PROP_UTS_ENTITY_CODE, DEFAULT_ENTITY_CODE);
		CLIENT_CODE = ResourceManager.getProperties(IResourceProperties.PROP_UTS_CLIENT_CODE, DEFAULT_CLIENT_CODE);
		PASSWORD = ResourceManager.getProperties(IResourceProperties.PROP_UTS_PWD, DEFAULT_PASSWORD);
		SESSION_ID = ResourceManager.getProperties(IResourceProperties.PROP_UTS_SESSION_ID, DEFAULT_SESSION_ID);
		CLIENT_VERSION = ResourceManager.getProperties(IResourceProperties.PROP_UTS_CLIENT_VERESION, DEFAULT_CLIENT_VERSION);
		POLL_FREQ = Long.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_UTS_POLL_FREQ, DEFAULT_POLL_FREQ));

		WSDL_FILE = ResourceManager.getProperties(IResourceProperties.PROP_UTS_WSDL_FILE);

		CXF_SPI_PROVIDER = ResourceManager.getProperties(IResourceProperties.PROP_CXF_SPI_PROVIDER);
		if (CXF_SPI_PROVIDER != null)
		{
			String oldSpi = System.getProperty("javax.xml.ws.spi.Provider");
			System.setProperty("javax.xml.ws.spi.Provider", CXF_SPI_PROVIDER);
			logger.info("override javax.xml.ws.spi.Provider old[{}] new[{}]", oldSpi, CXF_SPI_PROVIDER);
		}
	}
	
	private B2TDataModel service = null;
	private B2TDataModelSoap port = null;
	
	private AtomicBoolean isStart = new AtomicBoolean(false);
	
	public UtsClient()
	{
		sess = new UtsSession(ENTITY_CODE, CLIENT_CODE, PASSWORD, SESSION_ID, CLIENT_VERSION);
		UtsMessageProcessor processor = UtsMessageProcessor.instance();
		processor.setUtsSessionListener(sess);
		
		logger.info("create Uts client : {}", sess.toString());
	}

	public synchronized void connect() 
	{
		if (WSDL_FILE == null)
		{
			service = new B2TDataModel();
		}
		else
		{
            URL url;
			try
			{
				logger.debug("create web service wsdl {}", WSDL_FILE);
				url = new File(WSDL_FILE).toURI().toURL();
//logger.debug("create web service wsdl {}", url);
				service = new B2TDataModel(url);
			}
			catch (MalformedURLException e)
			{
				logger.error("invalid wsdl url {}", WSDL_FILE, e);
			}

		}
		port = service.getB2TDataModelSoap();

		logger.info("connect() [{}, {} {}, {}] wsdl[{}]", ENTITY_CODE, CLIENT_CODE, SESSION_ID, CLIENT_VERSION, WSDL_FILE);
	}
	
	public synchronized void login()
	{
		String resp = port.updateDirectAccess(UtsMessageBuilder.buildLogin(sess));
		IXmlNode o = UtsMessageProcessor.parseXml(resp);
		UtsMessageProcessor.instance().dispatch(o);
		
		logger.info("login() - {}", o.toString());
	}
	
	public synchronized void poll()
	{
		String content = UtsMessageBuilder.buildGetAllQuotesDelta(sess);
		String resp = port.getAllMyEntityQuotesDirectAccessDelta(content);
		
		logger.info(content);
		logger.info(resp);
		IXmlNode o = UtsMessageProcessor.parseXml(resp);
		UtsMessageProcessor.instance().dispatch(o);
		
//		resp = port.getAllMyRepliesDirectAccess(UtsDirectAccessMessageBuilder.buildGetAllMyReplies(sess));
//		// System.out.println(resp);
//		UtsDirectAccessMessageProcessor.parseXml(resp);
	}
	
	public synchronized void logout()
	{
		try
		{
			String content = UtsMessageBuilder.buildLogout(sess);
			String resp = port.updateDirectAccess(content);
			UtsMessageProcessor.parseXml(resp);
		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}
	
	public synchronized void ping()
	{
		try
		{
			port.ping();
		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}

	public void stop() 
	{
		try
		{
			boolean isExpected = isStart.compareAndSet(true, false);
			if (!isExpected)
			{
				logout();
			}
			logger.info("Stop client");
		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}
	
	public void run()
	{
		long now = 0, diff = 0, lastSent = 0;
		try
		{
			isStart.compareAndSet(false, true);
			
			logger.info("Start client");
			
			connect();
			login();

			for (;;)
			{
				if (isStart.get()) 
				{
					now = System.currentTimeMillis();
					if (now > lastSent)
					{
						poll();
						lastSent = System.currentTimeMillis();
						diff = lastSent - now;
						try
						{
							Thread.sleep(POLL_FREQ - diff);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
			}

		}
		catch (Exception e)
		{
			logger.error("", e);
		}
		logger.info("Exit client");
	}
}
