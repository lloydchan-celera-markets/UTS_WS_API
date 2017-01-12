package com.celera.core.dm;

import java.time.LocalDate;

public interface IQuote extends IOrder
{
	public Double getBidPrice();
	public void setBidPrice(Double bidPrice);
	public Long getBidTime();
	public void setBidTime(Long bidTime);
	public Long getBidQty();
	public void setBidQty(Long bidQty);
	public Double getAskPrice();
	public void setAskPrice(Double askPrice);
	public Long getAskTime();
	public void setAskTime(Long askTime);
	public Long getAskQty();
	public void setAskQty(Long askQty);
}
