package com.vectails.xml.data;

import java.time.LocalDate;

import com.vectails.common.GenericFactory;
import com.vectails.message.ICommonFields;
import com.vectails.xml.INodeUpdateListener;
import com.vectails.xml.IXmlNode;

public class IndexFuture extends GenericFactory implements IXmlNode, INodeUpdateListener
{
	public IndexFuture()
	{
		super(IndexFuture.class);
	}

	private String ExpiryDate = null;
	private String IndexCode = null;
	private String IndexFinancialMarketCode = null;
	private String IndexUnderlyingTypeCode = null;
	private String LastUpdateDateTime = null;
	
	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}

	public void setIndexCode(String indexCode) {
		IndexCode = indexCode;
	}

	public void setIndexFinancialMarketCode(String indexFinancialMarketCode) {
		IndexFinancialMarketCode = indexFinancialMarketCode;
	}

	public void setIndexUnderlyingTypeCode(String indexUnderlyingTypeCode) {
		IndexUnderlyingTypeCode = indexUnderlyingTypeCode;
	}

	public void setLastUpdateDateTime(String lastUpdateDateTime) {
		LastUpdateDateTime = lastUpdateDateTime;
	}

	public String getExpiryDate()
	{
		return ExpiryDate;
	}

	public String getIndexCode()
	{
		return IndexCode;
	}

	public String getIndexFinancialMarketCode()
	{
		return IndexFinancialMarketCode;
	}

	public String getIndexUnderlyingTypeCode()
	{
		return IndexUnderlyingTypeCode;
	}

	public String getLastUpdateDateTime()
	{
		return LastUpdateDateTime;
	}
	
	@Override
	public LocalDate getLastTime()
	{
		return LocalDate.parse(LastUpdateDateTime, ICommonFields.DT_FORMATTER);
	}
}
