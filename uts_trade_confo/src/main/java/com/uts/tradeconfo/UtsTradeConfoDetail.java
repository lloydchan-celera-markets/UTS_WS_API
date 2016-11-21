package com.uts.tradeconfo;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.mongo.entity.Hedge;
import com.celera.mongo.entity.Leg;
import com.celera.mongo.entity.TradeConfo;
import com.uts.tools.Uts2Dm;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.celera.mongo.entity.Hedge;
//import com.celera.mongo.entity.Leg;
//import com.celera.mongo.entity.TradeConfo;
//import com.uts.tools.Uts2Dm;

public class UtsTradeConfoDetail 
{
	Logger logger = LoggerFactory.getLogger(UtsTradeConfoDetail.class);
	
    NumberFormat format = NumberFormat.getInstance();
//	static final String fmt = "dd-mmm-YY";
//	static final SimpleDateFormat sdf = new SimpleDateFormat(fmt);
//	static final NumberFormat nfmt = NumberFormat.getInstance();
	
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
	List<Leg> legs = new ArrayList<Leg>();
	List<Hedge> hedges = new ArrayList<Hedge>();
//	List<String> hedgeFutRef = null;
	String brokerageFee = null;
	String brokerageCny = null;
	String file = null;

	private static final List<String> futs = new ArrayList<String>(); 
	
	static {
		futs.add("KS200");
		futs.add("HSCEI");
		futs.add("HSI");
	}
	
	@Override
	public String toString()
	{
		return "UtsTradeConfoDetail [logger=" + logger + ", format=" + format + ", summary=" + summary + ", buyer="
				+ buyer + ", seller=" + seller + ", price=" + price + ", curncy=" + curncy + ", tradeDate=" + tradeDate
				+ ", refPrice=" + refPrice + ", id=" + id + ", delta=" + delta + ", buyQty=" + buyQty + ", sellQty="
				+ sellQty + ", ptValue=" + ptValue + ", ptCny=" + ptCny + ", premiumPmt=" + premiumPmt + ", notational="
				+ notational + ", notationalCny=" + notationalCny + ", rate=" + rate + ", premium=" + premium
				+ ", premiumCny=" + premiumCny + ", legs=" + legs + ", hedges=" + hedges + ", brokerageFee="
				+ brokerageFee + ", brokerageCny=" + brokerageCny + ", file=" + file + "]";
	}

