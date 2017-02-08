package com.celera.gateway;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ESessionState
{
	AHT_CLOSE(10),
	AHT_CLOSE_E(22),
	AHT_CLR_INFO(11),
	AHT_INACT_T_ORDER(12),
	AHT_NEXT_DAY(13),
	AHT_OPEN(14),
	AHT_OPEN_PL(15),
	AHT_PRE_MKT_ACT(16),
	CLOSE(2),
	CLOSE_TODAY(18),
	CLOSE_TODAY_E(21),
	CL_CLOSE(9),
	CL_START(8),
	FAILOVER(20),
	OPEN(3),
	OPENALLOC(1),
	OPEN_DPL(19),
	OPEN_DPL_VCM(23),
	OPEN_PL(17),
	OPEN_VCM(24),
	PAUSE(6),
	PREOPEN(4),
	PREOPENALLOC(5),
	PRE_MKT_ACT(7),
	RESET_VCM(27),
	VCM_COOL_OFF(26),
	VCM_COOL_OFF_DPL(25),
	UNKNOWN(0)
	;
	
	private static final Map<Integer, ESessionState> map = new ConcurrentHashMap<Integer, ESessionState>();
	static
	{
		for (ESessionState e : ESessionState.values())
			map.put(e.asInt, e);
	}
	
	public static ESessionState get(final int asInt)
	{
		ESessionState ss = map.get(asInt);
		if (ss == null) {
			return UNKNOWN;
		}
		return ss;
	}

	public static ESessionState get(final byte asByte)
	{
		return get((int)asByte);
	}
	
	private final int asInt;

	ESessionState(int asInt) {
		this.asInt = asInt;
	};

	public int getAsInt()
	{
		return asInt;
	}
}