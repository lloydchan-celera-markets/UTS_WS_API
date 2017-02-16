package com.celera.core.mds;

public interface IMarketDataService
{
	public void onLastPrice(String symbol, Double price);
}
