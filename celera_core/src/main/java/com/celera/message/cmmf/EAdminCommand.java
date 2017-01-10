package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EAdminCommand
{
	SOD('S'),
	EOD('E');

	private static final Map<Character, EAdminCommand> map = new LinkedHashMap<Character, EAdminCommand>();
	static
	{
		for (EAdminCommand e : EAdminCommand.values())
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

	private EAdminCommand(char asChar)
	{
		this.asChar = asChar;
	}

	public static EAdminCommand get(final char asChar)
	{
		return map.get(asChar);
	}
}
