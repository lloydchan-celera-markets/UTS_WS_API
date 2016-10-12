package com.celera.core.dm;

import java.time.LocalDate;

public class Trade implements ITrade
{
	private String id = null;
	private Long orderId = null;
	private Double price = null;
	private Long qty = null;
	private LocalDate time = null;
	private String comment = null;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
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

	public Long getQty()
	{
		return qty;
	}

	public void setQty(Long qty)
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
}
