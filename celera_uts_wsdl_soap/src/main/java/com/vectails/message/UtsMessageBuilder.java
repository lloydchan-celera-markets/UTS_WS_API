package com.vectails.message;

import com.vectails.session.UtsSession;

public class UtsMessageBuilder
{
	public static final String entityCode = "CELERA";
	public static final String clientCode = "DACTEST";
	public static final String pwd = "uat";
	public static final String sessId = "7df96e02-e058-4212-a822-bd3cce2a87db";
	public static final String clientVer = "UtsDacV1.8";
	
	public static final String OPS_LOGIN = "OpenDirectAccessSession";
	public static final String OPS_LOGOUT = "CloseDirectAccessSession";
	public static final String OPS_GET_ALL_MY_ENTITY_QUOTES = "GetAllMyEntityQuotes";
	public static final String OPS_GET_ALL_MY_ENTITY_QUOTES_DELTA = "GetAllMyEntityQuotesDelta";
	public static final String OPS_GET_ALL_MY_REPLIES = "GetAllMyReplies";
	
	public static String buildLogin(UtsSession sess)
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(sess.getEntityCode(), sess.getClientCode(),
				sess.getPassword(), OPS_LOGIN, sess.getSessionId(), sess.getClientVersion());
		return msg.toXmlString();
	}

	public static String buildLogout(UtsSession sess)
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(sess.getEntityCode(), sess.getClientCode(),
				sess.getPassword(), OPS_LOGOUT, sess.getSessionId(), sess.getClientVersion());
		msg.setAddressee(null);
		msg.setDerivativeType(null);
		msg.setUnderlying(null);
		msg.setIndexFuture(null);
		return msg.toXmlString();
	}
	
	public static String buildGetAllQuotes(UtsSession sess)
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(sess.getEntityCode(), sess.getClientCode(),
				sess.getPassword(), OPS_GET_ALL_MY_ENTITY_QUOTES, sess.getSessionId(), sess.getClientVersion());
		return msg.toXmlString();
	}

	public static String buildGetAllQuotesDelta(UtsSession sess)
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(sess.getEntityCode(), sess.getClientCode(),
				sess.getPassword(), OPS_GET_ALL_MY_ENTITY_QUOTES_DELTA, sess.getSessionId(), sess.getClientVersion());
		msg.setTimeOfLastRecoveredQuotes(sess.getTimeOfLastRecoveredQuotes());
		return msg.toXmlString();
	}

	public static String buildGetAllMyReplies(UtsSession sess)
	{
		UtsDirectAccessMessage msg = new UtsDirectAccessMessage(sess.getEntityCode(), sess.getClientCode(),
				sess.getPassword(), OPS_GET_ALL_MY_REPLIES, sess.getSessionId(), sess.getClientVersion());
		msg.setTimeOfLastRecoveredQuotes(sess.getTimeOfLastRecoveredQuotes());
		return msg.toXmlString();
	}
}
