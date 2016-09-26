package com.vectails.data;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class Currency extends GenericFactory implements IXmlNode
{
	public Currency()
	{
		super(Currency.class);
	}

	private String Code;
	private String Name;
	
	public void setCode(String code) {
		Code = code;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCode()
	{
		return Code;
	}

	public String getName()
	{
		return Name;
	}
}
