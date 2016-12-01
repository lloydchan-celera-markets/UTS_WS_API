package com.celera.core.service.staticdata;

import com.celera.core.dm.IInstrument;

public interface IStaticDataListener
{
	public void onInstrumentUpdate(IInstrument instr);
}
