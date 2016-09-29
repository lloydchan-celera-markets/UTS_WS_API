package com.vectails.session;

import java.time.LocalDate;

import com.celera.core.dm.IInstrument;
import com.vectails.xml.INodeUpdateListener;
import com.vectails.xml.IXmlNode;

public interface IOnUpdateNode
{
//	public <T> void onUpdateLTAddressee(IInstrument instr);
	public <T> void onUpdateNode(INodeUpdateListener node);
//	public <T> void onUpdateDerivativeTypeTime(IInstrument instr);
//	public <T> void onUpdateTimeIndexFuture(IInstrument instr);
}
