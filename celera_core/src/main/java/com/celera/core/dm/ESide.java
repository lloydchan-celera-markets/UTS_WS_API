package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

import com.celera.message.cmmf.ECommand;

public enum ESide
{
	BUY("BUY", 1), 
	SELL("SELL", 2), 
	SHORT_SELL("SHORT_SELL", 3), 
	SHORT_SELL_EXEMPT("SHORT_SELL_EXEMPT", 4), 
	CROSS("CROSS", 5), 
	;
	
	private static final Map<Integer, ESide> map = new LinkedHashMap<Integer, ESide>();
	private static final Map<String, ESide> nameMap = new LinkedHashMap<String, ESide>();
	static
	{
		for (ESide e : ESide.values())
			map.put(e.asInt, e);
		for (ESide e : ESide.values())
			nameMap.put(e.name, e);
	}
	
	public static ESide get(final int asInt)
	{
		return map.get(asInt);
	}
	
	public static ESide get(final String asName)
	{
		return nameMap.get(asName.toUpperCase());
	}
	
	private final String name;
	private final int asInt;

	ESide(String name, int asInt) {
		this.name = name;
		this.asInt = asInt;
	};

	public String getName() {
		return name;
	}

	public int getAsInt()
	{
		return asInt;
	}
//	public static ESide get(String side) {
//		switch (side) {
//		case "BUY" : return BUY;
//		case "SELL" : return SELL;
//		}
//		return BOTH;
//	}
}
