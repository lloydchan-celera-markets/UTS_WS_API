package com.celera.common;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public enum EMonthFuture 
{
	F(1), // JAN('F'),
	G(2), // FEB('G'),
	H(3), // MAR('H'),
	J(4), // APR('J'),
	K(5), // MAY('K'),
	M(6), // JUN('M'),
	N(7), // JUL('N'),
	Q(8), // AUG('Q'),
	U(9), // SEP('U'),
	V(10), // OCT('V'),
	X(11), // NOV('X'),
	Z(12); // DEC('Z');
	
	private static final Map<Integer, EMonthFuture> map = new LinkedHashMap<Integer, EMonthFuture>();
	static
	{
		for (EMonthFuture e : EMonthFuture.values())
			map.put(e.asInt, e);
	}

	private final Integer asInt;

	@Override
	public String toString()
	{
		return Integer.toString(asInt);
	}
	
	public int asInt()
	{
		return asInt;
	}

	private EMonthFuture(int asInt)
	{
		this.asInt = asInt;
	}

	public static EMonthFuture get(final int asInt)
	{
		return map.get(asInt);
	}

	public static EMonthFuture get(Date d)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int month = cal.get(Calendar.MONTH) + 1;
		return map.get(month);
	}
}
