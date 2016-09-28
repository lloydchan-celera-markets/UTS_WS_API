package com.vectails.message;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.UtsDirectAccessResponse;

public class UtsApiMessageProcessor
{
	public static IXmlNode parseXml(String resp)
	{
		IXmlNode o = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;

			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(resp)));

			dom.getDocumentElement().normalize();

			Element docEle = dom.getDocumentElement();

			o = (IXmlNode) new UtsDirectAccessResponse();
			o.parseNode(docEle);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}
	
	public static void dispatch(IXmlNode o) 
	{
		if (o instanceof UtsDirectAccessResponse) {
			List l = ((UtsDirectAccessResponse)o).getDerivativeTypes();
		}
		
	}
}
