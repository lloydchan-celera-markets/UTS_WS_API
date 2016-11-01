package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EMessageType 
{
	ADMIN('A'), 
	QUERY('Q'), 
	RESPONSE('R'),
	TASK('T');
	
	private static final Map<Character, EMessageType> map = new LinkedHashMap<Character, EMessageType>();
	static
	{
		for (EMessageType e : EMessageType.values())
			map.put(e.asChar, e);
	}
	
	private final Character asChar;
	
	@Override
	public String toString()
	{
		return Character.toString(asChar);
	}
	
	public char asChar()
	{
		return asChar;
	}

	private EMessageType(char asChar)
	{
		this.asChar = asChar;
	}

	public static EMessageType get(final char asChar)
	{
		return map.get(asChar);
	}
}
