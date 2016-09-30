package com.vectails.oms;

import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;

public interface IOMSListener
{
	public void onOrder(IOrder o); 
	public void onQuote(IOrder q); 
	public void onTrade(ITrade t); 
	
}
