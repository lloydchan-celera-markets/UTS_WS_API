package com.celera.core.dm;

public class Addressee
{
	private String entityCode = null;
	private String addrCode = null;
	private Boolean isBroadcast = null;

	public String getEntityCode()
	{
		return entityCode;
	}

	public void setEntityCode(String entityCode)
	{
		this.entityCode = entityCode;
	}

	public String getAddrCode()
	{
		return addrCode;
	}

	public void setAddrCode(String addrCode)
	{
		this.addrCode = addrCode;
	}

	public Boolean getIsBroadcast()
	{
		return isBroadcast;
	}

	public void setIsBroadcast(Boolean isBroadcast)
	{
		this.isBroadcast = isBroadcast;
	}

	@Override
	public String toString()
	{
		return "Addressee [entityCode=" + entityCode + ", addrCode=" + addrCode + ", isBroadcast=" + isBroadcast + "]";
	}
}
