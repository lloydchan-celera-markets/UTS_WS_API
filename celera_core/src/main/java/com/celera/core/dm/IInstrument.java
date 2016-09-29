package com.celera.core.dm;

import java.time.LocalDate;

public interface IInstrument
{
	public String key();
	public void setStatus(EStatus status);
	public EStatus getStatus();
	public LocalDate getLastUpdate();
	public String toString();
}
