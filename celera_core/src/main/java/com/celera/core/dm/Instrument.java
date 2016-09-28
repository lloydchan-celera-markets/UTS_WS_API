package com.celera.core.dm;

public class Instrument
{
	private final String key;
	private final String market;
	private final String symbol;
	private final String name;
	private final String ISIN;
	private final String BLOOMBERG_CODE;
	private final String RIC;
	private EStatus status;
	private Long lastUpdate;
	
	public Instrument(String key, String market, String symbol, String name, String iSIN, String bLOOMBERG_CODE,
			String rIC)
	{
		this.key = key;
		this.market = market;
		this.symbol = symbol;
		this.name = name;
		ISIN = iSIN;
		BLOOMBERG_CODE = bLOOMBERG_CODE;
		RIC = rIC;
		status = EStatus.CLOSE;
		lastUpdate = 0L;
	}

	public void setStatus(EStatus status)
	{
		if (System.currentTimeMillis() > this.lastUpdate)
			this.status = status;
	}

	public void setLastUpdate(Long lastUpdate)
	{
		if (lastUpdate > this.lastUpdate)
			this.lastUpdate = lastUpdate;
	}
}
