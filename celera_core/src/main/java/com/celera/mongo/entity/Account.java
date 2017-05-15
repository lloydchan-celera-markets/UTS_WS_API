package com.celera.mongo.entity;

import java.util.Date;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.AccountRepo;

@Document(collection = "account")
public class Account implements IMongoDocument<AccountRepo>
{
	@Id
	private String id;
	private String company = null;
	private String address = null;
	private String emails = null;
	private String attn = null;

	private Date lastModified;

	private String utsName = null;
	
	public Account()
	{}

	public Account(String id, String company, String address, String emails, String attn, String utsName)
	{
		super();
		this.id = id;
		this.company = company;
		this.address = address;
		this.emails = emails;
		this.attn = attn;
		
		this.utsName = utsName;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getEmails()
	{
		return emails;
	}

	public void setEmails(String emails)
	{
		this.emails = emails;
	}

	public String getAttn()
	{
		return attn;
	}

	public void setAttn(String attn)
	{
		this.attn = attn;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	// public JsonObject json()
	//
	// {
	//
	// JsonObjectBuilder builder = Json.createObjectBuilder();
	//
	// for (Field f : this.getClass().getDeclaredFields())
	//
	// {
	//
	// try
	//
	// {
	//
	// Object o = f.get(this);
	//
	// if (o != null)
	//
	// {
	//
	// String name = f.getName();
	//
	// builder.add(name, o.toString());
	//
	// }
	//
	// } catch (IllegalArgumentException | IllegalAccessException e)
	//
	// {
	//
	// logger.error("", e);
	//
	// }
	//
	// }
	//
	// // builder.add("side", side).add("qty", qty).add("price",
	//
	// // price).add("future", future);
	//
	// JsonObject object = builder.build();
	//
	// return object;
	//
	// }

	public String getUtsName() {
		return utsName;
	}

	public void setUtsName(String utsName) {
		this.utsName = utsName;
	}

	public static void main(String[] args)
	{


	}

	public String key() {
		return this.company.toUpperCase();
	}
	
	@Override
	public String toString()
	{
		return "Account [id=" + id + ", company=" + company + ", address=" + address + ", emails=" + emails + ", attn="
				+ attn + "]";
	}

	@Override
	public void setLastModified(Date d)
	{
		this.lastModified = d;
	}

	@Override
	public Class getRepo()
	{
		return AccountRepo.class;
	}
}
