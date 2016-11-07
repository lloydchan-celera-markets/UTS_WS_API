package com.celera.backoffice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TradeDetails")
public class TradeDetails
{
    List<TradeDetail> TradeDetail = new ArrayList<TradeDetail>();
    String period;
    String company;
    String description;
    String size;
    String hedge;
    String total_fee;

	@XmlElement
	public void setPeriod(String period)
	{
		this.period = period;
	}
	@XmlElement
	public void setCompany(String company)
	{
		this.company = company;
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
	public void setTotal_fee(String total_fee)
	{
		this.total_fee = total_fee;
	}

	public List<TradeDetail> getTradeDetail()
	{
		return TradeDetail;
	}

	@XmlElement(name = "TradeDetail")
	public void setTradeDetail(List<TradeDetail> data)
	{
		this.TradeDetail = data;
	}
	public String getPeriod()
	{
		return period;
	}
	public String getCompany()
	{
		return company;
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
	public String getTotal_fee()
	{
		return total_fee;
	}
	
}