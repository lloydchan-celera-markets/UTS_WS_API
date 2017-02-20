package com.celera.core.dm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ESessionState
{
	T("T", (byte)0x00), 
	T_PLUS_1("T_PLUS_1", (byte)0x03);
	
	private static final Map<Byte, ESessionState> map = new ConcurrentHashMap<Byte, ESessionState>();
	private static final Map<String, ESessionState> nameMap = new ConcurrentHashMap<String, ESessionState>();
	static
	{
		for (ESessionState e : ESessionState.values())
			map.put(e.value, e);
		for (ESessionState e : ESessionState.values())
			nameMap.put(e.name, e);
	}
	
	public static ESessionState get(final int asInt)
	{
		ESessionState ss = map.get(asInt);
		if (ss == null) {
			return T;
		}
		return ss;
	}
	
	public static ESessionState get(final String asName)
	{
		ESessionState ss = nameMap.get(asName);
		if (ss == null) {
			return T;
		}
		return ss;
	}
	
	private final String name;
	private final byte value;

	ESessionState(String name, byte value) {
		this.name = name;
		this.value = value;
	};

	public String getName() {
		return name;
	}
	
	public byte value() {
		return value;
	}
}
