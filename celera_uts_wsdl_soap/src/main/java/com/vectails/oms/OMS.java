package com.vectails.oms;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;
import com.celera.core.dm.ITrade;

public class OMS implements IOMSListener
{
	final static Logger logger = LoggerFactory.getLogger(OMS.class);
	
	private Map<Long, IOrder> allOrders = new HashMap<Long, IOrder>();
	private Map<Long, ITrade> allTrades = new HashMap<Long, ITrade>();
	
	private Long tradeId = 0l;
	
	static private OMS INSTANCE = null;
	
	static synchronized public OMS instance() {
		if (INSTANCE == null)
			INSTANCE = new OMS();
		return INSTANCE;
	}
	
	public IOrder get(Long id) 
	{
		return allOrders.get(id);
	}

	@Override
	public void onOrder(IOrder o)
	{
		Long id = o.getId();
		allOrders.put(id, o);
		logger.info("onOrder: " + o.toString());		
	}

	@Override
	public void onQuote(IOrder o)
	{
		Long id = o.getId();
		if (!allOrders.containsKey(id))
		{
			allOrders.put(id, o);
		}
		logger.info("onQuote: " + o.toString());
	}

	@Override
	public void onTrade(ITrade o)
	{
		Long id = o.getId();
		if (id == null)
		{
			id = ++tradeId;
			o.setId(id);
		}
		allTrades.put(id, o);
		logger.info("onTrade: " + o.toString());		
	}
}
