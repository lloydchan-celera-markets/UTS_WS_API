package com.vectails.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectails.message.UtsApiMessageBuilder;
import com.vectails.message.UtsApiMessageProcessor;
import com.vectails.message.UtsDirectAccessMessage;
import com.vectails.xml.IXmlNode;
import com.vectalis.B2TDataModel;
import com.vectalis.B2TDataModelSoap;

public final class UtsDirectAccessClient
{
	final static Logger logger = LoggerFactory.getLogger(UtsDirectAccessClient.class);
	final static StringBuilder sb = new StringBuilder();
	
	private UtsDirectAccessSession sess;
	
	public static final String ENTITY_CODE = "CELERA";
	public static final String CLIENT_CODE = "DACTEST";
	public static final String PASSWORD = "uat";
	public static final String SESSION_ID = "7df96e02-e058-4212-a822-bd3cce2a87db";
	public static final String CLIENT_VERSION = "UtsDacV1.8";
	
	private B2TDataModel service = null;
	private B2TDataModelSoap port = null;
	
	public UtsDirectAccessClient()
	{
		sess = new UtsDirectAccessSession(ENTITY_CODE, CLIENT_CODE, PASSWORD, SESSION_ID, CLIENT_VERSION);
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
		String resp = port.updateDirectAccess(UtsApiMessageBuilder.buildLogin());
		IXmlNode o = UtsApiMessageProcessor.parseXml(resp);
		
		logger.info(o.toString());
	}
	
	public void poll()
	{
		String resp = port.getAllMyEntityQuotesDirectAccess(UtsApiMessageBuilder.buildGetAllQuotes());
		// System.out.println(resp);
		UtsApiMessageProcessor.parseXml(resp);

		resp = port.getAllMyEntityQuotesDirectAccessDelta(UtsApiMessageBuilder.buildGetAllQuotesDelta());
		// System.out.println(resp);
		UtsApiMessageProcessor.parseXml(resp);

		resp = port.getAllMyRepliesDirectAccess(UtsApiMessageBuilder.buildGetAllMyReplies());
		// System.out.println(resp);
		UtsApiMessageProcessor.parseXml(resp);

		// port.getLoginGenericURL(new String());
		resp = port.ping();
		// System.out.println(resp);
	}
	
	public void logout()
	{
		// logout
		String resp = port.updateDirectAccess(UtsApiMessageBuilder.buildLogout());
		// System.out.println(resp);
		UtsApiMessageProcessor.parseXml(resp);
	}
	
	public static void main(String args[]) throws Exception
	{
		UtsDirectAccessClient client = new UtsDirectAccessClient();
		client.connect();
		client.login();
		
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
