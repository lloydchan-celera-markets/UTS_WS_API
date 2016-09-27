package com.vectails.xml.data;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.IXmlParser;

public class LegDerivativeItem extends GenericFactory implements IXmlNode
{
	public LegDerivativeItem()
	{
		super(LegDerivativeItem.class);
	}

	private String Value;
	private String Name;

	public String getValue()
	{
		return Value;
	}

	public void setValue(String value)
	{
		Value = value;
	}

	public String getName()
	{
		return Name;
	}

	public void setName(String name)
	{
		Name = name;
	}
}
