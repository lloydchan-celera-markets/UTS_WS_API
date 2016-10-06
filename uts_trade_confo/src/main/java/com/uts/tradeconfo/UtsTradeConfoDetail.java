package com.uts.tradeconfo;

import java.util.ArrayList;
import java.util.List;

public class UtsTradeConfoDetail
{
	String summary = null;
	String buyer = null;
	String seller = null;
	String price = null;
	String curncy = null;
	String tradeDate = null;
	String refPrice = null;
	String id = null;
	String delta = null;
	String buyQty = null;
	String sellQty = null;
	String ptValue = null;
	String ptCny = null;
	String premiumPmt = null;
	String notational = null;
	String notationalCny = null;
	String rate = null;
	String premium = null;
	String premiumCny = null;
	List<String> legs = new ArrayList<String>();
	String hedge = null;
	String hedgeFutRef = null;
	String brokerageFee = null;
	String brokerageCny = null;

	@Override
	public String toString()
	{
		return "Trade [summary=" + summary + ", buyer=" + buyer + ", seller=" + seller + ", price=" + price
				+ ", curncy=" + curncy + ", tradeDate=" + tradeDate + ", refPrice=" + refPrice + ", id=" + id
				+ ", delta=" + delta + ", buyQty=" + buyQty + ", sellQty=" + sellQty + ", ptValue=" + ptValue
				+ ", ptCny=" + ptCny + ", premiumPmt=" + premiumPmt + ", notational=" + notational + ", notationalCny="
				+ notationalCny + ", rate=" + rate + ", premium=" + premium + ", premiumCny=" + premiumCny + ", legs="
				+ legs + ", hedge=" + hedge + ", hedgeFutRef=" + hedgeFutRef + ", brokerageFee=" + brokerageFee
				+ ", brokerageCny=" + brokerageCny + "]";
	}

	public void parsePdf(String sPdf)
	{
		boolean isHedge = false;
		boolean isFees = false;
		boolean isSummary = false;

		String[] lines = sPdf.split("\n");
		int len = lines.length;
		for (int i = 0; i < len; i++)
		{
			String s = lines[i];
			if (s.startsWith("SUMMARY"))
			{
				isSummary = true;
			}
			else if (s.startsWith("BUYER"))
			{
				isSummary = false;
				this.buyer = s.substring(6);
			}
			else if (s.startsWith("SELLER"))
			{
				isSummary = false;
				this.seller = s.substring(7);
			}
			else if (s.startsWith("PRICE"))
			{
				String[] tokens = s.split(" ");
				this.curncy = tokens[1].substring(0, 3);
				this.price = tokens[1].substring(3);

				if (tokens.length > 3)
				{
					this.tradeDate = tokens[3];
				}
			}
			else if (s.startsWith("REF"))
			{
				String[] tokens = s.split(" ");
				this.refPrice = tokens[1];
				if (tokens.length > 3)
				{
					this.id = tokens[3];
				}
			}
			else if (s.startsWith("DELTA"))
			{
				this.delta = s.substring(6);
			}
			else if (s.startsWith("SIZE(PTVALUE)"))
			{
				String[] tokens = s.split("\\s|\\(|\\)|/");
				// String[] tokens2 = tokens[1].split("");
				String qty = tokens[3];
				String[] qtys = qty.split("x");
				this.buyQty = qtys[0];
				this.sellQty = qtys[1];
				String sPtValue = tokens[4];
				int pos = sPtValue.length() - 3;
				this.ptCny = sPtValue.substring(pos);
				this.ptValue = sPtValue.substring(0, pos);

				if (tokens.length > 7)
				{
					this.premiumPmt = tokens[8];
				}
			}
			else if (s.startsWith("NOTIONAL"))
			{
				String[] tokens = s.split(" ");
				this.notationalCny = tokens[1].substring(0, 3);
				this.notational = tokens[1].substring(3);
				if (tokens.length > 3)
				{
					this.curncy = tokens[2].substring(0, 7);
					this.rate = tokens[3];
				}
			}
			else if (s.startsWith("PREMIUM"))
			{
				this.premiumCny = s.substring(8, 11);
				this.premium = s.substring(11);
			}
			else if (s.startsWith("Leg"))
			{
				this.legs.add(s);
			}
			else if (s.startsWith("HEDGE"))
			{
				isHedge = true;
			}
			else if (s.startsWith("FEES"))
			{
				isHedge = false;
				isFees = true;
			}
			else if (s.startsWith("Termswillbedefinedasperexchangerulesandregulations"))
			{
				return;
			}
			else if (isSummary)
			{
				this.summary = s;
				isSummary = false;
			}
			else
			{
				if (isHedge)
				{
					String[] tokens = s.split(";|=");
					this.hedge = tokens[0];
					this.hedgeFutRef = tokens[2];
				}
				else if (isFees & s.startsWith("BROKERAGEFEE"))
				{
					this.brokerageCny = s.substring(13, 16);
					this.brokerageFee = s.substring(16);
					isFees = false;
				}
			}
		}
	}
}