	public void parsePdf1(String sPdf)
	{
		String s = null;
		try
		{
			boolean isHedge = false;
			boolean isFees = false;
			boolean isSummary = false;
			
			String[] lines = sPdf.split("\n");
			int len = lines.length;
			for (int i = 0; i < len; i++)
			{
				s = lines[i];
				if (s.startsWith("SUMMARY"))
				{
					isSummary = true;
				}
				else if (s.startsWith("BUYER"))
				{
					isSummary = false;
					this.buyer = s.substring(6);
				}
				else if (s.startsWith(" BUYER"))
				{
					isSummary = false;
					this.buyer = s.substring(7);
				}
				else if (s.startsWith("SELLER"))
				{
					isSummary = false;
					this.seller = s.substring(7);
				}
				else if (s.startsWith(" SELLER"))
				{
					isSummary = false;
					this.seller = s.substring(8);
				}
				else if (s.startsWith("PRICE"))
				{
					String[] tokens = s.split(" ");
					
					try 
					{
						format.parse(tokens[2]);
					}
					catch (Exception e)
					{
						continue;
					}
					this.curncy = tokens[1];
					this.price = tokens[2];
				}
				else if (s.startsWith(" PRICE"))
				{
					String[] tokens = s.split(" ");
					this.curncy = tokens[2];
					this.price = tokens[3];
				}
				else if (s.startsWith("REF"))
				{
					this.refPrice = s.substring(4);
					if (s.indexOf("TRADE ID") > 0)
					{
						int k = s.indexOf("TRADE ID") + "TRADE ID".length();
						this.id = s.substring(k).trim();
					}
//					System.out.println(this.price);
//					System.out.println("id=null sPdf=" + this.id);
					
				}
				else if (s.startsWith("DELTA"))
				{
					this.delta = s.substring(6);
				}
				else if (s.startsWith("TRADE DATE"))
				{
					this.tradeDate = s.substring(11);
				}
				else if (s.startsWith("PREMIUM PAYMENT"))
				{
					this.premiumPmt = s.substring(16);
				}
				else if (s.startsWith("PREMIUM"))
				{
					try {
						this.premiumCny = s.substring(8, 11);
						this.premium = s.substring(12);	
					}
					catch (Exception e)
					{
//						e.printStackTrace();
					}
					
				}
				else if (s.startsWith("TRADE ID"))
				{
					this.id = s.substring(9);
				}
				else if (s.startsWith("SIZE (PT VALUE)"))
				{
					String[] tokens = s.substring(16).split("\\s|\\(|\\)|/");
					if (tokens[0].contains("x"))
					{
						String[] qtys = tokens[0].split("x");
//if ("CELERAEQ-2016-13046".equals(id)) {
//System.out.println(this.toString());
//}
						if (this.buyer != null && this.seller != null)
						{

							if (this.buyer.indexOf("-") > 0)
							{
								this.seller = null;
							} else if (this.seller.indexOf("-") > 0)
							{
								this.buyer = null;
							}
						}
					}
					else
					{
						if (this.buyer != null && this.seller != null)
						{
							if (this.buyer.indexOf("-") > 0)
							{
								this.seller = null;
							} else if (this.seller.indexOf("-") > 0)
							{
								this.buyer = null;
							}
						} 
					}
					try
					{
						this.ptCny = tokens[3];
						this.ptValue = tokens[2];
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else if (s.startsWith("NOTIONAL"))
				{
					String[] tokens = s.split(" ");
					try {
					this.notationalCny = tokens[1];
					this.notational = tokens[2];
					}
					catch (Exception e){
//						System.out.println("s=" + s);
					}
	//				if (tokens.length > 3)
	//				{
	//					this.curncy = tokens[2].substring(0, 7);
	//					this.rate = tokens[3];
	//				}
				}
				else if (s.startsWith("Leg"))
				{
					String[] tokens = s.split(" ");
					int lenLeg = tokens.length;
					
					String side = tokens[2];
					Double size = Uts2Dm.toDouble(tokens[3]);
					String expiry = tokens[4];
					String strike = tokens[5];
					String product = "";
					for (int j = 6; j<lenLeg-2; j++) {
						product += tokens[j] + " ";
					}
					String price = tokens[lenLeg - 2];
					String premium = tokens[lenLeg - 1];
					
					this.legs.add(new Leg(side, size, expiry, strike, price, premium, product));
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
	//			else if (s.startsWith("Terms will be defined as per exchange rules and regulations"))
	//			{
	//				return;
	//			}
				else if (isSummary)
				{
					this.summary = s;
					isSummary = false;
				}
				else if (s.startsWith("Syn "))
				{
					String[] tokens = s.split(" ");
					int lenLeg = tokens.length;
					
					String side = tokens[2];
					Double size = Uts2Dm.toDouble(tokens[3]);
					String expiry = tokens[4];
					String strike = tokens[5];
					String product = "";
					for (int j = 6; j<lenLeg-2; j++) {
						product += tokens[j] + " ";
					}
					String price = tokens[lenLeg - 2];
					String premium = tokens[lenLeg - 1];
					
					Hedge h = new Hedge();
					h.setSide(side);
					h.setQty(size);
					h.setPrice(Uts2Dm.toDouble(price));
					h.setFuture(product + " " + expiry + " " + strike + " " + premium);
					this.hedges.add(h);
				}
				else
				{
					if (isHedge)
					{
						String trim = s.toUpperCase()
								.replaceAll(" ", "")
								.replace("FUTURES(", ";")
								.replace(");FUTUREREFERENCE=", ";")
								.replace(":AVERAGEEXECUTEDPRICE=", ";");
//						System.out.println(trim);
						
						if (s.startsWith("Hedge settlement as per exchange rules") || 
								s.startsWith("Trade hedge with synthetic forward"))
						{}
						else
//						else if (!s.contains(" "))
						{
							
							try
							{
//							String trim = s.replaceAll(" ", "");
							boolean isBuy = false;
							boolean hasAt = false;
							
							if (trim.startsWith("YOUBUY")) {
								trim = trim.replace("YOUBUY", "");
								isBuy = true;
							}
							else if (trim.startsWith("BUY")) {
								trim = trim.replace("BUY", "");
								isBuy = true;
							}
							else if (trim.startsWith("YOUSELL")) {
								trim = trim.replace("YOUSELL", "");
							}
							else if (trim.startsWith("SELL")) {
								trim = trim.replace("SELL", "");
							}
							
							if (trim.contains("@"))
								hasAt = true;
							
							String side = null, qty = null, fut = null, price = null;
							if (hasAt) {
								String _fut = isFut(trim); 
								if (_fut != null)
								{
									trim = trim.replace(_fut, ";").replace("FUTURE@", ";").replace("C@", ";").replace("P@", ";");
									// 5;JUN16242.5;4.90
									String[] tokens = trim.split(";");
									side = isBuy ? "Buy" : "Sell";
									qty = tokens[0];
									fut = _fut + " " + tokens[1].substring(0, 5);
									price = tokens[2];
								}
							}
							else {
									String[] tokens = trim.split(";");
									side = isBuy ? "Buy" : "Sell";
									qty = tokens[0];
									int pos = tokens[1].length() - 5;
									fut = tokens[1].substring(0, pos) + " " + tokens[1].substring(pos);
									price = tokens[2];
							}
							
//System.out.println("testing=" + side +"," + qty + "," + fut + "," + price);
							
								Hedge h = new Hedge();
								h.setSide(side);
								h.setQty(Uts2Dm.toDouble(qty));
								h.setPrice(Uts2Dm.toDouble(price));
								h.setFuture(fut);
								this.hedges.add(h);
							}
							catch (Exception e) {
//								System.out.println("s=" + s);
//								e.printStackTrace();
							}
						}
					}
					else if (isFees && s.startsWith("BROKERAGE FEE"))
					{
						int offset = 0;
						if (s.startsWith(" "))
							offset++;
						
						this.brokerageCny = s.substring(14 + offset, 17 + offset);
						this.brokerageFee = s.substring(18 + offset);
						isFees = false;
						
						Double buyQty = 0d;
						Double sellQty = 0d;
						for (Leg leg : legs) 
						{
							if ("Buy".equals(leg.getSide()))
							{
								buyQty += Double.valueOf(leg.getQty()); 
							}
							else 
							{
								sellQty += Double.valueOf(leg.getQty());
							}
						}
						for (Hedge h : hedges) 
						{
							if ("Buy".equals(h.getSide()))
							{
								buyQty += h.getQty(); 
							}
							else 
							{
								sellQty += h.getQty();
							}
						}
						if (buyQty > 0d)
							this.buyQty = buyQty.toString();
						if (sellQty > 0d)
							this.sellQty = sellQty.toString();
					}
					else if (s.startsWith(" BROKERAGE FEE"))
					{
						int offset = 0;
						if (s.startsWith(" "))
							offset++;
						
						this.brokerageCny = s.substring(14 + offset, 17 + offset);
						this.brokerageFee = s.substring(18 + offset);
					}
					else if (s.contains("RATE")) {
						this.rate = s.substring(13);
					}
				}
			}
			
if ("CELERAEQ-2016-12813".equals(this.id)) {
	System.out.println(this.toString());
	System.out.println("sPdf=" + sPdf);
}

			
			if (this.buyer == null && this.seller == null)
			{
if (this.id.equals("CELERAEQ-2016-12878"))
{
	System.out.println(this.toString());
	if (this.brokerageFee.equals("1,200"))
		this.buyer = "Nomura International Plc - Nadjib Ezziane";
	else {
		this.seller = "Eclipse Futures (HK) Limited - Traders";
	}
}
				System.out.println("buyer,seller=null sPdf=" + sPdf);
			}
			else if (this.buyer != null && this.seller != null)
			{
logger.debug("============ buyer and seller both exist[start]============ {}", this.toString());
if (legs.get(0).getSide().equals("Sell"))
{
	this.buyer = null;
}
else {
	this.seller = null;
}
logger.debug("============ buyer and seller both exist[end]============ {}", this.toString());
//				if (this.buyer.indexOf("-") > 0)
//				{
//					this.seller = null;
//				} else if (this.seller.indexOf("-") > 0)
//				{
//					this.buyer = null;
//				}
			} 
			if ("Celera Bank Private Test 1 - james Hugh".equals(this.seller)) {
//System.out.println("========Celera Bank Private Test 1 - james Hugh============" + this.id);			
this.seller = "Thierry";
			}
			if (this.tradeDate == null )
			{
				if (sPdf.indexOf("TRADE DATE") > 0)
				{
					int k = sPdf.indexOf("TRADE DATE ") + "TRADE DATE ".length();
					this.tradeDate = sPdf.substring(k, k+9).trim();
				}
//				System.out.println("sPdf=" + this.buyer + "," + this.seller +"," + this.tradeDate);
				if (this.tradeDate == null || this.tradeDate.length() > 15)
					System.out.println("tradeDate=null sPdf=" + sPdf);
			}
			if (this.brokerageFee == null)
			{
				System.out.println("brokerageFee sPdf=" + sPdf);
			}
			if (this.price == null || this.price.startsWith("DATE"))
			{
//				System.out.println(this.price);
//				System.out.println("sPdf=" + sPdf);
			}
			if (this.id == null)
			{
				if (sPdf.indexOf("TRADE ID") > 0)
				{
					int k = sPdf.indexOf("TRADE ID ") + "TRADE ID ".length();
					this.id = sPdf.substring(k, k+19).trim();
				}
//				System.out.println(this.price);
//				System.out.println("id=null sPdf=" + this.id);
			}
			
			if (this.curncy == null && this.brokerageCny == null && this.notationalCny == null)
			{
//				System.out.println(this.price);
				System.out.println("curncy=null sPdf=" + sPdf);
			}
//			System.out.println("brokerageFee=" + this.brokerageFee);
//if ("CELERAEQ-2016-13046".equals(id)) {
//	System.out.println(this.toString());
//}
			
			
			// handle special case
			if ("CELERAEQ-2016-12802".equals(this.id) 
					|| "CELERAEQ-2016-12800".equals(this.id)
					|| "CELERAEQ-2016-12803".equals(this.id)
					|| "CELERAEQ-2016-12804".equals(this.id)
					|| "CELERAEQ-2016-12808".equals(this.id)
					|| "CELERAEQ-2016-12813".equals(this.id)
					|| "CELERAEQ-2016-12814".equals(this.id)
					|| "CELERAEQ-2016-12821".equals(this.id)
					|| "CELERAEQ-2016-12823".equals(this.id)
					|| "CELERAEQ-2016-12900".equals(this.id)
					|| "CELERAEQ-2016-13016".equals(this.id)
					)
			{
				if (this.buyer != null && this.seller != null) {
					if (legs.get(0).getSide().equals("Buy"))
						this.seller = null;
					else
						this.buyer = null;
				}
if ("CELERAEQ-2016-12813".equals(this.id)) {
	logger.debug("s={} sPdf={}", s, sPdf);
}
			}
			
		}
		catch (Exception e)
		{
logger.error("s={} sPdf={}", s, sPdf, e);
//			logger.error("Error s=[{}] sPdf=[{}]", s, sPdf, e);
		}
	}

	public void parsePdf(String sPdf)
	{
		String s = null;
		try
		{
			boolean isHedge = false;
			boolean isFees = false;
			boolean isSummary = false;
			
			String[] lines = sPdf.split("\n");
			int len = lines.length;
			String firm = null;
			String participant = null;
			for (int i = 0; i < len; i++)
			{
				s = lines[i];
				if (s.startsWith("To: ") || s.startsWith(" To: ")) 
				{
					s = s.replace(" To: ", "").replace("To: ", "");
					try {
						int idx = s.indexOf(" <");
						if (idx > 0) {
							String[] tokens = s.split(" <");
							participant  = tokens[0];
							firm = participant.split(" - ")[0];

						} else {
							// System.out.println(sPdf);
							participant = s;
							firm = s.split(" - ")[0];

						}
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
				else if (s.startsWith("SUMMARY"))
				{
					isSummary = true;
				}
				else if (s.startsWith("BUYER ") || s.startsWith(" BUYER "))
				{
					s = s.replace(" BUYER ", "").replace("BUYER ", "");
					isSummary = false;
					String buyer = s;
					if (buyer.indexOf(firm) >= 0) {
						this.buyer = participant;
//System.out.println("==========buyer1======" + participant + "," + firm);
					}
					else {
//System.out.println("==========buyer2======" + buyer + "," + firm);
					}
				}
				else if (s.startsWith("SELLER ") || s.startsWith(" SELLER "))
				{
					s = s.replace(" SELLER ", "").replace("SELLER ", "");
					isSummary = false;
					String seller = s;
					if (seller.indexOf(firm) >= 0) {
						this.seller = participant;
//System.out.println("==========seller1======" + participant + "," + firm);
					}
					else {
//System.out.println("==========seller2======" + seller + "," + firm);
					}
				}
				else if (s.startsWith("PRICE"))
				{
					String[] tokens = s.split(" ");
					
					try 
					{
						format.parse(tokens[2]);
					}
					catch (Exception e)
					{
						continue;
					}
					this.curncy = tokens[1];
					this.price = tokens[2];
				}
				else if (s.startsWith(" PRICE"))
				{
					String[] tokens = s.split(" ");
					this.curncy = tokens[2];
					this.price = tokens[3];
				}
				else if (s.startsWith("REF"))
				{
					this.refPrice = s.substring(4);
					if (s.indexOf("TRADE ID") > 0)
					{
						int k = s.indexOf("TRADE ID") + "TRADE ID".length();
						this.id = s.substring(k).trim();
					}
//					System.out.println(this.price);
//					System.out.println("id=null sPdf=" + this.id);
					
				}
				else if (s.startsWith("DELTA"))
				{
					this.delta = s.substring(6);
				}
				else if (s.startsWith("TRADE DATE"))
				{
					this.tradeDate = s.substring(11);
				}
				else if (s.startsWith("PREMIUM PAYMENT"))
				{
					this.premiumPmt = s.substring(16);
				}
				else if (s.startsWith("PREMIUM"))
				{
					try {
						this.premiumCny = s.substring(8, 11);
						this.premium = s.substring(12);	
					}
					catch (Exception e)
					{
//						e.printStackTrace();
					}
					
				}
				else if (s.startsWith("TRADE ID"))
				{
					this.id = s.substring(9);
				}
				else if (s.startsWith("SIZE (PT VALUE)"))
				{
					String[] tokens = s.substring(16).split("\\s|\\(|\\)|/");
					if (tokens[0].contains("x"))
					{
						String[] qtys = tokens[0].split("x");
//if ("CELERAEQ-2016-13046".equals(id)) {
//System.out.println(this.toString());
//}
//						if (this.buyer != null && this.seller != null)
//						{
//							if (this.buyer.indexOf("-") > 0)
//							{
//								this.seller = null;
//							} else if (this.seller.indexOf("-") > 0)
//							{
//								this.buyer = null;
//							}
//						}
					}
					else
					{
//						if (this.buyer != null && this.seller != null)
//						{
//							if (this.buyer.indexOf("-") > 0)
//							{
//								this.seller = null;
//							} else if (this.seller.indexOf("-") > 0)
//							{
//								this.buyer = null;
//							}
//						} 
					}
					try
					{
						this.ptCny = tokens[3];
						this.ptValue = tokens[2];
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else if (s.startsWith("NOTIONAL"))
				{
					String[] tokens = s.split(" ");
					try {
						this.notationalCny = tokens[1];
						this.notational = tokens[2];
					}
					catch (Exception e){
//						System.out.println("s=" + s);
					}
					//				if (tokens.length > 3)
					//				{
					//					this.curncy = tokens[2].substring(0, 7);
					//					this.rate = tokens[3];
					//				}
				}
				else if (s.startsWith("Leg"))
				{
					String[] tokens = s.split(" ");
					int lenLeg = tokens.length;
					
					String side = tokens[2];
					Double size = Uts2Dm.toDouble(tokens[3]);
					String expiry = tokens[4];
					String strike = tokens[5];
					String product = "";
					for (int j = 6; j<lenLeg-2; j++) {
						product += tokens[j] + " ";
					}
					String price = tokens[lenLeg - 2];
					String premium = tokens[lenLeg - 1];
					
					this.legs.add(new Leg(side, size, expiry, strike, price, premium, product));
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
				//			else if (s.startsWith("Terms will be defined as per exchange rules and regulations"))
				//			{
				//				return;
				//			}
				else if (isSummary)
				{
					this.summary = s;
					isSummary = false;
				}
				else if (s.startsWith("Syn "))
				{
					String[] tokens = s.split(" ");
					int lenLeg = tokens.length;
					
					String side = tokens[2];
					Double size = Uts2Dm.toDouble(tokens[3]);
					String expiry = tokens[4];
					String strike = tokens[5];
					String product = "";
					for (int j = 6; j<lenLeg-2; j++) {
						product += tokens[j] + " ";
					}
					String price = tokens[lenLeg - 2];
					String premium = tokens[lenLeg - 1];
					
					Hedge h = new Hedge();
					h.setSide(side);
					h.setQty(size);
					h.setPrice(Uts2Dm.toDouble(price));
					h.setFuture(product + " " + expiry + " " + strike + " " + premium);
					this.hedges.add(h);
				}
				else
				{
					if (isHedge)
					{
						String trim = s.toUpperCase()
								.replaceAll(" ", "")
								.replace("FUTURES(", ";")
								.replace(");FUTUREREFERENCE=", ";")
								.replace(":AVERAGEEXECUTEDPRICE=", ";");
//						System.out.println(trim);
						
						if (s.startsWith("Hedge settlement as per exchange rules") || 
								s.startsWith("Trade hedge with synthetic forward"))
						{}
						else
//						else if (!s.contains(" "))
						{
							
							try
							{
//							String trim = s.replaceAll(" ", "");
								boolean isBuy = false;
								boolean hasAt = false;
								
								if (trim.startsWith("YOUBUY")) {
									trim = trim.replace("YOUBUY", "");
									isBuy = true;
								}
								else if (trim.startsWith("BUY")) {
									trim = trim.replace("BUY", "");
									isBuy = true;
								}
								else if (trim.startsWith("YOUSELL")) {
									trim = trim.replace("YOUSELL", "");
								}
								else if (trim.startsWith("SELL")) {
									trim = trim.replace("SELL", "");
								}
								
								if (trim.contains("@"))
									hasAt = true;
								
								String side = null, qty = null, fut = null, price = null;
								if (hasAt) {
									String _fut = isFut(trim); 
									if (_fut != null)
									{
										trim = trim.replace(_fut, ";").replace("FUTURE@", ";").replace("C@", ";").replace("P@", ";");
										// 5;JUN16242.5;4.90
										String[] tokens = trim.split(";");
										side = isBuy ? "Buy" : "Sell";
										qty = tokens[0];
										fut = _fut + " " + tokens[1].substring(0, 5);
										price = tokens[2];
									}
								}
								else {
									String[] tokens = trim.split(";");
									side = isBuy ? "Buy" : "Sell";
									qty = tokens[0];
									int pos = tokens[1].length() - 5;
									fut = tokens[1].substring(0, pos) + " " + tokens[1].substring(pos);
									price = tokens[2];
								}
								
//System.out.println("testing=" + side +"," + qty + "," + fut + "," + price);
								
								Hedge h = new Hedge();
								h.setSide(side);
								h.setQty(Uts2Dm.toDouble(qty));
								h.setPrice(Uts2Dm.toDouble(price));
								h.setFuture(fut);
								this.hedges.add(h);
							}
							catch (Exception e) {
//								System.out.println("s=" + s);
//								e.printStackTrace();
							}
						}
					}
					else if (isFees && s.startsWith("BROKERAGE FEE"))
					{
						int offset = 0;
						if (s.startsWith(" "))
							offset++;
						
						this.brokerageCny = s.substring(14 + offset, 17 + offset);
						this.brokerageFee = s.substring(18 + offset);
						isFees = false;
						
						Double buyQty = 0d;
						Double sellQty = 0d;
						for (Leg leg : legs) 
						{
							if ("Buy".equals(leg.getSide()))
							{
								buyQty += Double.valueOf(leg.getQty()); 
							}
							else 
							{
								sellQty += Double.valueOf(leg.getQty());
							}
						}
						for (Hedge h : hedges) 
						{
							if ("Buy".equals(h.getSide()))
							{
								buyQty += h.getQty(); 
							}
							else 
							{
								sellQty += h.getQty();
							}
						}
						if (buyQty > 0d)
							this.buyQty = buyQty.toString();
						if (sellQty > 0d)
							this.sellQty = sellQty.toString();
					}
					else if (s.startsWith(" BROKERAGE FEE"))
					{
						int offset = 0;
						if (s.startsWith(" "))
							offset++;
						
						this.brokerageCny = s.substring(14 + offset, 17 + offset);
						this.brokerageFee = s.substring(18 + offset);
					}
					else if (s.contains("RATE")) {
						this.rate = s.substring(13);
					}
				}
			}
			
			// missing mandatory values
			if (this.id == null)
			{
				if (sPdf.indexOf("TRADE ID") > 0)
				{
					int k = sPdf.indexOf("TRADE ID ") + "TRADE ID ".length();
					this.id = sPdf.substring(k, k+19).trim();
				}
			}
			if (this.tradeDate == null )
			{
				if (sPdf.indexOf("TRADE DATE") > 0)
				{
					int k = sPdf.indexOf("TRADE DATE ") + "TRADE DATE ".length();
					this.tradeDate = sPdf.substring(k, k+9).trim();
				}
//				System.out.println("sPdf=" + this.buyer + "," + this.seller +"," + this.tradeDate);
				if (this.tradeDate == null || this.tradeDate.length() > 15)
					System.out.println("tradeDate=null sPdf=" + sPdf);
			}
			
			// handle special case
			if (this.buyer == null && this.seller == null)
			{
				if (this.id.equals("CELERAEQ-2016-12878"))
				{
//					System.out.println(this.toString());
					if (this.brokerageFee.equals("1,200"))
						this.buyer = "Nomura International Plc - Nadjib Ezziane";
					else {
						this.seller = "Eclipse Futures (HK) Limited - Traders";
					}
				}
logger.error("buyer,seller=null sPdf={}", sPdf);
			}
			if ("Celera Bank Private Test 1 - james Hugh".equals(this.seller)) {
//System.out.println("========Celera Bank Private Test 1 - james Hugh============" + this.id);			
				this.seller = "Thierry";
			}
			if (this.brokerageFee == null)
			{
				System.out.println("brokerageFee sPdf=" + sPdf);
			}
			if (this.price == null || this.price.startsWith("DATE"))
			{
//				System.out.println(this.price);
//				System.out.println("sPdf=" + sPdf);
			}
			
			if (this.curncy == null && this.brokerageCny == null && this.notationalCny == null)
			{
//				System.out.println(this.price);
logger.error("curncy=null sPdf={}", sPdf);
			}
		}
		catch (Exception e)
		{
logger.error("s={} sPdf={}", s, sPdf, e);
//			logger.error("Error s=[{}] sPdf=[{}]", s, sPdf, e);
		}
	}
	
	private static String isFut(String hedge)
	{
		for (String fut: futs)
		{
			if (hedge.contains(fut))
				return fut;
		}
		return null;
	}
	
	public String getSummary()
	{
		return summary;
	}

	public String getBuyer()
	{
		return buyer;
	}

	public String getSeller()
	{
		return seller;
	}

	public String getPrice()
	{
		return price;
	}

	public String getCurncy()
	{
		if (curncy == null) {
			if (this.brokerageCny == null)
				return this.notationalCny;
			return this.brokerageCny;
		}
		return curncy;
	}

	public String getTradeDate()
	{
		return tradeDate;
	}

	public String getRefPrice()
	{
		return refPrice;
	}

	public String getId()
	{
		return id;
	}

	public String getDelta()
	{
		return delta;
	}

	public String getBuyQty()
	{
		return buyQty;
	}

	public String getSellQty()
	{
		return sellQty;
	}

	public String getPtValue()
	{
		return ptValue;
	}

	public String getPtCny()
	{
		return ptCny;
	}

	public String getPremiumPmt()
	{
		return premiumPmt;
	}

	public String getNotational()
	{
		return notational;
	}

	public String getNotationalCny()
	{
		return notationalCny;
	}

	public String getRate()
	{
		return rate;
	}

	public String getPremium()
	{
		return premium;
	}

	public String getPremiumCny()
	{
		return premiumCny;
	}

	public List<Leg> getLegs()
	{
		return legs;
	}

	public List<Hedge> getHedges()
	{
		return hedges;
	}

//	public String getHedgeFutRef()
//	{
//		return hedgeFutRef;
//	}

	public String getBrokerageFee()
	{
		return brokerageFee;
	}

	public String getBrokerageCny()
	{
		return brokerageCny;
	}
	
	public String getFile()
	{
		return file;
	}

	public void setFile(String file)
	{
		this.file = file;
	}

	public TradeConfo convert() 
	{
		TradeConfo to = new TradeConfo();
		to.setSummary(this.summary);
		to.setBuyer(this.buyer);
		to.setSeller(this.seller);
		to.setPrice(Uts2Dm.toDouble(this.price));
		to.setCurncy(this.curncy);
		to.setTradeDate(Uts2Dm.toDate(this.tradeDate));
		to.setRefPrice(Uts2Dm.toDouble(this.refPrice));
		to.setTradeConfoId(this.id);
		to.setDelta(this.delta);
		to.setBuyQty(Uts2Dm.toDouble(this.buyQty));
		to.setSellQty(Uts2Dm.toDouble(this.sellQty));
		to.setPtValue(Uts2Dm.toDouble(this.ptValue));
		to.setPtCny(this.ptCny);
		to.setPremiumPmt(this.premiumPmt);
		to.setNotational(Uts2Dm.toDouble(this.notational));
		to.setNotationalCny(this.notationalCny);
		to.setRate(Uts2Dm.toDouble(this.rate));
		to.setPremium(Uts2Dm.toDouble(this.premium));
		to.setPremiumCny(this.premiumCny);
//		to.setHedgeFutRef(Uts2Dm.toDouble(this.hedgeFutRef));
		to.setBrokerageFee(Uts2Dm.toDouble(this.brokerageFee));
		to.setBrokerageCny(this.brokerageCny);
		to.setFile(this.getFile());
		
		List<Hedge> hedges = to.getHedges();
		hedges.addAll(this.hedges);
		List<Leg> legs = to.getLegs();
		legs.addAll(this.legs);
		
		to.setLastModified(new Date(System.currentTimeMillis()));
		
		return to;
	}
	
	public static void main(String[] args)
	{
//		String s = "Buy 5 FUTURES (HSCEI OCT16); Future Reference = 9,950";
//		String[] tokens = s.split("\\s|;|=|\\(|\\)");
		
//		String s = "Youbuy1Futures(HSCEIAPR16);FutureReference=8855";
//		s = s.replace("Youbuy", "").replace("Futures(", ",").replace(");FutureReference=", ",");
		
		String s = "You buy5KS200JUN16242.5C@4.90";
		s = s.replace("Youbuy", "").replace("Futures(", ",").replace(");FutureReference=", ",");
		
		System.out.println(s);
//		String[] tokens = s.split("Youbuy|Futures\\(|;|FutureReference=|\\)");
//		String[] tokens = s.split("\\(|;|=|\\)");
		String[] tokens = s.split(",");

		for (int i =0; i <tokens.length; i++)
		{
			System.out.println(i + "=" + tokens[i]);
		}
	}
	
}