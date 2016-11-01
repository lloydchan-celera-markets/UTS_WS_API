package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ECommand
{
	QUERY_ALL_TRADES('A'), QUERY_TRADE_BETWEEN('B'), EMAIL_INVOICE('E'), CREATE_INVOICE('G'), UPDATE_CLIENT('C');

	private static final Map<Character, ECommand> map = new LinkedHashMap<Character, ECommand>();
	static
	{
		for (ECommand e : ECommand.values())
			map.put(e.asChar, e);
	}

	private final Character asChar;

	@Override
	public String toString()
	{
		return Character.toString(asChar);
	}
	
	public char asChar()
	{
		return asChar;
	}

	private ECommand(char asChar)
	{
		this.asChar = asChar;
	}

	public static ECommand get(final char asChar)
	{
		return map.get(asChar);
	}
}
