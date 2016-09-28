package com.vectails.message;

public class UtsApiMessageBuilder
{
	public static final String ENTITY_CODE = "CELERA";
	public static final String CLIENT_CODE = "DACTEST";
	public static final String PASSWORD = "uat";
	public static final String SESSION_ID = "7df96e02-e058-4212-a822-bd3cce2a87db";
	public static final String CLIENT_VERSION = "UtsDacV1.8";
	
	public static final String OPS_LOGIN = "OpenDirectAccessSession";
	public static final String OPS_LOGOUT = "CloseDirectAccessSession";
	public static final String OPS_GET_ALL_MY_ENTITY_QUOTES = "GetAllMyEntityQuotes";
	public static final String OPS_GET_ALL_MY_ENTITY_QUOTES_DELTA = "GetAllMyEntityQuotesDelta";
	public static final String OPS_GET_ALL_MY_REPLIES = "GetAllMyReplies";
	
	public static String buildLogin()
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(ENTITY_CODE, CLIENT_CODE, PASSWORD, OPS_LOGIN,
				SESSION_ID, CLIENT_VERSION);
		return msg.toXmlString();
	}

	public static String buildLogout()
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(ENTITY_CODE, CLIENT_CODE, PASSWORD, OPS_LOGOUT,
				SESSION_ID, CLIENT_VERSION);
		msg.setAddressee(null);
		msg.setDerivativeType(null);
		msg.setUnderlying(null);
		msg.setIndexFuture(null);
		return msg.toXmlString();
	}
	
	public static String buildGetAllQuotes()
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(ENTITY_CODE, CLIENT_CODE, PASSWORD, OPS_GET_ALL_MY_ENTITY_QUOTES,
				SESSION_ID, CLIENT_VERSION);
		return msg.toXmlString();
	}

	public static String buildGetAllQuotesDelta()
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(ENTITY_CODE, CLIENT_CODE, PASSWORD, OPS_GET_ALL_MY_ENTITY_QUOTES_DELTA,
				SESSION_ID, CLIENT_VERSION);
		msg.setTimeOfLastRecoveredQuotes("0");
		return msg.toXmlString();
	}

	public static String buildGetAllMyReplies()
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(ENTITY_CODE, CLIENT_CODE, PASSWORD, OPS_GET_ALL_MY_REPLIES,
				SESSION_ID, CLIENT_VERSION);
		return msg.toXmlString();
	}
}
