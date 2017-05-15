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

import com.celera.mongo.repo.TradeDetailsRepo;

//@XmlRootElement(name = "TradeDetails")
@Document(collection = "tradedetails")
public class TradeDetails implements IMongoDocument<TradeDetailsRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(TradeDetails.class);
	
	@Id
	private String id;
	
	private String size;
	private String hedge;
	private String total_fee;
	
	private Date lastModified;
	
//	@DBRef(db = "tradedetail")
	List<TradeDetail> tradeDetail = new ArrayList<TradeDetail>();
	
	@PersistenceConstructor
	public TradeDetails(String id, String size, String hedge, String total_fee, Date lastModified,
			List<TradeDetail> tradeDetail)
	{
		super();
		this.id = id;
		this.size = size;
		this.hedge = hedge;
		this.total_fee = total_fee;
		this.lastModified = lastModified;
		this.tradeDetail = tradeDetail;
	}

	public TradeDetails() {}
	
//	@XmlElement
	public void setSize(String size)
	{
		this.size = size;
	}

//	@XmlElement
	public void setHedge(String hedge)
	{
		this.hedge = hedge;
	}

//	@XmlElement
	public void setTotal_fee(String total_fee)
	{
		this.total_fee = total_fee;
	}

	public List<TradeDetail> getTradeDetail()
	{
		return tradeDetail;
	}

//	@XmlElement(name = "TradeDetail")
	public void setTradeDetail(List<TradeDetail> data)
	{
		this.tradeDetail = data;
	}

	public String getSize()
	{
		return size;
	}

	public String getHedge()
	{
		return hedge;
	}

	public String getTotal_fee()
	{
		return total_fee;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	@Override
	public void setLastModified(Date d)
	{
		this.lastModified = d;
	}

	@Override
	public Class getRepo()
	{
		return TradeDetailsRepo.class;
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

	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();
		JsonArrayBuilder tdBuilder = Json.createArrayBuilder();

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
		
		for (TradeDetail h : this.tradeDetail)
		{
			tdBuilder.add(h.json());
		}
		
		builder.add("tradedetail", tdBuilder);

		JsonObject empJsonObject = builder.build();

		logger.debug("TradeDetails JSON {}", empJsonObject);

		return empJsonObject;
	}
	
}