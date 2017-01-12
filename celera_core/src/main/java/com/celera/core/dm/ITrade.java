package com.celera.core.dm;

import java.time.LocalDate;

import javax.json.JsonObject;

public interface ITrade
{
	public Long getId();	// unique
	public void setId(Long id);
	public Double getPrice();
	public void setPrice(Double price);
	public Integer getQty();
	public void setQty(Integer qty);
	public Long getLastUpdateTime();
	public void setLastUpdateTime(Long time);
	
	public JsonObject json();
}
