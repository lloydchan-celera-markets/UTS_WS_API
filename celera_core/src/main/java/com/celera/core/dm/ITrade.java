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
	public LocalDate getTime();
	public void setTime(LocalDate time);
	
	public JsonObject json();
}
