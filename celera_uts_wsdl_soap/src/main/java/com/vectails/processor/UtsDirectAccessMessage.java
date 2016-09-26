package com.vectails.processor;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.celera.core.common.IXmlNode;
import com.vectails.data.LastUpdateDateTime;
import com.vectails.data.UtsDirectAccessResponse;

public class UtsDirectAccessMessage {
	
	private String EntityCode;
	private String ClientCode;
	private String Password;
	private String Command;
	private String SessionId;
	private String ClientVersion;
	
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
	}
	
	public String toXmlString() {
		return "<UtsDirectAccessMessage EntityCode='" + EntityCode + "' ClientCode='" + ClientCode + "' Password='"
				+ Password + "' Command='" + Command + "' SessionId='" + SessionId + "' ClientVersion='" + ClientVersion
				+ "'>" + addressee.toString() + underlying.toString() + derivativeType.toString() + indexFuture.toString()
				+ "</UtsDirectAccessMessage>";
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
