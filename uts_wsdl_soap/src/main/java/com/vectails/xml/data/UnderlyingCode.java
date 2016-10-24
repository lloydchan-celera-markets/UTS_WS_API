package com.vectails.xml.data;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlParser;

public class UnderlyingCode extends GenericFactory implements IXmlParser
{

	public UnderlyingCode()
	{
		super(UnderlyingCode.class);
	}

	private String Code;
	private String Name;

	public void setCode(String code)
	{
		Code = code;
	}

	public void setName(String name)
	{
		Name = name;
	}
}
