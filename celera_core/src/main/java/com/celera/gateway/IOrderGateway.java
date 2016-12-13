package com.celera.gateway;

import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;

public interface IOrderGateway
{
	void createOrder(IOrder o);
	void modifyOrder(IOrder o);
	void cancelOrder(IOrder o);
	void createQuote(IQuote quote);
	void modifyQuote(IQuote q);
	void cancelQuote(IQuote q);
}
