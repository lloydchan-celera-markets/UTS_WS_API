package com.celera.gateway;

import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;

public interface IOrderGatewayListener
{
	public void onOrder(IOrder o); 
	public void onQuote(IOrder q); 
	public void onTrade(ITrade t); 
	public void onTradeReport(Long id, EOrderStatus status, String reason);
	public void onInstrumentUpdate(IInstrument i);
}
