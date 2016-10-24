package com.vectails.xml.data;

import java.time.LocalDate;

import com.uts.tools.Uts2Dm;
import com.vectails.common.GenericFactory;
import com.vectails.session.IUtsLastTimeUpdateListener;
import com.vectails.xml.IUtsLastTimeUpdater;
import com.vectails.xml.IXmlNode;

public class Addressee extends GenericFactory implements IXmlNode, IUtsLastTimeUpdater
{
	public Addressee()
	{
		super(Addressee.class);
	}

	// attribute (Addressees)
	private String EntityCode = null;
	private String Code = null;
	private String TypeCode = null;
	private String Name = null;
	private String ShortCode = null;
	private String LastUpdateDateTime = null;
	
	// attributes (Quote/Addressees)
	private String Broadcast = null;
	private String AddresseeCode = null;

	public String getBroadcast()
	{
		return Broadcast;
	}

	public void setBroadcast(String broadcast)
	{
		Broadcast = broadcast;
	}

	public String getAddresseeCode()
	{
		return AddresseeCode;
	}

	public void setAddresseeCode(String addresseeCode)
	{
		AddresseeCode = addresseeCode;
	}

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

	@Override
	public LocalDate getLastTime()
	{
		return Uts2Dm.toLocalDate(LastUpdateDateTime);
	}
	
	@Override
	public void updateLastTime(IUtsLastTimeUpdateListener l)
	{
		l.setAddresseeLT(getLastTime());
	}
}