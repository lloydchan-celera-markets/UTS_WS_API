package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EStatus
{
	ACTIVE('A', (byte)1), SUSPEND('S', (byte)2), ISSUED('I', (byte)3), DELISTED('D', (byte)4), CLOSE('C', (byte)5), OBSOLETED('O', (byte)6);

	private static final Map<Character, EStatus> map = new LinkedHashMap<Character, EStatus>();
	private static final Map<Byte, EStatus> bMap = new LinkedHashMap<Byte, EStatus>();
	static
	{
		for (EStatus e : EStatus.values()) {
			map.put(e.value, e);
			bMap.put(e.b, e);
		}
	}
	
	private final char value;
	private final byte b;

	EStatus(char c, byte b)
	{
		this.value = c;
		this.b = b;
	};

	public char getChar()
	{
		return value;
	}
	
	public static EStatus get(final char asChar)
	{
		return map.get(asChar);
	}

	public static EStatus get(final byte as)
	{
		return bMap.get(as);
	}
}
