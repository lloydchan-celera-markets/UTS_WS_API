package com.vectails.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectails.message.UtsDirectAccessMessageProcessor;
import com.vectails.data.StaticDataManager;
import com.vectails.message.UtsDirectAccessMessage;
import com.vectails.message.UtsDirectAccessMessageBuilder;
import com.vectails.xml.IXmlNode;
import com.vectalis.B2TDataModel;
import com.vectalis.B2TDataModelSoap;

public final class UtsDirectAccessClient
{
	final static Logger logger = LoggerFactory.getLogger(UtsDirectAccessClient.class);
	
	public static final String ENTITY_CODE = "CELERA";
	public static final String CLIENT_CODE = "DACTEST";
	public static final String PASSWORD = "uat";
	public static final String SESSION_ID = "7df96e02-e058-4212-a822-bd3cce2a87db";
	public static final String CLIENT_VERSION = "UtsDacV1.8";
	
	final private UtsDirectAccessSession sess;
	
	private B2TDataModel service = null;
	private B2TDataModelSoap port = null;
	
	public UtsDirectAccessClient()
	{
		sess = new UtsDirectAccessSession(ENTITY_CODE, CLIENT_CODE, PASSWORD, SESSION_ID, CLIENT_VERSION);
		UtsDirectAccessMessageProcessor.setCallback(sess);
	}

	public void connect() 
	{
		service = new B2TDataModel();
		port = service.getB2TDataModelSoap();

		logger.info("connect() [{}, {} {}, {}]", ENTITY_CODE, CLIENT_CODE, SESSION_ID, CLIENT_VERSION);
	}
	
	public void login()
	{
		// login
		String resp = port.updateDirectAccess(UtsDirectAccessMessageBuilder.buildLogin(sess));
		IXmlNode o = UtsDirectAccessMessageProcessor.parseXml(resp);
		UtsDirectAccessMessageProcessor.dispatch(o);
		
		logger.debug("login() {}", o.toString());
		
System.out.println(resp); 
	}
	
	public void poll()
	{
//		while (true) 
//		{
//			
//		}
		String resp = port.getAllMyEntityQuotesDirectAccess(UtsDirectAccessMessageBuilder.buildGetAllQuotes(sess));
		// System.out.println(resp);
		UtsDirectAccessMessageProcessor.parseXml(resp);

		resp = port.getAllMyEntityQuotesDirectAccessDelta(UtsDirectAccessMessageBuilder.buildGetAllQuotesDelta(sess));
		// System.out.println(resp);
		UtsDirectAccessMessageProcessor.parseXml(resp);

		resp = port.getAllMyRepliesDirectAccess(UtsDirectAccessMessageBuilder.buildGetAllMyReplies(sess));
		// System.out.println(resp);
		UtsDirectAccessMessageProcessor.parseXml(resp);

		// port.getLoginGenericURL(new String());
		resp = port.ping();
		// System.out.println(resp);
	}
	
	public void logout()
	{
		// logout
		String resp = port.updateDirectAccess(UtsDirectAccessMessageBuilder.buildLogout(sess));
		// System.out.println(resp);
		UtsDirectAccessMessageProcessor.parseXml(resp);
	}
	
	public static void main(String args[]) throws Exception
	{
		UtsDirectAccessClient client = new UtsDirectAccessClient();
		client.connect();
		client.login();
		
		client.poll();
//StaticDataManager.print();

//		B2TDataModel utsService = new B2TDataModel();
//		B2TDataModelSoap port = utsService.getB2TDataModelSoap();
//
//		// login
//		String resp = port.updateDirectAccess(UtsApiMessageBuilder.buildLogin());
//		// System.out.println(resp);
//		IXmlNode o = UtsApiMessageProcessor.parseXml(resp);
//		System.out.println(o.toString());
//		
//		resp = port.getAllMyEntityQuotesDirectAccess(UtsApiMessageBuilder.buildGetAllQuotes());
//		// System.out.println(resp);
//		UtsApiMessageProcessor.parseXml(resp);
//
//		resp = port.getAllMyEntityQuotesDirectAccessDelta(UtsApiMessageBuilder.buildGetAllQuotesDelta());
//		// System.out.println(resp);
//		UtsApiMessageProcessor.parseXml(resp);
//
//		resp = port.getAllMyRepliesDirectAccess(UtsApiMessageBuilder.buildGetAllMyReplies());
//		// System.out.println(resp);
//		UtsApiMessageProcessor.parseXml(resp);
//
//		// port.getLoginGenericURL(new String());
//		resp = port.ping();
//		// System.out.println(resp);
//
//		// logout
//		resp = port.updateDirectAccess(UtsApiMessageBuilder.buildLogout());
//		// System.out.println(resp);
//		UtsApiMessageProcessor.parseXml(resp);

		System.exit(0);
	}

}
