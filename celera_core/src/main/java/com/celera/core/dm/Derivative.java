package com.celera.core.dm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Derivative extends Instrument
{
	protected final String strike;
	protected final String expiry;
	protected final Double price;
	protected final Boolean isPriceInPercent;
	protected List<Leg> legs = new ArrayList<Leg>();

	public Derivative(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE,
			String rIC, LocalDate lastUpdate, String strike, String expiry, Double price,
			Boolean isPriceInPercent)
	{
		super(market, symbol, type, name, iSIN, bLOOMBERG_CODE, rIC, lastUpdate);
		this.strike = strike;
		this.expiry = expiry;
		this.price = price;
		this.isPriceInPercent = isPriceInPercent;
	}
	
	public void addLeg(IInstrument leg)
	{
		legs.add((Leg) leg);
	}

	@Override
	public String key()
	{
		String key = super.key();
		key += (strike == null ? "" : "_" + strike);
		key += (expiry == null ? "" : "_" + strike);
		return key;
	}
	
	@Override
	public String toString()
	{
		String s = super.toString();
		s += "Derivative [strike=" + strike + ", expiry=" + expiry + ", price=" + price + ", isPriceInPercent="
				+ isPriceInPercent + ", Legs[";
		for (Leg g : legs)
		{
			s += g.toString() + ",";
		}
		s += "]] ";
		return s;
	}
}
