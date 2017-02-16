package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ECommand
{
	// Common
	QUERY_ALL_TRADES('A'), 
	QUERY_TRADE_BETWEEN('B'),
	// BO Service command
	EMAIL_INVOICE('E'),
	EMAIL_BATCH_INVOICE('F'),
	CREATE_INVOICE('G'), 
	UPDATE_CLIENT('C'),
	QUERY_ALL_INVOICES('I'),
	UPDATE_INVOICE('U'),
	QUERY_UTS_SUMMARY('S'),
	REMOVE_INVOICE('T'),
	LOG('L'),
	// Order Gateway command
	ORDER_REQUEST('O'),
	BLOCK_TRADE_REPORT('N'),
	TRADE_REPORT('R'),
	TRADE('T'),
	UPDATE_INSTRUMENT('P'),
	ADMIN_REQUEST('H'),
	SOD('S'),
	OG_QUERY('Q'),
	LAST_PRICE('L'),
	REJECT('J');

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
