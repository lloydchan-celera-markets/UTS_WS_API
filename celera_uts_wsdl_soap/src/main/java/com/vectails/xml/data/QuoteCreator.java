package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class QuoteCreator extends GenericFactory implements IXmlNode
{
	private String Code = null;

	List<IXmlNode> SubscribedUnderlyings = new ArrayList<IXmlNode>();
	List<IXmlNode> Addressees = new ArrayList<IXmlNode>();

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