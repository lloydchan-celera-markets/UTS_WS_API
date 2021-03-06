package com.vectails.xml.data;

import java.time.LocalDate;

import com.uts.tools.Uts2Dm;
import com.vectails.common.GenericFactory;
import com.vectails.session.IUtsLastTimeUpdateListener;
import com.vectails.xml.IUtsLastTimeUpdater;
import com.vectails.xml.IXmlNode;

public class Underlying extends GenericFactory implements IXmlNode, IUtsLastTimeUpdater
{
	public Underlying()
	{
		super(Underlying.class);
	}

	private String FinancialMarketCode = null;
	private String UnderlyingTypeCode = null;
	private String Code = null;
	private String Name = null;
	private String Reuters = null;
	private String ISIN = null;
	private String Bloomberg = null;
	private String IsObsolete = null;
	private String HasFutures = null;
	private String LastUpdateDateTime = null;
	
	public void setCode(String code) {
		Code = code;
	}

	public void setFinancialMarketCode(String financialMarketCode) {
		FinancialMarketCode = financialMarketCode;
	}

	public void setUnderlyingTypeCode(String underlyingTypeCode) {
		UnderlyingTypeCode = underlyingTypeCode;
	}

	public void setReuters(String reuters) {
		Reuters = reuters;
	}

	public void setISIN(String iSIN) {
		ISIN = iSIN;
	}

	public void setBloomberg(String bloomberg) {
		Bloomberg = bloomberg;
	}

	public void setIsObsolete(String isObsolete) {
		IsObsolete = isObsolete;
	}

	public void setHasFutures(String hasFutures) {
		HasFutures = hasFutures;
	}

	public void setLastUpdateDateTime(String lastUpdateDateTime) {
		LastUpdateDateTime = lastUpdateDateTime;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getFinancialMarketCode()
	{
		return FinancialMarketCode;
	}

	public String getUnderlyingTypeCode()
	{
		return UnderlyingTypeCode;
	}

	public String getCode()
	{
		return Code;
	}

	public String getName()
	{
		return Name;
	}

	public String getReuters()
	{
		return Reuters;
	}

	public String getISIN()
	{
		return ISIN;
	}

	public String getBloomberg()
	{
		return Bloomberg;
	}

	public String getIsObsolete()
	{
		return IsObsolete;
	}

	public String getHasFutures()
	{
		return HasFutures;
	}

	public String getLastUpdateDateTime()
	{
		return LastUpdateDateTime;
	}
	
	@Override
	public LocalDate getLastTime()
	{
		return Uts2Dm.toLocalDate(LastUpdateDateTime);
	}

	@Override
	public void updateLastTime(IUtsLastTimeUpdateListener l)
	{
		l.setUnderlyingLT(getLastTime());
	}
}