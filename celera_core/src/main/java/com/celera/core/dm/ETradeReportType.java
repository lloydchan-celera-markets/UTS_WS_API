package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ETradeReportType
{
	T1_SELF_CROSS("T1", 10), 
	T2_COMBO_CROSS("T2", 11), 
	T4_INTERBANK_CROSS("T4", 7)
	;
	
	private final String name;
	private final int value;

	private static final Map<String, ETradeReportType> map = new LinkedHashMap<String, ETradeReportType>();
	static
	{
		for (ETradeReportType e : ETradeReportType.values())
			map.put(e.name, e);
	}
	
	ETradeReportType(String name, int value) {
		this.name = name;
		this.value = value;
	};

	public String getName() {
		return name;
	}
	
	public int value() {
		return value;
	}
	
	public static ETradeReportType get(final String t)
	{
		return map.get(t);
	}
}
