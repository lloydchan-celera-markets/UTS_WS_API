package com.celera.core.dm;

import java.time.LocalDate;

import javax.json.JsonObject;

public class Trade implements ITrade
{
	private Long id = null;
	private Long orderId = null;
	private Double price = null;
	private Integer qty = null;
	private LocalDate time = null;
	private String comment = null;
	private Long lastUpdateTime = null;

	public Trade() {
		lastUpdateTime = System.currentTimeMillis(); 
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public LocalDate getTime()
	{
		return time;
	}

	public void setTime(LocalDate time)
	{
		this.time = time;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	@Override
	public String toString()
	{
		return "Trade [id=" + id + ", orderId=" + orderId + ", price=" + price + ", qty=" + qty + ", time=" + time
				+ ", comment=" + comment + "]";
	}

	@Override
	public JsonObject json()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	@Override
	public void setLastUpdateTime(Long time)
	{
		lastUpdateTime = time;		
	}
}
