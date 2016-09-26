package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class AllowedQuoteCreators extends GenericFactory implements IXmlNode
{
	public AllowedQuoteCreators()
	{
		super(QuoteCreator.class);
	}

	List<IXmlNode> QuoteCreator = new ArrayList<IXmlNode>();
}
