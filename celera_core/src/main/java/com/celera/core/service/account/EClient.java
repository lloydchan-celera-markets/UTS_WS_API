//package com.celera.core.service.account;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public enum EClient
//{
//	BUY("BUY", 1), 
//	SELL("SELL", 2), 
//	SHORT_SELL("SHORT SELL", 3), 
//	SHORT_SELL_EXEMPT("SHORT SELL EXEMPT", 4), 
//	CROSS("CROSS", 5), 
//	;
//	
//	private static final Map<Integer, EClient> map = new LinkedHashMap<Integer, EClient>();
//	private static final Map<String, EClient> nameMap = new LinkedHashMap<String, EClient>();
//	static
//	{
//		for (EClient e : EClient.values())
//			map.put(e.asInt, e);
//		for (EClient e : EClient.values())
//			nameMap.put(e.name, e);
//	}
//	
//	public static EClient get(final int asInt)
//	{
//		return map.get(asInt);
//	}
//	
//	public static EClient get(final String asName)
//	{
//		return nameMap.get(asName.toUpperCase());
//	}
//	
//	private final String name;
//	private final int asInt;
//
//	EClient(String name, int asInt) {
//		this.name = name;
//		this.asInt = asInt;
//	};
//
//	public String getName() {
//		return name;
//	}
//
//	public int getAsInt()
//	{
//		return asInt;
//	}
////	public static ESide get(String side) {
////		switch (side) {
////		case "BUY" : return BUY;
////		case "SELL" : return SELL;
////		}
////		return BOTH;
////	}
//}