//package com.celera.mongo.entity;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.json.Json;
//import javax.json.JsonArrayBuilder;
//import javax.json.JsonObject;
//import javax.json.JsonObjectBuilder;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.PersistenceConstructor;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import com.celera.mongo.repo.InvoiceRepo;
//import com.celera.mongo.repo.TradeConfoRepo;
//
//@Document(collection = "tradeconfo")
//public class Invoice_bak implements IMongoDocument<InvoiceRepo>
//{
//	private static final Logger logger = LoggerFactory.getLogger(Invoice_bak.class);
//	
//	@Id
//	private String id;
//	private String MMyy;
//	private String client;
//	private String currency;
//	private Double amount;
//	private String file;
//	private String invoiceId;
//	private Boolean isSent;
//	private Boolean isPaid;
//	
//	private Date lastModified;
//
//	private List<TradeConfo> tradeConfo = new ArrayList<TradeConfo>();
//
//	public Invoice_bak()
//	{
//	}
//
//	@PersistenceConstructor
//	public Invoice_bak(String id, String mMyy, String client, String currency, Double amount, String file, String invoiceId,
//			Boolean isSent, Boolean isPaid, Date lastModified, List<TradeConfo> tradeConfo)
//	{
//		super();
//		this.id = id;
//		MMyy = mMyy;
//		this.client = client;
//		this.currency = currency;
//		this.amount = amount;
//		this.file = file;
//		this.invoiceId = invoiceId;
//		this.isSent = isSent;
//		this.isPaid = isPaid;
//		this.lastModified = lastModified;
//		this.tradeConfo = tradeConfo;
//	}
//
//	public String getId()
//	{
//		return id;
//	}
//
//	public void setId(String id)
//	{
//		this.id = id;
//	}
//
//
//	public Date getLastModified()
//	{
//		return lastModified;
//	}
//
//	public void setLastModified(Date lastModified)
//	{
//		this.lastModified = lastModified;
//	}
//
//	public String getInvoiceId()
//	{
//		return invoiceId;
//	}
//
//	public void setInvoiceId(String invoiceId)
//	{
//		this.invoiceId = invoiceId;
//	}
//
//	@Override
//	public String toString()
//	{
//		return "Invoice [id=" + id + ", MMyy=" + MMyy + ", client=" + client + ", currency=" + currency + ", amount="
//				+ amount + ", file=" + file + ", invoiceId=" + invoiceId + ", isSent=" + isSent + ", isPaid=" + isPaid
//				+ ", lastModified=" + lastModified + ", tradeConfo=" + tradeConfo + "]";
//	}
//
//	@Override
//	public Class getRepo()
//	{
//		return Invoice_bak.class;
//	}
//
//	public JsonObject json()
//	{
//		JsonObjectBuilder builder = Json.createObjectBuilder();
//		JsonArrayBuilder tradeConfoBuilder = Json.createArrayBuilder();
//
//		for (Field f : this.getClass().getDeclaredFields()) {
//			try
//			{
//				Object o = f.get(this);
//				if (o != null)
//				{
//					String name = f.getName();
//					builder.add(name, o.toString());
//				}
//			} catch (IllegalArgumentException | IllegalAccessException e)
//			{
//				logger.error("", e);
//			}
//		}
//		
//		for (TradeConfo h : tradeConfo)
//		{
//			tradeConfoBuilder.add(h.json());
//		}
//		
//		builder.add("tradeconfo", tradeConfoBuilder);
//
//		JsonObject empJsonObject = builder.build();
//
//		logger.debug("Invoice JSON {}", empJsonObject);
//
//		return empJsonObject;
//	}
//	
//	
////	public static void main(String[] args)
////	{
////		TradeConfo o = new TradeConfo();
//////		o.setBuyer("Barclay");
//////		o.setTradeConfoId("234");
////		System.out.println(o.key());
////	}
//}
