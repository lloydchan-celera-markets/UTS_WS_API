package com.vectails.xml.data;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

public class BidFrom extends GenericFactory implements IXmlNode
{
	// elements
	private String EntityCode = null;
	private String AddresseeCode = null;
	private String ShortCode = null;

	public BidFrom()
	{
		super(BidFrom.class);
	}

	public void setEntityCode(String entityCode)
	{
		EntityCode = entityCode;
	}

	public void setAddresseeCode(String addresseeCode)
	{
		AddresseeCode = addresseeCode;
	}

	public void setShortCode(String shortCode)
	{
		ShortCode = shortCode;
	}

	public String getEntityCode()
	{
		return EntityCode;
	}

	public String getAddresseeCode()
	{
		return AddresseeCode;
	}

	public String getShortCode()
	{
		return ShortCode;
	}
}
