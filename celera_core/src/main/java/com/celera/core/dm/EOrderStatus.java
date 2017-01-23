package com.celera.core.dm;

import java.util.LinkedHashMap;
import java.util.Map;

import com.celera.message.cmmf.ECommand;

public enum EOrderStatus
{
	UNSENT("UNSENT", 0),
	PENDING_NEW("PENDING_NEW", 1),
	PENDING_AMEND("PENDING_AMEND", 2),
	PENDING_CANCEL("PENDING_CANCEL", 3),
	ON_MARKET("ON_MARKET", 4),
	CANCELLED("CANCELLED", 5),
	FILLED("FILLED", 6),
	PARTIAL_FILLED("PARTIAL_FILLED", 7),
	REJECTED("REJECTED", 8),
	AMEND_REJECTED("AMEND_REJECTED", 9),
	CANCEL_REJECTED("CANCEL_REJECTED", 10),
	EXPIRED("EXPIRED", 11),
	AMEND("AMEND", 12),
	UNBOOK("UNBOOK", 13),
	UNKNOWN("UNKNOWN", 14),
	INACTIVE("INACTIVE", 15),
	SENT("SENT", 16),
	NEW("NEW", 17),
	PARTIAL_SENT("PARTIAL_SENT", 18),
	PARTIAL_REJECT("PARTIAL_REJECT", 19),
	;
	
	private static final Map<Integer, EOrderStatus> map = new LinkedHashMap<Integer, EOrderStatus>();
	private static final Map<String, EOrderStatus> nameMap = new LinkedHashMap<String, EOrderStatus>();
	static
	{
		for (EOrderStatus e : EOrderStatus.values()) {
			map.put(e.value, e);
			nameMap.put(e.name, e);
		}
	}
	
	private final String name;
	private final int value;

	EOrderStatus(String name, int value) {
		this.name = name;
		this.value = value;
	};

	public String getName() {
		return name;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public static EOrderStatus get(final Integer asChar)
	{
		return map.get(asChar);
	}

	public static EOrderStatus get(final String name)
	{
		return nameMap.get(name);
	}
}
