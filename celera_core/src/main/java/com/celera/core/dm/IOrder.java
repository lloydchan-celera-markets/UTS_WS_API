package com.celera.core.dm;

import java.io.IOException;

import javax.json.JsonObject;

public interface IOrder
{
	public EOrderStatus getStatus();
	public void setStatus(EOrderStatus status);
	public IInstrument getInstr();
	public void setInstr(IInstrument instr);
	public EOrderType getOrderType();
	public void setOrderType(EOrderType ordType);
	public Long getId();
	public void setId(Long id);
	
	public void setQty(Integer qty);
	public Integer getQty();
	public void setPrice(Double p);
	public Double getPrice();
	
	public byte[] toCmmf() throws IOException;
	public JsonObject json();
}
