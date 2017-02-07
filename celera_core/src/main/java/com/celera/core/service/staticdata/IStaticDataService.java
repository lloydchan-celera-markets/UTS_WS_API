package com.celera.core.service.staticdata;

import java.util.List;

import com.celera.core.dm.IInstrument;

public interface IStaticDataService
{
	public IInstrument getInstr(String name);
	public List<IInstrument> getAllInstruments();
	public String getClearingMember(String code);
	
	public void onInstrumentUpdate(IInstrument i);
}
