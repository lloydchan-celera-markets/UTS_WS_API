package com.celera.core.mds;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.oms.OMS;
import com.celera.core.oms.OrderLoggerServer;
import com.celera.ipc.URL;

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
		
		Double getLastPrice(String symbol) {
			return lastPrice.get(symbol);
		}
	}
	
	private final BBOManager bboManager = new BBOManager();
	static private MarketDataService INSTANCE = null;
	
	private MarketDataService() {
	}
	
	static synchronized public MarketDataService instance()
	{
		if (INSTANCE == null)
			INSTANCE = new MarketDataService();
		return INSTANCE;
	}
	
	@Override
	public void onLastPrice(String symbol, Double price) {
		bboManager.onLastPrice(symbol, price);		
	}
	
	@Override
	public Double getLastPrice(String symbol) {
		return bboManager.getLastPrice(symbol);	
	}
	
	public static void main(String[] args)
	{
	}

}
