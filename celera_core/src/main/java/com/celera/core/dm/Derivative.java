package com.celera.core.dm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Derivative extends Instrument implements IDerivative
{
	protected String strike;
	protected String expiry;
	protected Double price;
	protected final Boolean isPriceInPercent;
	protected Map<String, Leg> legs = new HashMap<String, Leg>();

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
	
	/**
	 * @param code   , flexible API, allow caller determine leg id
	 * @param leg
	 */
	public void addLeg(String id, IInstrument leg)
	{
		legs.put(id, (Leg) leg);
	}

	public Leg getLeg(String key) 
	{
		return legs.get(key);
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
		for (Leg e : legs.values())
		{
			s += e.toString() + ",";
		}
		s += "]] ";
		return s;
	}

	@Override
	public void setPrice(Double price)
	{
		this.price = price;
	}

	public void setStrike(String strike)
	{
		this.strike = strike;
	}

	public void setExpiry(String expiry)
	{
		this.expiry = expiry;
	}
}
