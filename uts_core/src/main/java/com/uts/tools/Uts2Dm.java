package com.uts.tools;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOrderStatus;

public class Uts2Dm
{
	final static Logger logger = LoggerFactory.getLogger(Uts2Dm.class);

	private final static String UTS_LOCAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	static final DateTimeFormatter UTS_FORMATTER = DateTimeFormatter.ofPattern(UTS_LOCAL_DATE_FORMAT);
	
	static final SimpleDateFormat UTS_SDF = new SimpleDateFormat("dd-MMM-yy");
	public static final String DB_NULL = "DBNull";
	static final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();

	private static final String DB_DATE_FORMATTER = "dd-MMM-yy";
	private final static SimpleDateFormat dbSdf = new SimpleDateFormat(DB_DATE_FORMATTER);
	
	public static EInstrumentType toInstrumentType(String code)
	{
		switch (code)
		{
		case "S":
			return EInstrumentType.STOCK;
		case "I":
			return EInstrumentType.INDEX;
		}
		String newCode = code.replace("_", "").replace("%", "_PERCENT");
		EInstrumentType type = EInstrumentType.OPTION;
		try
		{
			type = EInstrumentType.valueOf(newCode);
		}
		catch (Exception e)
		{
			logger.error("type=[{}], {}", code, e.getMessage());
		}
		return type;
	}
	
	public static EOrderStatus toStatus(String mode)
	{
		switch (mode)
		{
		case "INSERT":
			return EOrderStatus.ON_MARKET;
		}
		return null;
	}

	public static LocalDate toLocalDate(String time)
	{
		if (time == null)
			return null; // skip throw
		
		try
		{
			return LocalDate.parse(time, UTS_FORMATTER);
		}
		catch (Exception e)
		{
//			e.printStackTrace();	// time should not be null
			return null;
		}
	}

	public static String toDateString(Date d)
	{
		if (d == null)
			return null; // skip throw
		
		try
		{
			return UTS_SDF.format(d);
		}
		catch (Exception e)
		{
//			e.printStackTrace();	// time should not be null
			return null;
		}
	}
	
	public static Date toDate(String d)
	{
		if (d == null)
			return null; // skip throw
		
		try
		{
			return UTS_SDF.parse(d);
		}
		catch (Exception e)
		{
//			e.printStackTrace();	// time should not be null
			return null;
		}
	}
	
	public static String toLocalDate(LocalDate time)
	{
		if (time == null)
			return null; // skip throw
		
		try
		{
			return time.format(UTS_FORMATTER);
		}
		catch (Exception e)
		{
//			e.printStackTrace();	// time must be same format
			return null;
		}
	}
	
	public static String toLocalDateTime(LocalDateTime time)
	{
		if (time == null)
			return null; // skip throw
		
		try
		{
			return time.format(UTS_FORMATTER);
		}
		catch (Exception e)
		{
//			e.printStackTrace();	// time must be same format
			return null;
		}
	}

	public static Double toDouble(String s)
	{
		if (s == null || s.length() == 0 || DB_NULL.equals(s))
			return null; // skip throw
		try
		{
			return format.parse(s).doubleValue();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Boolean toBoolean(String s)
	{
		if (s == null || s.length() == 0 || DB_NULL.equals(s))
			return null; // skip throw
		try
		{
			return Boolean.valueOf(s);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Long toLong(String s)
	{
		if (s == null || s.length() == 0 || DB_NULL.equals(s))
			return null; // skip throw
		
		try
		{
			return format.parse(s).longValue();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static Integer toInt(String s)
	{
		if (s == null || s.length() == 0 || DB_NULL.equals(s))
			return null; // skip throw
		
		try
		{
			return format.parse(s).intValue();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
}
