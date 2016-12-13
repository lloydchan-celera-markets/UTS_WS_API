package com.celera.core.dm;

public enum EOrderType
{
	LIMIT("LIMIT"), 
	MARKET("MARKET"), 
	IOC("IOC"), 
	FOK("FOK"), 
	;
	
	private final String name;

	EOrderType(String name) {
		this.name = name;
	};

	public String getName() {
		return name;
	}
}
