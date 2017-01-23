package com.celera.core.dm;

import java.util.HashMap;
import java.util.Map;

public class Derivative extends Instrument implements IDerivative
{
	protected Double strike;
	protected Double delta;
	protected String expiry;
	protected Double price;
	protected final Boolean isPriceInPercent;
	protected Map<String, Leg> legs = new HashMap<String, Leg>();

	public Derivative(String market, String symbol, EInstrumentType type, String name, String iSIN, String bLOOMBERG_CODE,
			String rIC, Double strike, String expiry, Double price,
			Boolean isPriceInPercent, Double delta)
	{
		super(market, symbol, type, name, iSIN, bLOOMBERG_CODE, rIC);
		this.strike = strike;
		this.delta = delta;
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
	
	public String getExpiry()
	{
		return expiry;
	}

	public void setExpiry(String expiry)
	{
		this.expiry = expiry;
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
				+ isPriceInPercent + ",delta=" + delta + ", Legs[";
		for (Leg e : legs.values())
		{
			s += e.toString() + ",";
		}
		s += "]] ";
		return s;
	}

//	@Override
	public void setPrice(Double price)
	{
		this.price = price;
	}

	public void setStrike(Double strike)
	{
		this.strike = strike;
	}

	@Override
	public Double getStrike()
	{
		return strike;
	}

	@Override
	public Double getDelta()
	{
		return delta;
	}
}
