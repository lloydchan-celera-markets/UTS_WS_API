package com.celera.core.dm;

import java.io.IOException;

import javax.json.JsonObject;

public interface ITradeReport extends IOrder 
{
	public EOrderStatus getStatus();
	public void setStatus(EOrderStatus status);
	public IInstrument getInstr();
	public void setInstr(IInstrument instr);
	public EOrderType getOrderType();
	public void setOrderType(EOrderType ordType);
	public Long getId();
	public void setId(Long id);
	public void setTradeReportType(ETradeReportType trType);
	
	public void setQty(Integer qty);
	public Integer getQty();
	public void setPrice(Double p);
	public Double getPrice();
	
	public String getBuyer();
	public String getSeller();
	public ETradeReportType getTradeReportType();
	
	public String getRemark();
	public void setRemark(String text);
	
	public Long getGroupId();
	public void setGroupId(Long groupId);
	
	public byte[] toCmmf() throws IOException;
	public JsonObject json();
	
	public Long getLastUpdateTime();
}
