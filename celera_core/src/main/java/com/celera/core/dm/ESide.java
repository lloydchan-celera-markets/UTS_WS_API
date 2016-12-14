package com.celera.core.dm;

public enum ESide
{
	BUY("BUY"), 
	SELL("SELL"), 
	BOTH("BOTH"), 
	;
	
	private final String name;

	ESide(String name) {
		this.name = name;
	};

	public String getName() {
		return name;
	}
}
