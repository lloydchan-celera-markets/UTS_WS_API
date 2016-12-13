package com.celera.core.dm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Leg extends Derivative implements ILeg
{
	private Double multiplier = null;

	public Leg(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE,
			String rIC, String strike, String expiry, Double price, Boolean isPriceInPercent,
			Double multiplier)
	{
		super(market, symbol, type, name, iSIN, bLOOMBERG_CODE, rIC, strike, expiry, price,
				isPriceInPercent);
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
