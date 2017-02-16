package com.celera.core.dm;

import java.time.LocalDate;

import javax.json.JsonObject;

public interface ITrade
{
	public Long getTradeId();	// unique
	public void setTradeId(Long id);
	public Long getOrderId();	// unique
	public void setOrderId(Long id);
	public Double getPrice();
	public void setPrice(Double price);
	public Integer getQty();
	public void setQty(Integer qty);
	
	public EOrderStatus getStatus();
	public Integer getGiveupId();
	
	public Long getLastUpdateTime();
	public void setLastUpdateTime(Long time);
	
	public JsonObject json();
}
