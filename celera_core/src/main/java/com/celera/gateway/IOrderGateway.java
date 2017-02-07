package com.celera.gateway;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.IBlockTradeReport;
import com.celera.core.dm.IInstrument;
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
	void createTradeReport(IOrder o);
	void cancelTradeReport(IOrder o);
//	void createBlockTradeReport(List<IOrder> tradeReportList, Long id, String synSymbol);
	void createBlockTradeReport(IBlockTradeReport block);
	/**
	 * @param password
	 * @param newPassword
	 * newPassword == null -> login , newPassword != null -> change password
	 */
	void login(String password);
	void changePassword(String oldPwd, String newPwd);
	void logout();
	void SOD();
	void subscribeMarketData();
	void unsubscribeMarketData();
	void query(String command);
	public void getAllInstrument();
	
	void startTestSOD(String password);
	
//	void onInstrumentUpdate(IInstrument i);
	boolean isTradedSymbol(String symbol);
	boolean isReady();
}
