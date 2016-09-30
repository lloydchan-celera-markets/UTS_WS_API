package com.celera.core.dm;

public enum EOrderStatus
{
	ON_MARKET("ON_MARKET"), 
	SENT("SENT"), 
	INACTIVE("INACTIVE"), 
	;
	
	private final String name;

	EOrderStatus(String name) {
		this.name = name;
	};

	public String getName() {
		return name;
	}
}
