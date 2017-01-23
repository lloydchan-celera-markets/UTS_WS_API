package com.celera.core.dm;

import java.time.LocalDate;

public class Instrument implements IInstrument
{
	protected final String market;
	protected String symbol;
	protected final EInstrumentType type;
	protected final String name;
	protected final String ISIN;
	protected final String BLOOMBERG_CODE;
	protected final String RIC;
	protected EStatus status;
	protected LocalDate lastUpdate;

	public Instrument(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE, String rIC)
	{
		this.market = market;
		this.symbol = symbol;
		this.type = type;
		this.name = name;
		ISIN = iSIN;
		BLOOMBERG_CODE = bLOOMBERG_CODE;
		RIC = rIC;
		status = EStatus.CLOSE;
		this.lastUpdate = LocalDate.now();
	}

//	@Override
	public String key()
	{
		return market + "_" + symbol;
	}
	
	public String getSymbol()
	{
		return symbol;
	}

	public EInstrumentType getType()
	{
		return type;
	}

//	@Override
	public void setStatus(EStatus status)
	{
		LocalDate now = LocalDate.now();
		if (now.isAfter(this.lastUpdate))
		{
			this.status = status;
			this.lastUpdate = now;
		}
	}

	public String getName()
	{
		return name;
	}

	public LocalDate getLastUpdate()
	{
		return lastUpdate;
	}

//	@Override
	public EStatus getStatus()
	{
		return this.status;
	}

	@Override
	public String toString()
	{
		return "Instrument [market=" + market + ", symbol=" + symbol + ", type=" + type + ", name=" + name + ", ISIN="
				+ ISIN + ", BLOOMBERG_CODE=" + BLOOMBERG_CODE + ", RIC=" + RIC + ", status=" + status + ", lastUpdate="
				+ lastUpdate + "]";
	}

	@Override
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
}
