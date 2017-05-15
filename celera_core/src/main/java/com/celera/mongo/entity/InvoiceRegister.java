package com.celera.mongo.entity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.InvoiceRegisterRepo;

@Document(collection = "invoiceregister")
public class InvoiceRegister implements ICustomizeMongoDocument<InvoiceRegisterRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(InvoiceRegister.class);
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat sdf_ddMMMyy = new SimpleDateFormat("dd-MMM-yy");
	public static SimpleDateFormat sdfMMyy = new SimpleDateFormat("MMyy");

	@Id
	private String id;
	
	private Date date;
	private String invoice;
	private String customer;
	private String accountNumber;
	private String curncy;
	private Double amount;
	private String key;
	
	private Date lastModified;
	
	@PersistenceConstructor
	public InvoiceRegister(Date date, String invoice, String customer, String accountNumber, String curncy,
			Double amount)
	{
		super();
		this.date = date;
		this.invoice = invoice;
		this.customer = customer;
		this.accountNumber = accountNumber;
		this.curncy = curncy;
		this.amount = amount;
		this.key = this.toKey();
	}

	public InvoiceRegister(){};
	public InvoiceRegister(String date, String invoice, String accountNumber, String curncy, String customer, 
			Double amount)	// use by CSVReader
	{
		super();
		try {
			this.date = sdf_ddMMMyy.parse(date);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.invoice = invoice;
		this.customer = customer;
		this.accountNumber = accountNumber;
		this.curncy = curncy;
		this.amount = amount;
		this.key = this.toKey();
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getCustomer()
	{
		return customer;
	}

	public void setCustomer(String customer)
	{
		this.customer = customer;
	}

	public String getAccountNumber()
	{
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	public String getCurncy()
	{
		return curncy;
	}

	public void setCurncy(String curncy)
	{
		this.curncy = curncy;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	public String toKey() {
//		Date d;
//		try {
//			d = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, -1);
//			String dKey = sdfMMMyy.format(cal.getTime());
//			String key = "IR_" + accountNumber + "_" + curncy.replace("KRW", "USD") + "_" + sdfMMyy.format(date);
			String key = accountNumber + "_" + curncy.replace("KRW", "USD") + "_" + sdfMMyy.format(date);
			key = key.toUpperCase();
//			String key = customer + "_" + curncy + "_" + dKey;
//System.out.println("===================================================map.key=" + key);
			return key;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	@Override
	public void setLastModified(Date d)
	{
		this.lastModified = d;
	}

	@Override
	public Class getRepo()
	{
		return InvoiceRegisterRepo.class;
	}

	@Override
	public String getId()
	{
		return this.id;
	}

	@Override
	public void setId(String id)
	{
		this.id = id;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@Override
	public String toString()
	{
		return "InvoiceRegister [id=" + id + ", date=" + date + ", invoice=" + invoice + ", customer=" + customer
				+ ", accountNumber=" + accountNumber + ", curncy=" + curncy + ", amount=" + amount + ", key=" + key
				+ ", lastModified=" + lastModified + "]";
	}

	public JsonObject json()
	{
		JsonObject empJsonObject = null;
		JsonObjectBuilder builder = Json.createObjectBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			try
			{
				Object o = f.get(this);
				if (o != null)
				{
					String name = f.getName();
					if (name.equals("date")) {
						builder.add(name, ((Date)o).getTime());
					}
					else {
						builder.add(name, o.toString());
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
		
		empJsonObject = builder.build();

		logger.debug("InvoiceRegister JSON {}", empJsonObject);
		
		return empJsonObject;
	}
	
}
