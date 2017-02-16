package com.celera.message.cmmf;

import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EStatus;

public interface ICmmfProcessor
{
//	public void onOrder(Long id, EOrderStatus status, String reason, Integer giveupNum);
	public void onInstrumentUpdate(String symbol, EStatus status);
	public void onTradeReport(Long id, EOrderStatus status, String reason, Integer giveupNum);
	public void onTrade(Long ordId, Long tradeId, double price, int qty, EOrderStatus status, Integer giveupNum);
	public void onLastPrice(String symbol, double price);
}
