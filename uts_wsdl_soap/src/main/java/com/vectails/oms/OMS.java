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
	private Map<String, ITrade> trades = new HashMap<String, ITrade>();

	static private OMS INSTANCE = null;

	static synchronized public OMS instance()
	{
		if (INSTANCE == null)
			INSTANCE = new OMS();
		return INSTANCE;
	}

	public IOrder get(Long id)
	{
		return allOrders.get(id);
	}

	public void onOrder(IOrder o)
	{
		Long id = o.getId();
		allOrders.put(id, o);
		logger.info("onOrder: " + o.toString());
	}

	public void onQuote(IOrder o)
	{
		Long id = o.getId();
		if (!allOrders.containsKey(id))
		{
			allOrders.put(id, o);
		}
		logger.info("onQuote: " + o.toString());
	}

	public void onTrade(ITrade o)
	{
		String id = o.getId();
		ITrade t = trades.put(id, o);
		if (t != null)
		{
			logger.info("onTrade update: " + o.toString());
		}
		else
		{
			logger.info("onTrade new: " + o.toString());
		}
	}
}
