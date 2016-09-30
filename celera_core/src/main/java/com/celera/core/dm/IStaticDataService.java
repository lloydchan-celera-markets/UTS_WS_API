package com.celera.core.dm;

public interface IStaticDataService
{
	public void onInstrumentUpdate(IInstrument instr);
	public IInstrument get(String key);
	public String toKey(IInstrument instr);
	public String genKey(String key);
}
