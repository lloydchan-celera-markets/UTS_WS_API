package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EOGAdmin
{
	LOGIN('I'), 
	LOGOUT('O'), 
	CHANGE_PASSWORD('C'),
	SOD('S'),
	SUBSCRIBE_MARKET_DATA('M'),
	UNSUBSCRIBE_MARKET_DATA('U');

	private final char asChar;

	EOGAdmin(char c)
	{
		this.asChar = c;
	};

	public char getChar()
	{
		return asChar;
	}
	
	private static final Map<Character, EOGAdmin> map = new LinkedHashMap<Character, EOGAdmin>();
	static
	{
		for (EOGAdmin e : EOGAdmin.values())
			map.put(e.asChar, e);
	}

	public static EOGAdmin get(final char asChar)
	{
		return map.get(asChar);
	}
}
