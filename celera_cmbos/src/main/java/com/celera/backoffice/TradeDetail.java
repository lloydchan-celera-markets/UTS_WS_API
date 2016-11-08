package com.celera.backoffice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TradeDetail")
public class TradeDetail
{
	String date;
	String id;
	String description;
	String size;
	String hedge;
	String reference;
	String fee;

	@XmlElement
	public void setDate(String date)
	{
		this.date = date;
	}

	@XmlElement
	public void setId(String id)
	{
		this.id = id;
	}

	@XmlElement
	public void setDescription(String description)
	{
		this.description = description;
	}

	@XmlElement
	public void setSize(String size)
	{
		this.size = size;
	}

	@XmlElement
	public void setHedge(String hedge)
	{
		this.hedge = hedge;
	}

	@XmlElement
	public void setReference(String reference)
	{
		this.reference = reference;
	}

	@XmlElement
	public void setFee(String fee)
	{
		this.fee = fee;
	}

	public String getDate()
	{
		return date;
	}

	public String getId()
	{
		return id;
	}

	public String getDescription()
	{
		return description;
	}

	public String getSize()
	{
		return size;
	}

	public String getHedge()
	{
		return hedge;
	}

	public String getReference()
	{
		return reference;
	}

	public String getFee()
	{
		return fee;
	}

	@Override
	public String toString()
	{
		return "TradeDetail [date=" + date + ", id=" + id + ", description=" + description + ", size=" + size
				+ ", hedge=" + hedge + ", reference=" + reference + ", fee=" + fee + "]";
	}

	public List<String> toCsv()
	{
		List<String> s = new ArrayList<String>();
		s.add(this.date);
		s.add(this.id);
		s.add(this.description);
		s.add(this.size);
		s.add(this.hedge);
		s.add(this.reference);
		s.add(this.fee);
		return s;
	}
}
