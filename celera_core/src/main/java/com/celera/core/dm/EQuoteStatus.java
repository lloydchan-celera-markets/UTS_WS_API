package com.celera.core.dm;

public enum EQuoteStatus
{
	INSERT("INSERT");
	
	private final String name;

	EQuoteStatus(String name) {
		this.name = name;
	};

	public String getName() {
		return name;
	}
}
