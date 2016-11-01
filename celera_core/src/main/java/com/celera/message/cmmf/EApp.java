package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EApp
{
	DBA('D'), WEB_UI('W'), BOServiceManager('B');

	private static final Map<Character, EApp> map = new LinkedHashMap<Character, EApp>();
	static
	{
		for (EApp e : EApp.values())
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

	private EApp(char asChar)
	{
		this.asChar = asChar;
	}

	public static EApp get(final char asChar)
	{
		return map.get(asChar);
	}
}
