package com.celera.core.dm;

public interface IDerivative extends IInstrument
{
	public Double getStrike();
	public Double getDelta();
	public void setPrice(Double price);
	public String getExpiry();
}
