package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class Quotes extends GenericFactory implements IXmlNode 
{
	public Quotes()
	{
		super(Quote.class);
	}

	List<IXmlNode> Quote = new ArrayList<IXmlNode>();

	public List<IXmlNode> getQuote()
	{
		return Quote;
	}
}