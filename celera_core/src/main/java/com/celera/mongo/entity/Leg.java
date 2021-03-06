package com.celera.mongo.entity;

import java.lang.reflect.Field;
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
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.HedgeRepo;

@Document(collection = "leg")
public class Leg implements IMongoDocument<HedgeRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(Leg.class); 
	
	@Id
	private String id;
	private String side = null;
	private Double qty = null;
	private String expiry = null;
	private String strike = null;
	private String price = null;
	private String premium = null;
	private String product = null;

	private Date lastModified;

	public Leg()
	{
	}

	@PersistenceConstructor
	public Leg(String side, Double qty, String expiry, String strike, String price, String premium, String product)
	{
		this.side = side;
		this.qty = qty;
		this.expiry = expiry;
		this.strike = strike;
		this.price = price;
		this.premium = premium;
		this.product = product;
	}

	@Override
	public Class getRepo()
	{
		return HedgeRepo.class;
	}

	
	public void setSide(String side)
	{
		this.side = side;
	}

	public String getSide()
	{
		return side;
	}

	public Double getQty()
	{
		return qty;
	}

	public String getExpiry()
	{
		return expiry;
	}

	public String getStrike()
	{
		return strike;
	}

	public String getPrice()
	{
		return price;
	}

	public String getPremium()
	{
		return premium;
	}

	public String getProduct()
	{
		return product;
	}

	public void setQty(Double qty)
	{
		this.qty = qty;
	}

	public void setExpiry(String expiry)
	{
		this.expiry = expiry;
	}

	public void setStrike(String strike)
	{
		this.strike = strike;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public void setPremium(String premium)
	{
		this.premium = premium;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	@Override
	public String toString()
	{
		return "Leg [side=" + side + ", qty=" + qty + ", expiry=" + expiry + ", strike=" + strike + ", price=" + price
				+ ", premium=" + premium + ", product=" + product + "]";
	}

	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			try
			{
				Object o = f.get(this);
				if (o != null)
				{
					String name = f.getName();
					builder.add(name, o.toString());
				}
			} catch (IllegalArgumentException | IllegalAccessException e)
			{
				logger.error("", e);
			}
		}
		
//		builder.add("side", side).add("qty", qty).add("price", price).add("future", future);
		JsonObject object = builder.build();
		return object;
	}

	@Override
	public void setLastModified(Date d)
	{
		this.lastModified = d;
	}

	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public void setId(String id)
	{
		this.id = id;		
	}
}
