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

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.TradeDetailRepo;

//@XmlRootElement(name = "TradeDetail")
@Document(collection = "tradedetail")
public class TradeDetail implements IMongoDocument<TradeDetailRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(TradeDetail.class);
	
	@Id
	private String id;
	
	private String date;
	private String tradeId;
	private String description;
	private String size;
	private String hedge;
	private String reference;
	private String fee;
	private String tradeConfoFile;
	
	@PersistenceConstructor
	public TradeDetail(String id, String date, String tradeId, String description, String size, String hedge,
			String reference, String fee, String tradeConfoFile, Date lastModified)
	{
		super();
		this.id = id;
		this.date = date;
		this.tradeId = tradeId;
		this.description = description;
		this.size = size;
		this.hedge = hedge;
		this.reference = reference;
		this.fee = fee;
		this.tradeConfoFile = tradeConfoFile;
		this.lastModified = lastModified;
	}

	public TradeDetail() {};
	
	private Date lastModified;
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getTradeId()
	{
		return tradeId;
	}

	public void setTradeId(String tradeId)
	{
		this.tradeId = tradeId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public String getHedge()
	{
		return hedge;
	}

	public void setHedge(String hedge)
	{
		this.hedge = hedge;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}

	public String getFee()
	{
		return fee;
	}

	public void setFee(String fee)
	{
		this.fee = fee;
	}

	public String getTradeConfoFile()
	{
		return tradeConfoFile;
	}

	public void setTradeConfoFile(String tradeConfoFile)
	{
		this.tradeConfoFile = tradeConfoFile;
	}

	@Override
	public String toString()
	{
		return "TradeDetail [id=" + id + ", date=" + date + ", tradeId=" + tradeId + ", description=" + description
				+ ", size=" + size + ", hedge=" + hedge + ", reference=" + reference + ", fee=" + fee
				+ ", tradeConfoFile=" + tradeConfoFile + ", lastModified=" + lastModified + "]";
	}

	public List<String> toCsv()
	{
		List<String> s = new ArrayList<String>();
		s.add(this.date);
		s.add(this.tradeId);
		s.add(this.description);
		s.add(this.size);
		s.add(this.hedge);
		s.add(this.reference);
		s.add(this.fee);
		return s;
	}

	@Override
	public void setLastModified(Date d)
	{
		lastModified = d;		
	}

	@Override
	public Class getRepo()
	{
		return TradeDetailRepo.class;
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
		
		JsonObject empJsonObject = builder.build();

		logger.debug("TradeDetail JSON {}", empJsonObject);

//		// write to file
//		OutputStream os = new FileOutputStream("emp.txt");
//		JsonWriter jsonWriter = Json.createWriter(os);
//		/**
//		 * We can get JsonWriter from JsonWriterFactory also JsonWriterFactory
//		 * factory = Json.createWriterFactory(null); jsonWriter =
//		 * factory.createWriter(os);
//		 */
//		jsonWriter.writeObject(empJsonObject);
//		jsonWriter.close();
		
		return empJsonObject;
	}
	
}
