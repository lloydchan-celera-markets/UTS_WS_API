package com.celera.backoffice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.celera.adapter.DatabaseAdapter;
import com.celera.mongo.entity.TradeConfo;

public class TCreateInvoice implements Runnable
{
	private static final String PRE_INVOICE_NUMBER = "CEL";
	private static int invoice_Num = 1;
	
	private final Date start; 
	private final Date end;
	
	public TCreateInvoice(Date start, Date end)
	{
		this.start = start;
		this.end = end;
	}
	
	public void run()
	{

		
		Map<String, List<TradeConfo>> client2TradeConfo = new HashMap<String, List<TradeConfo>>();

		// (client, currency) -> trade confo
		List<TradeConfo> dbList = DatabaseAdapter.getHistTradeConfo(start, end);
		String key;
		for (TradeConfo e : dbList)
		{
			String curncy = e.getCurncy();
			String buyer = e.getBuyer();
			if (buyer == null)
			{
				String seller = e.getSeller();
				key = seller + curncy;
			} else
			{
				key = buyer + curncy;
			}
			List temp = (List) client2TradeConfo.get(key);
			if (temp == null)
			{
				temp = new ArrayList<TradeConfo>();
				client2TradeConfo.put(key, temp);
			}
			temp.add(e);
		}
		
		// for each client
		for (Map.Entry<String, List<TradeConfo>> e: client2TradeConfo.entrySet())
		{
			String.format("CEL%04d", invoice_Num);
			// find client info
			List<TradeConfo> l = e.getValue();
		}
		
	}

}
