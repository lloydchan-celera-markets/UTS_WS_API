package com.java.common.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest
{

	public static void testDateFormat(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date d;
		try
		{
			d = sdf.parse("30-10-2016");
			System.out.println(d);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static void main(String[] args)
	{
		testDateFormat();

	}

}
