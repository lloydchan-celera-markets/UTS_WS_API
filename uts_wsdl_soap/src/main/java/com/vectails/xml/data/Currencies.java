package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class Currencies extends GenericFactory implements IXmlNode 
{
	public Currencies()
	{
		super(Currency.class);
	}

	List<IXmlNode> Currency = new ArrayList<IXmlNode>();
}