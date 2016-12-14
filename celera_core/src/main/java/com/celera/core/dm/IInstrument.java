package com.celera.core.dm;

import java.time.LocalDate;

public interface IInstrument
{
	public static final int CMMF_PRICE_FACTOR = 10000;
	
	public String key();
	public void setStatus(EStatus status);
	public EStatus getStatus();
	public LocalDate getLastUpdate();
	public String getName();
	public EInstrumentType getType();
	public String toString();
	public String getSymbol();
}
