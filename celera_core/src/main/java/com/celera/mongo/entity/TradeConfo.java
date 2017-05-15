package com.celera.mongo.entity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.TradeConfoRepo;

@Document(collection = "tradeconfo")
public class TradeConfo implements ICustomizeMongoDocument<TradeConfoRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(TradeConfo.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	@Id
	private String id;
	private String summary;
	private String buyer;
	private String seller;
	private Double price;
	private String curncy;
	private Date tradeDate;
	private Double refPrice;
	private String tradeConfoId;
	private String delta;
	private Double buyQty;
	private Double sellQty;
	private Double ptValue;
	private String ptCny;
	private String premiumPmt;
	private Double notational;
	private String notationalCny;
	private Double rate;
	private Double premium;
	private String premiumCny;
	
	private Double brokerageFee;
	private String brokerageCny;
	
	private Boolean hasInvoiceCreated = false;

	private String file;
	
	private Date lastModified;

	private List<Hedge> hedges = new ArrayList<Hedge>();
	private List<Leg> legs = new ArrayList<Leg>();

	public TradeConfo()
	{
	}

	@PersistenceConstructor
	public TradeConfo(String id, String summary, String buyer, String seller, Double price, String curncy,
			Date tradeDate, Double refPrice, String tradeConfoId, String delta, Double buyQty, Double sellQty,
			Double ptValue, String ptCny, String premiumPmt, Double notational, String notationalCny, Double rate,
			Double premium, String premiumCny, List<Leg> legs, List<Hedge> hedges, Double brokerageFee,
			String brokerageCny, Date lastModified, Boolean hasInvoiceCreated, String file)
	{
		super();
		this.id = id;
		this.summary = summary;
		this.buyer = buyer;
		this.seller = seller;
		this.price = price;
		this.curncy = curncy;
		this.tradeDate = tradeDate;
		this.refPrice = refPrice;
		this.tradeConfoId = tradeConfoId;
		this.delta = delta;
		this.buyQty = buyQty;
		this.sellQty = sellQty;
		this.ptValue = ptValue;
		this.ptCny = ptCny;
		this.premiumPmt = premiumPmt;
		this.notational = notational;
		this.notationalCny = notationalCny;
		this.rate = rate;
		this.premium = premium;
		this.premiumCny = premiumCny;
		this.legs = legs;
		this.hedges = hedges;
		this.brokerageFee = brokerageFee;
		this.brokerageCny = brokerageCny;
		this.lastModified = lastModified;
		this.hasInvoiceCreated = hasInvoiceCreated;
		this.file = file;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setTradeConfoId(String tradeConfoId)
	{
		this.tradeConfoId = tradeConfoId;
	}

	public String getTradeConfoId()
	{
		return tradeConfoId;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getBuyer()
	{
		return buyer;
	}

	public void setBuyer(String buyer)
	{
		this.buyer = buyer;
	}

	public String getSeller()
	{
		return seller;
	}

	public void setSeller(String seller)
	{
		this.seller = seller;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public String getCurncy()
	{
		return curncy;
	}

	public void setCurncy(String curncy)
	{
		this.curncy = curncy;
	}

	public Date getTradeDate()
	{
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate)
	{
		this.tradeDate = tradeDate;
	}

	public Double getRefPrice()
	{
		return refPrice;
	}

	public void setRefPrice(Double refPrice)
	{
		this.refPrice = refPrice;
	}

	public String getDelta()
	{
		return delta;
	}

	public void setDelta(String delta)
	{
		this.delta = delta;
	}

	public Double getBuyQty()
	{
		return buyQty;
	}

	public void setBuyQty(Double buyQty)
	{
		this.buyQty = buyQty;
	}

	public Double getSellQty()
	{
		return sellQty;
	}

	public void setSellQty(Double sellQty)
	{
		this.sellQty = sellQty;
	}

	public Double getPtValue()
	{
		return ptValue;
	}

	public void setPtValue(Double ptValue)
	{
		this.ptValue = ptValue;
	}

	public String getPtCny()
	{
		return ptCny;
	}

	public void setPtCny(String ptCny)
	{
		this.ptCny = ptCny;
	}

	public String getPremiumPmt()
	{
		return premiumPmt;
	}

	public void setPremiumPmt(String premiumPmt)
	{
		this.premiumPmt = premiumPmt;
	}

	public Double getNotational()
	{
		return notational;
	}

	public void setNotational(Double notational)
	{
		this.notational = notational;
	}

	public String getNotationalCny()
	{
		return notationalCny;
	}

	public void setNotationalCny(String notationalCny)
	{
		this.notationalCny = notationalCny;
	}

	public Double getRate()
	{
		return rate;
	}

	public void setRate(Double rate)
	{
		this.rate = rate;
	}

	public Double getPremium()
	{
		return premium;
	}

	public void setPremium(Double premium)
	{
		this.premium = premium;
	}

	public String getPremiumCny()
	{
		return premiumCny;
	}

	public void setPremiumCny(String premiumCny)
	{
		this.premiumCny = premiumCny;
	}

	public List<Leg> getLegs()
	{
		return legs;
	}

	public void setLegs(List<Leg> legs)
	{
		this.legs = legs;
	}

	public List<Hedge> getHedges()
	{
		return hedges;
	}

	public void setHedges(List<Hedge> hedges)
	{
		this.hedges = hedges;
	}

	public Double getBrokerageFee()
	{
		return brokerageFee;
	}

	public void setBrokerageFee(Double brokerageFee)
	{
		this.brokerageFee = brokerageFee;
	}

	public String getBrokerageCny()
	{
		return brokerageCny;
	}

	public void setBrokerageCny(String brokerageCny)
	{
		this.brokerageCny = brokerageCny;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}

	public Boolean getHasInvoiceCreated()
	{
		return hasInvoiceCreated;
	}

	public void setHasInvoiceCreated(Boolean hasInvoiceCreated)
	{
		this.hasInvoiceCreated = hasInvoiceCreated;
	}

	public String getFile()
	{
		return file;
	}

	public void setFile(String file)
	{
		this.file = file;
	}

	@Override
	public String toString()
	{
		return "TradeConfo [id=" + id + ", summary=" + summary + ", buyer=" + buyer + ", seller=" + seller + ", price="
				+ price + ", curncy=" + curncy + ", tradeDate=" + tradeDate + ", refPrice=" + refPrice
				+ ", tradeConfoId=" + tradeConfoId + ", delta=" + delta + ", buyQty=" + buyQty + ", sellQty=" + sellQty
				+ ", ptValue=" + ptValue + ", ptCny=" + ptCny + ", premiumPmt=" + premiumPmt + ", notational="
				+ notational + ", notationalCny=" + notationalCny + ", rate=" + rate + ", premium=" + premium
				+ ", premiumCny=" + premiumCny + ", brokerageFee=" + brokerageFee + ", brokerageCny=" + brokerageCny
				+ ", hasSent=" + hasInvoiceCreated + ", file=" + file + ", lastModified=" + lastModified + ", hedges=" + hedges
				+ ", legs=" + legs + "]";
	}

	@Override
	public Class getRepo()
	{
		return TradeConfoRepo.class;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyer == null) ? 0 : buyer.hashCode());
		result = prime * result + ((seller == null) ? 0 : seller.hashCode());
		result = prime * result + ((tradeConfoId == null) ? 0 : tradeConfoId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeConfo other = (TradeConfo) obj;
		if (buyer == null)
		{
			if (other.buyer != null)
				return false;
		}
		else if (!buyer.equals(other.buyer))
			return false;
		if (seller == null)
		{
			if (other.seller != null)
				return false;
		}
		else if (!seller.equals(other.seller))
			return false;
		if (tradeConfoId == null)
		{
			if (other.tradeConfoId != null)
				return false;
		}
		else if (!tradeConfoId.equals(other.tradeConfoId))
			return false;
		return true;
	}

	public String getKey()
	{
		return buyer + seller + tradeConfoId;
	}
	
	public JsonObject json()
	{
		JsonObjectBuilder tradeConfoBuilder = Json.createObjectBuilder();
		JsonArrayBuilder hedgesBuilder = Json.createArrayBuilder();
		JsonArrayBuilder legsBuilder = Json.createArrayBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			try
			{
				Object o = f.get(this);
				if (o != null)
				{
					String name = f.getName();
					if (name.equals("tradeDate")) {
						tradeConfoBuilder.add(name, ((Date)o).getTime());
					}
					else {
						tradeConfoBuilder.add(name, o.toString());
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e)
			{
				logger.error("", e);
			}
			catch (Exception e) {
				logger.error("", e);
			}
		}
		
		JsonObject empJsonObject = null;
		try
		{
//			if (hedges != null)
			{
				for (Hedge h : hedges)
				{
					hedgesBuilder.add(h.json());
				}
				tradeConfoBuilder.add("hedges", hedgesBuilder);
			}
//			if (legs != null)
			{
				for (Leg l : legs)
				{
					legsBuilder.add(l.json());
				}
				tradeConfoBuilder.add("legs", legsBuilder);
			}

			empJsonObject = tradeConfoBuilder.build();

			logger.debug("TradeConfo JSON {}", empJsonObject);
		} catch (Exception e)
		{
			logger.error("{}", this.toString(), e);
		}

		return empJsonObject;	}
}
	
	
	