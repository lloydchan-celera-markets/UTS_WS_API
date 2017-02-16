package com.celera.core.dm;

import java.time.LocalDate;

import javax.json.JsonObject;

public class Trade implements ITrade
{
	private Long id = null;
	private Long orderId = null;
	private Double price = null;
	private Integer qty = null;
	private String comment = null;
	private EOrderStatus status = null;
	private Integer giveupId = null;
	private Long lastUpdateTime = null;

	public Trade() {
		lastUpdateTime = System.currentTimeMillis(); 
	}
	
	public Trade(Long id, Long orderId, Double price, Integer qty, EOrderStatus status, String comment, Integer giveupId)
	{
		super();
		this.id = id;
		this.orderId = orderId;
		this.price = price;
		this.qty = qty;
		this.comment = comment;
		this.giveupId = giveupId;
		lastUpdateTime = System.currentTimeMillis(); 
	}

	public Long getTradeId()
	{
		return id;
	}

	public void setTradeId(Long id)
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
		return "Trade [id=" + id + ", orderId=" + orderId + ", price=" + price + ", qty=" + qty + ", comment=" + comment
				+ ", giveupId=" + giveupId
				+ ", lastUpdateTime=" + lastUpdateTime + "]";
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

	public Integer getGiveupId()
	{
		return giveupId;
	}

	public void setGiveupId(Integer giveupId)
	{
		this.giveupId = giveupId;
	}

	public EOrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(EOrderStatus status)
	{
		this.status = status;
	}
	
	
}
