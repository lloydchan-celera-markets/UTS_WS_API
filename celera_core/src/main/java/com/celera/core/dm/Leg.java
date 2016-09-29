package com.celera.core.dm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Leg extends Derivative
{
	private Double multiplier = null;

	public Leg(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE,
			String rIC, LocalDate lastUpdate, String strike, String expiry, Double price, Boolean isPriceInPercent,
			Double multiplier)
	{
		super(market, symbol, type, name, iSIN, bLOOMBERG_CODE, rIC, lastUpdate, strike, expiry, price,
				isPriceInPercent);
		this.multiplier = multiplier;
	}

	@Override
	public String toString()
	{
		return "Leg [Type=" + this.type + ", multiplier=" + multiplier + "]";
	}
}
