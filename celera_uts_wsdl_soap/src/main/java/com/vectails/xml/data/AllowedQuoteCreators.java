package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class AllowedQuoteCreators extends GenericFactory implements IXmlNode
{
	public AllowedQuoteCreators()
	{
		super(QuoteCreator.class);
	}

	List<IXmlNode> QuoteCreator = new ArrayList<IXmlNode>();
}
