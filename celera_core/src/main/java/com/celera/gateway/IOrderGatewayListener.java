package com.celera.gateway;


import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;

public interface IOrderGatewayListener
{
	public void onOrder(IOrder o); 
	public void onQuote(IOrder q); 
	public void onTrade(ITrade t); 
}
