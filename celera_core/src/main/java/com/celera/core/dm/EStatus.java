package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EStatus
{
	ACTIVE('A', 1), SUSPEND('S', 2), ISSUED('I',3), DELISTED('D', 4), CLOSE('C', 5), OBSOLETED('O', 6);

	private static final Map<Character, EStatus> map = new LinkedHashMap<Character, EStatus>();
	private static final Map<Integer, EStatus> intMap = new LinkedHashMap<Integer, EStatus>();
	static
	{
		for (EStatus e : EStatus.values()) {
			map.put(e.asChar, e);
			intMap.put(e.asInt, e);
		}
	}
	
	private final char asChar;
	private final int asInt;

	EStatus(char _asChar, int _asInt)
	{
		this.asChar = _asChar;
		this.asInt = _asInt;
	};

	public char getChar()
	{
		return asChar;
	}

	public int getInt()
	{
		return asInt;
	}
	
	public static EStatus get(final char _asChar)
	{
		return map.get(_asChar);
	}

	public static EStatus get(final int _asInt)
	{
		return intMap.get(_asInt);
	}
}
