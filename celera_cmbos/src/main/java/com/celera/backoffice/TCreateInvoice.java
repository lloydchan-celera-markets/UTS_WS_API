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
	private static final String PATTERN = " - ";
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

		// (firm + currency) -> trade confo
		List<TradeConfo> dbList = DatabaseAdapter.getHistTradeConfo(start, end);
		String key;
		for (TradeConfo e : dbList)
		{
			String curncy = e.getCurncy();
			String buyer = e.getBuyer();
			String firm;
			if (buyer == null)
			{
				String seller = e.getSeller();
				int pos = seller.lastIndexOf(PATTERN);
				firm = (pos > 0) ? seller.substring(0,  pos) : seller;
				key = firm + curncy;
			} else
			{
				int pos = buyer.lastIndexOf(PATTERN);
				firm = (pos > 0) ? buyer.substring(0,  pos) : buyer;
				key = firm + curncy;
			}
			List temp = (List) client2TradeConfo.get(key);
			if (temp == null)
			{
				temp = new ArrayList<TradeConfo>();
				client2TradeConfo.put(key, temp);
			}
			temp.add(e);
		}

		String.format("CEL%04d", invoice_Num);
		// for each client
		for (Map.Entry<String, List<TradeConfo>> e: client2TradeConfo.entrySet())
		{
			// find client info
			List<TradeConfo> tradeConfo = e.getValue();
			TradeDetail tradeDetail = new TradeDetail();
			for (TradeConfo tc : e.getValue())
			{
				tradeDetail.setDate(tc.getTradeDate());	
			}
			
		}
		
	}

}
