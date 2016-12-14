package com.celera.core.dm;

public class Leg extends Derivative implements ILeg
{
	private Double multiplier = null;

	public Leg(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE,
			String rIC, Double strike, String expiry, Double price, Boolean isPriceInPercent,
			Double multiplier, Double delta)
	{
		super(market, symbol, type, name, iSIN, bLOOMBERG_CODE, rIC, strike, expiry, price,
				isPriceInPercent, delta);
		this.multiplier = multiplier;
	}

	@Override
	public String toString()
	{
		return "Leg [symbol=" + this.symbol + ", name=" + this.name + ", type=" + this.type + ", multiplier=" + multiplier + ", strike="
				+ this.strike + ", expiry=" + this.expiry + ", price=" + this.price + "]";
	}

//	@Override
	public void setMultiplier(Double multiplier)
	{
		this.multiplier = multiplier;
	}
}
