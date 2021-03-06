package com.celera.core.dm;

import java.io.IOException;
import java.time.LocalDate;

import javax.json.JsonObject;

public interface IOrder
{
	public EOrderStatus getStatus();
	public void setStatus(EOrderStatus status);
	public IInstrument getInstr();
	public void setInstr(IInstrument instr);
	public EOrderType getOrderType();
	public void setOrderType(EOrderType ordType);
	public Long getOrderId();
	public void setOrderId(Long id);
	public Long getRefId();
	public void setRefId(Long id);
	
	public void setQty(Integer qty);
	public Integer getQty();
	public void setPrice(Double p);
	public Double getPrice();
	
	public String getRemark();
	public void setRemark(String remark);
	
	public Long getLastUpdateTime();
	
	public byte[] toCmmf() throws IOException;
	
	public void update(Double price, Integer qty, String giveup, ESessionState state);
	
	public IOrder clone();
	
	public void addTrade(ITrade trade);
	
	public JsonObject json();
}
