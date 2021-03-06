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

@Document(collection = "hedge")
public class Hedge implements IMongoDocument<HedgeRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(Hedge.class); 
	
	@Id
	private String id;
	private String side = null;
	private Double qty = null;
	private Double price = null;
	private String future = null;

	private Date lastModified;

	public Hedge()
	{
	}

	@PersistenceConstructor
	public Hedge(String side, Double qty, Double price, String future)
	{
		super();
		this.side = side;
		this.qty = qty;
		this.price = price;
		this.future = future;
	}
	
	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	public Double getQty()
	{
		return qty;
	}

	public void setQty(Double qty)
	{
		this.qty = qty;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public String getFuture()
	{
		return future;
	}

	public void setFuture(String future)
	{
		this.future = future;
	}

	@Override
	public Class getRepo()
	{
		return HedgeRepo.class;
	}

	@Override
	public String toString()
	{
		return "Hedge [side=" + side + ", qty=" + qty + ", price=" + price + ", future=" + future + "]";
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
//	public static void main(String[] args)
//	{
//		TradeConfo o = new TradeConfo();
////		o.setBuyer("Barclay");
////		o.setTradeConfoId("234");
//		System.out.println(o.key());
//	}

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
