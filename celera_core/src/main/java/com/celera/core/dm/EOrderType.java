package com.celera.core.dm;

public enum EOrderType
{
	LIMIT("LIMIT", 1), 
	MARKET("MARKET", 2), 
	MARKET_TO_LIMIT("MARKET_TO_LIMIT", 3), 
	IOC("IOC", 4), 
	FOK("FOK", 5), 
	;
	
	private final String name;
	private final int value;

	EOrderType(String name, int value) {
		this.name = name;
		this.value = value;
	};

	public String getName() {
		return name;
	}
	
	public int value() {
		return value;
	}
}
