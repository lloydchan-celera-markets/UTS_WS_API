package com.celera.core.dm;

public interface IOrder
{
	public EOrderStatus getStatus();
	public void setStatus(EOrderStatus status);
	public IInstrument getInstr();
	public void setInstr(IInstrument instr);
	public Long getId();
	public void setId(Long id);
	
	public void setQty(Long qty);
	public Long getQty();
	public void setPrice(Double p);
	public Double getPrice();
}
