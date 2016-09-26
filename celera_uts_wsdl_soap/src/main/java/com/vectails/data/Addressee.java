package com.vectails.data;

import com.celera.core.common.IXmlNode;

public class Addressee implements IXmlNode
{
	private String EntityCode = null;
	private String Code = null;
	private String TypeCode = null;
	private String Name = null;
	private String ShortCode = null;
	private String LastUpdateDateTime = null;

	public void setEntityCode(String entityCode)
	{
		EntityCode = entityCode;
	}

	public void setCode(String code)
	{
		Code = code;
	}

	public void setTypeCode(String typeCode)
	{
		TypeCode = typeCode;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public void setShortCode(String shortCode)
	{
		ShortCode = shortCode;
	}

	public void setLastUpdateDateTime(String lastUpdateDateTime)
	{
		LastUpdateDateTime = lastUpdateDateTime;
	}

	public String getEntityCode()
	{
		return EntityCode;
	}

	public String getCode()
	{
		return Code;
	}

	public String getTypeCode()
	{
		return TypeCode;
	}

	public String getName()
	{
		return Name;
	}

	public String getShortCode()
	{
		return ShortCode;
	}

	public String getLastUpdateDateTime()
	{
		return LastUpdateDateTime;
	}
}