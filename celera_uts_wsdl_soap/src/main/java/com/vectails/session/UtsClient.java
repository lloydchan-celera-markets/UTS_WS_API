package com.vectails.session;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectails.message.UtsDirectAccessMessage;
import com.vectails.message.UtsMessageBuilder;
import com.vectails.message.processor.UtsMessageProcessor;
import com.vectails.sds.UtsStaticDataService;
import com.vectails.xml.IXmlNode;
import com.vectalis.B2TDataModel;
import com.vectalis.B2TDataModelSoap;

public final class UtsClient implements Runnable
{
	final static Logger logger = LoggerFactory.getLogger(UtsClient.class);
	
	public static final String ENTITY_CODE = "CELERA";
	public static final String CLIENT_CODE = "DACTEST";
	public static final String PASSWORD = "uat";
	public static final String SESSION_ID = "7df96e02-e058-4212-a822-bd3cce2a87db";
	public static final String CLIENT_VERSION = "UtsDacV1.8";
	
	final private UtsSession sess;
	
	private B2TDataModel service = null;
	private B2TDataModelSoap port = null;
	
	private AtomicBoolean isStart = new AtomicBoolean(false);
	
	public UtsClient()
	{
		sess = new UtsSession(ENTITY_CODE, CLIENT_CODE, PASSWORD, SESSION_ID, CLIENT_VERSION);
		UtsMessageProcessor processor = UtsMessageProcessor.instance();
		processor.setUtsSessionListener(sess);
	}

	public void connect() 
	{
		service = new B2TDataModel();
		port = service.getB2TDataModelSoap();

		logger.info("connect() [{}, {} {}, {}]", ENTITY_CODE, CLIENT_CODE, SESSION_ID, CLIENT_VERSION);
	}
	
	public void login()
	{
		String resp = port.updateDirectAccess(UtsMessageBuilder.buildLogin(sess));
		IXmlNode o = UtsMessageProcessor.parseXml(resp);
		UtsMessageProcessor.instance().dispatch(o);
		
		logger.debug("login() {}", o.toString());
//System.out.println(o.toString()); 
	}
	
	public void poll()
	{
		String content = UtsMessageBuilder.buildGetAllQuotesDelta(sess);
		String resp = port.getAllMyEntityQuotesDirectAccessDelta(content);
		
		logger.info(content);
		logger.trace(resp);
		IXmlNode o = UtsMessageProcessor.parseXml(resp);
		UtsMessageProcessor.instance().dispatch(o);
		
//		resp = port.getAllMyRepliesDirectAccess(UtsDirectAccessMessageBuilder.buildGetAllMyReplies(sess));
//		// System.out.println(resp);
//		UtsDirectAccessMessageProcessor.parseXml(resp);
	}
	
	public void logout()
	{
		String content = UtsMessageBuilder.buildLogout(sess);
		String resp = port.updateDirectAccess(content);
		UtsMessageProcessor.parseXml(resp);
	}
	
	public void ping()
	{
		String resp = port.ping();
	}

	public void stop() 
	{
		isStart.compareAndSet(true, false);
		logout();
		logger.info("Stop client");
	}
	
	@Override
	public void run()
	{
		try
		{
			isStart.compareAndSet(false, true);
			
			logger.info("Start client");
			
			UtsClient client = new UtsClient();
			client.connect();
			client.login();

			for (;;)
			{
				if (isStart.get()) {
					
					client.poll();
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
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
