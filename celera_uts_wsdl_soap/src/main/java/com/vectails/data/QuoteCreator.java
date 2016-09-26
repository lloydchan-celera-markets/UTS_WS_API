package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class QuoteCreator extends GenericFactory implements IXmlNode
{
	private String Code = null;

	List<IXmlNode> SubscribedUnderlyings = new ArrayList<IXmlNode>();

	public QuoteCreator()
	{
		super(SubscribedUnderlyings.class);
	}

	public String getCode()
	{
		return Code;
	}

	public void setCode(String code)
	{
		Code = code;
	}
}