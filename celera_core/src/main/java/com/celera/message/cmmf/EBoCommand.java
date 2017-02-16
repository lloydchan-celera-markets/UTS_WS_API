package com.celera.message.cmmf;

import java.util.LinkedHashMap;
import java.util.Map;

public enum EBoCommand
{
	// Common
	QUERY_ALL_TRADES('A'), 
	QUERY_TRADE_BETWEEN('B'),
	UPDATE_CLIENT('C'),
	// BO Service command
	EMAIL_INVOICE('E'),
	EMAIL_BATCH_INVOICE('F'),
	CREATE_INVOICE('G'), 
	QUERY_ALL_INVOICES('I'),
	LOG('L'),
	QUERY_UTS_SUMMARY('S'),
	REMOVE_INVOICE('T'),
	UPDATE_INVOICE('U');

//	// Order Gateway command
//	ADMIN_REQUEST('H'),
//	REJECT('J'),
//	LAST_PRICE('L'),
//	BLOCK_TRADE_REPORT('N'),
//	ORDER_REQUEST('O'),
//	UPDATE_INSTRUMENT('P'),
//	OG_QUERY('Q'),
//	TRADE_REPORT('R'),
//	SOD('S'),
//	TRADE('T');

	private static final Map<Character, EBoCommand> map = new LinkedHashMap<Character, EBoCommand>();
	static
	{
		for (EBoCommand e : EBoCommand.values())
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

	private EBoCommand(char asChar)
	{
		this.asChar = asChar;
	}

	public static EBoCommand get(final char asChar)
	{
		return map.get(asChar);
	}
}
