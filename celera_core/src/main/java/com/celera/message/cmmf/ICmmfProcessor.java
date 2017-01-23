package com.celera.message.cmmf;

import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EStatus;

public interface ICmmfProcessor
{
	public void onInstrumentUpdate(String symbol, EStatus status);
	public void onTradeReport(Long id, EOrderStatus status, String reason);
}
