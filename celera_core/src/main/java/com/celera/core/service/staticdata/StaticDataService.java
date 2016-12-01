package com.celera.core.service.staticdata;

import com.celera.core.dm.IInstrument;

public class StaticDataService implements IStaticDataListener, IStaticDataService
{
	@Override
	public IInstrument getInstr(String name)
	{
		return null;
	}

	@Override
	public void onInstrumentUpdate(IInstrument instr)
	{
	}
}
