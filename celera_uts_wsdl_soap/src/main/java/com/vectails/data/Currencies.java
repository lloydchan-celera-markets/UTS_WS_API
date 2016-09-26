package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class Currencies extends GenericFactory implements IXmlNode 
{
	public Currencies()
	{
		super(Currency.class);
	}

	List<IXmlNode> Currency = new ArrayList<IXmlNode>();
}