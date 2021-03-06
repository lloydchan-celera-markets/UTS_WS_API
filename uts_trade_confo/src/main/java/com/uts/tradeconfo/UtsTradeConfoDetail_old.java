package com.uts.tradeconfo;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.mongo.entity.Hedge;
import com.celera.mongo.entity.Leg;
import com.celera.mongo.entity.TradeConfo;
import com.uts.tools.Uts2Dm;

public class UtsTradeConfoDetail_old 
{
//	static final String fmt = "dd-mmm-YY";
//	static final SimpleDateFormat sdf = new SimpleDateFormat(fmt);
//	static final NumberFormat nfmt = NumberFormat.getInstance();
	
	final static Logger logger = LoggerFactory.getLogger(UtsTradeConfoDetail.class);
	
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

	@Override
	public String toString()
	{
		return "UtsTradeConfoDetail [summary=" + summary + ", buyer=" + buyer + ", seller=" + seller + ", price="
				+ price + ", curncy=" + curncy + ", tradeDate=" + tradeDate + ", refPrice=" + refPrice + ", id=" + id
				+ ", delta=" + delta + ", buyQty=" + buyQty + ", sellQty=" + sellQty + ", ptValue=" + ptValue
				+ ", ptCny=" + ptCny + ", premiumPmt=" + premiumPmt + ", notational=" + notational + ", notationalCny="
				+ notationalCny + ", rate=" + rate + ", premium=" + premium + ", premiumCny=" + premiumCny + ", legs="
				+ legs + ", hedges=" + hedges + ", brokerageFee=" + brokerageFee + ", brokerageCny=" + brokerageCny + "]";
	}

	public void parsePdf(String sPdf)
	{
		try
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
				this.curncy = tokens[1];
				this.price = tokens[2];
			}
			else if (s.startsWith("REF"))
			{
				this.refPrice = s.substring(4);
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
				this.premiumCny = s.substring(8, 11);
				this.premium = s.substring(12);
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
					if (this.buyer != null && this.seller != null)
					{
						if (this.buyer.contains("-"))
						{
							this.seller = null;
						} else if (this.seller.contains("-"))
						{
							this.buyer = null;
						}
					}
				}
				else
				{
					if (this.buyer != null && this.seller != null)
					{
						if (this.buyer.contains("-"))
						{
							this.seller = null;
						} else if (this.seller.contains("-"))
						{
							this.buyer = null;
						}
					} 
				}
				this.ptCny = tokens[3];
				this.ptValue = tokens[2];
			}
			else if (s.startsWith("NOTIONAL"))
			{
				String[] tokens = s.split(" ");
				this.notationalCny = tokens[1];
				this.notational = tokens[2];
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
			else
			{
				if (isHedge)
				{
					String[] tokens = s.split("\\s|;|=|\\(|\\)");
					Hedge h = new Hedge();
					h.setSide(tokens[0]);
					h.setQty(Uts2Dm.toDouble(tokens[1]));
					h.setPrice(Uts2Dm.toDouble(tokens[12]));
					h.setFuture(tokens[4] + " " + tokens[5]);
					this.hedges.add(h);
//					this.hedgeFutRef = tokens[2];
				}
				else if (isFees & s.startsWith("BROKERAGE FEE"))
				{
					this.brokerageCny = s.substring(14, 17);
					this.brokerageFee = s.substring(18);
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
				else if (s.contains("RATE")) {
					this.rate = s.substring(13);
				}
			}
		}
		}
		catch (Exception e)
		{
			logger.error("Error parsePdf {}", sPdf, e);
		}
	}

	public static Logger getLogger()
	{
		return logger;
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
		
		List<Hedge> hedges = to.getHedges();
		hedges.addAll(this.hedges);
		List<Leg> legs = to.getLegs();
		legs.addAll(this.legs);
		
		to.setLastModified(new Date(System.currentTimeMillis()));
		
		return to;
	}
	
	public static void main(String[] args)
	{
		String s = "Buy 5 FUTURES (HSCEI OCT16); Future Reference = 9,950";
		String[] tokens = s.split("\\s|;|=|\\(|\\)");
		for (int i =0; i <s.length(); i++){
			System.out.println(i + "=" + tokens[i]);
		}
	}
	
}