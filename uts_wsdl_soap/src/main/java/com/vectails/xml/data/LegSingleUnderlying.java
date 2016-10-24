package com.vectails.xml.data;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

public class LegSingleUnderlying extends GenericFactory implements IXmlNode
{

	public LegSingleUnderlying()
	{
		super(LegSingleUnderlying.class);
	}

	// attributes
	private String Spot = null;
	private String FutureExpiry = null;
	private String FinancialMarketCode = null;
	private String UnderlyingTypeCode = null;
	private String UnderlyingCode = null;

	public String getSpot()
	{
		return Spot;
	}

	public void setSpot(String spot)
	{
		Spot = spot;
	}

	public String getFutureExpiry()
	{
		return FutureExpiry;
	}

	public void setFutureExpiry(String futureExpiry)
	{
		FutureExpiry = futureExpiry;
	}

	public String getFinancialMarketCode()
	{
		return FinancialMarketCode;
	}

	public void setFinancialMarketCode(String financialMarketCode)
	{
		FinancialMarketCode = financialMarketCode;
	}

	public String getUnderlyingTypeCode()
	{
		return UnderlyingTypeCode;
	}

	public void setUnderlyingTypeCode(String underlyingTypeCode)
	{
		UnderlyingTypeCode = underlyingTypeCode;
	}

	public String getUnderlyingCode()
	{
		return UnderlyingCode;
	}

	public void setUnderlyingCode(String underlyingCode)
	{
		UnderlyingCode = underlyingCode;
	}
}