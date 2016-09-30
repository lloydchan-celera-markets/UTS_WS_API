package com.celera.core.dm;

import java.time.LocalDate;

public interface ITrade
{
	public Long getId();	// unique
	public void setId(Long id);
	public Double getPrice();
	public void setPrice(Double price);
	public Long getQty();
	public void setQty(Long qty);
	public LocalDate getTime();
	public void setTime(LocalDate time);
}
