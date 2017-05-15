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

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.InvoiceRepo;
import com.celera.mongo.repo.LogRepo;


//@XmlRootElement(name="Invoice")	// generate xml -> pdf
@Document(collection = "log")
public class Log implements IMongoDocument<LogRepo>, Comparable
{
	private static final Logger logger = LoggerFactory.getLogger(Log.class);

	@Id
	private String id;
	
	private String message;
	
	private Date lastModified;

	public Log() {}
	
	@PersistenceConstructor
	public Log(String id, String message, Date lastModified)
	{
		super();
		this.id = id;
		this.message = message;
		this.lastModified = lastModified;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
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
		return LogRepo.class;
	}
	
	@Override
	public String toString()
	{
		return "Log [id=" + id + ", message=" + message + ", lastModified=" + lastModified + "]";
	}

	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();

		builder.add("message", this.message);
		builder.add("lastModified", this.lastModified.getTime());
		JsonObject empJsonObject = builder.build();

		logger.debug("Log JSON {}", empJsonObject);

		return empJsonObject;
	}

	@Override
	public int compareTo(Object o)
	{
		return this.getLastModified().compareTo(((Log)o).getLastModified());
	}
}
