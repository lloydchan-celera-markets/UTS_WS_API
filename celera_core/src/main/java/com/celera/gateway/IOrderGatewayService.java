package com.celera.gateway;

import java.util.List;

import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;

public interface IOrderGatewayService
{
	void createOrder(IOrder o);
	void modifyOrder(IOrder o);
	void cancelOrder(IOrder o);
	void createQuote(IQuote quote);
	void modifyQuote(IQuote q);
	void cancelQuote(IQuote q);
	void createTradeReport(IOrder o);
	void cancelTradeReport(IOrder o);
	void createBlockTradeReport(List<IOrder> tradeReportList, Long id, String synSymbol);
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
}
