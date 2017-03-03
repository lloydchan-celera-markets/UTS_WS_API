package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EAdminAction
{
	LOGIN('I'), 
	LOGOUT('O'), 
	CHANGE_PASSWORD('C'),
	SOD('S'),
	SUBSCRIBE_MARKET_DATA('M'),
	UNSUBSCRIBE_MARKET_DATA('U'),
	SET_READY('R');

	private final char asChar;

	EAdminAction(char c)
	{
		this.asChar = c;
	};

	public char getChar()
	{
		return asChar;
	}
	
	private static final Map<Character, EAdminAction> map = new LinkedHashMap<Character, EAdminAction>();
	static
	{
		for (EAdminAction e : EAdminAction.values())
			map.put(e.asChar, e);
	}

	public static EAdminAction get(final char asChar)
	{
		return map.get(asChar);
	}
}
