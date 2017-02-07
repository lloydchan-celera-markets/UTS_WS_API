package com.celera.core.dm;

//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import com.celera.message.cmmf.ECommand;

public enum EIsGiveup
{
	N((byte)0),
	Y((byte)1);
	
//	private static final Map<Integer, EOrderStatus> map = new LinkedHashMap<Integer, EOrderStatus>();
//	private static final Map<String, EOrderStatus> nameMap = new LinkedHashMap<String, EOrderStatus>();
//	static
//	{
//		for (EOrderStatus e : EOrderStatus.values()) {
//			map.put(e.value, e);
//			nameMap.put(e.name, e);
//		}
//	}
	
	private final byte value;

	EIsGiveup(byte value) {
		this.value = value;
	};

	public byte getValue()
	{
		return value;
	}

//	public static EIsGiveup get(final Integer asChar)
//	{
//		return map.get(asChar);
//	}
//
//	public static EIsGiveup get(final String name)
//	{
//		return nameMap.get(name);
//	}
}
