package com.celera.core.dm;

import java.time.LocalDate;

public interface IQuote extends IOrder
{
	public Double getBidPrice();
	public void setBidPrice(Double bidPrice);
	public LocalDate getBidTime();
	public void setBidTime(LocalDate bidTime);
	public Long getBidQty();
	public void setBidQty(Long bidQty);
	public Double getAskPrice();
	public void setAskPrice(Double askPrice);
	public LocalDate getAskTime();
	public void setAskTime(LocalDate askTime);
	public Long getAskQty();
	public void setAskQty(Long askQty);
}
