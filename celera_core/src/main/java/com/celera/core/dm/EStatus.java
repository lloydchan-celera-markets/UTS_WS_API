package com.celera.core.dm;

public enum EStatus
{
	ACTIVE('A'), SUSPEND('S'), CLOSE('C'), OBSOLETED('O');

	private final char value;

	EStatus(char c)
	{
		this.value = c;
	};

	public char getChar()
	{
		return value;
	}
}
