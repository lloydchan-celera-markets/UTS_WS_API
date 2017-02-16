package com.celera.core.mds;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketDataService implements IMarketDataService
{
	private class BBOManager
	{
		Logger logger = LoggerFactory.getLogger(BBOManager.class);
		// bbo book
		
		// last price
		private Map<String, Double> lastPrice = new ConcurrentHashMap<String, Double>();
		
		void onLastPrice(String symbol, Double price) {
			Double old = lastPrice.put(symbol, price);
//			logger.info("update [{}] last price to [{}]", symbol, price);
//			logger.info("update [{}] last price old [{}] to new [{}]", symbol, old, price);
		}
	}
	
	private final BBOManager bboManager = new BBOManager();
	
	public MarketDataService() {
	}
	
	public void onLastPrice(String symbol, Double price) {
		bboManager.onLastPrice(symbol, price);		
	}
	
	public static void main(String[] args)
	{
	}

}
