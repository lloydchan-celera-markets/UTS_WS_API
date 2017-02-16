package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EFoCommand
{
	// Order Gateway command
	ADMIN_REQUEST('H'), 
	REJECT('J'), 
	LAST_PRICE('L'), 
	BLOCK_TRADE_REPORT('N'), 
	ORDER_REQUEST('O'), 
	UPDATE_INSTRUMENT('P'), 
	OG_QUERY('Q'), 
	TRADE_REPORT('R'), 
	SOD('S'), 
	TRADE('T');
	
	private static final Map<Character, EFoCommand> map = new LinkedHashMap<Character, EFoCommand>();
	static
	{
		for (EFoCommand e : EFoCommand.values())
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

	private EFoCommand(char asChar)
	{
		this.asChar = asChar;
	}

	public static EFoCommand get(final char asChar)
	{
		return map.get(asChar);
	}
}
