package com.celera.gateway;

import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;

public interface IOrderGatewayListener
{
	public void onCoreOrder(IOrder o); 
	public void onQuote(IOrder q); 
	public void onCoreTrade(ITrade t); 
	public void onCoreTradeReport(Long id, EOrderStatus status, String reason, Integer giveupNum);
	public void onInstrumentUpdate(IInstrument i);
}
