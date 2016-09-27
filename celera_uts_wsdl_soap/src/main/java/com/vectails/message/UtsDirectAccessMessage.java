package com.vectails.message;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.LastUpdateDateTime;
import com.vectails.xml.data.UtsDirectAccessResponse;

public class UtsDirectAccessMessage {
	
	private String EntityCode;
	private String ClientCode;
	private String Password;
	private String Command;
	private String SessionId;
	private String ClientVersion;
	private String TimeOfLastRecoveredQuotes;
	
	private LastUpdateDateTime addressee = new LastUpdateDateTime("Addressee", "2000-01-01 00:00:00.000") ;
	private LastUpdateDateTime underlying = new LastUpdateDateTime("Underlying", "2000-01-01 00:00:00.000") ;
	private LastUpdateDateTime derivativeType = new LastUpdateDateTime("DerivativeType", "2000-01-01 00:00:00.000") ;
	private LastUpdateDateTime indexFuture = new LastUpdateDateTime("IndexFuture", "2000-01-01 00:00:00.000") ;
	
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
		this.addressee = addressee;
	}

	public void setUnderlying(LastUpdateDateTime underlying)
	{
		this.underlying = underlying;
	}

	public void setDerivativeType(LastUpdateDateTime derivativeType)
	{
		this.derivativeType = derivativeType;
	}

	public void setIndexFuture(LastUpdateDateTime indexFuture)
	{
		this.indexFuture = indexFuture;
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
	
	public static void parseXml(String resp) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(resp)));

			dom.getDocumentElement().normalize();

			Element docEle = dom.getDocumentElement();
//System.out.println(IXmlParser.UtsDirectAccessResponse.class.getName());			
IXmlNode l = (IXmlNode) new UtsDirectAccessResponse();
l.parseNode(docEle);
System.out.println(l);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
