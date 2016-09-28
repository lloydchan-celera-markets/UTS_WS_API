package com.vectails.message;

import com.vectails.xml.data.LastUpdateDateTime;

public class UtsDirectAccessMessage {
	
	private final String EntityCode;
	private final String ClientCode;
	private final String Password;
	private final String Command;
	private final String SessionId;
	private final String ClientVersion;
	private String TimeOfLastRecoveredQuotes;

	static private LastUpdateDateTime addressee = new LastUpdateDateTime("Addressee", "2000-01-01 00:00:00.000");
	static private LastUpdateDateTime underlying = new LastUpdateDateTime("Underlying", "2000-01-01 00:00:00.000");
	static private LastUpdateDateTime derivativeType = new LastUpdateDateTime("DerivativeType",
			"2000-01-01 00:00:00.000");
	static private LastUpdateDateTime indexFuture = new LastUpdateDateTime("IndexFuture", "2000-01-01 00:00:00.000");

	
//	=\"CELERA\" ClientCode=\"DACTEST\" Password=\"uat\" Command=\"OpenDirectAccessSession\" SessionId=\"7df96e02-e058-4212-a822-bd3cce2a87db\" ClientVersion=\"UtsDacV1.8\"></UtsDirectAccessMessage";
	
	public UtsDirectAccessMessage(String entityCode, String clientCode, String password, String command,
			String sessionId, String clientVersion) {
		EntityCode = entityCode;
		ClientCode = clientCode;
		Password = password;
		Command = command;
		SessionId = sessionId;
		ClientVersion = clientVersion;

		TimeOfLastRecoveredQuotes  = null;
	}
	
	public void setAddressee(LastUpdateDateTime addressee)
	{
		UtsDirectAccessMessage.addressee = addressee;
	}

	public void setUnderlying(LastUpdateDateTime underlying)
	{
		UtsDirectAccessMessage.underlying = underlying;
	}

	public void setDerivativeType(LastUpdateDateTime derivativeType)
	{
		UtsDirectAccessMessage.derivativeType = derivativeType;
	}

	public void setIndexFuture(LastUpdateDateTime indexFuture)
	{
		UtsDirectAccessMessage.indexFuture = indexFuture;
	}

	public void setTimeOfLastRecoveredQuotes(String timeOfLastRecoveredQuotes)
	{
		TimeOfLastRecoveredQuotes = timeOfLastRecoveredQuotes;
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder(0);
		sb.append("<UtsDirectAccessMessage EntityCode='").append(EntityCode)
			.append("' ClientCode='").append(ClientCode)
			.append("' Password='").append(Password)
			.append("' Command='").append(Command)
			.append("' SessionId='").append(SessionId)
			.append("' ClientVersion='").append(ClientVersion)
			.append("'>");
		
		if (addressee != null)
			sb.append(addressee.toString());
		if (underlying != null)
			sb.append(underlying.toString());
		if (derivativeType != null)
			sb.append(derivativeType.toString());
		if (indexFuture != null)
			sb.append(indexFuture.toString());
		if (TimeOfLastRecoveredQuotes != null)
			sb.append("<TimeOfLastRecoveredQuotes>").append(TimeOfLastRecoveredQuotes).append("</TimeOfLastRecoveredQuotes>");
				
		sb.append("</UtsDirectAccessMessage>");
		
		return sb.toString();
	}
}
