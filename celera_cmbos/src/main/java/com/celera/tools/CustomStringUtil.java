package com.celera.tools;

public class CustomStringUtil
{
	public static String replaceLastBy(String s, String find, String replace) {
//		String s = "November 2016, December 2016, January 2017";
//		s.substring(0, s.length() - 2);
//		int pos =  s.lastIndexOf(",");
		int pos =  s.lastIndexOf(find);
		if (pos < 0) 
			return s;
		
		String sub = s.substring(0, pos) + replace + s.substring(pos+find.length(), s.length());
//		String sub = s.substring(0, pos) + " and " + s.substring(pos+2, s.length());
		return sub;
//		System.out.println(sub);
	}
	
	
	public static void main(String[] args)
	{
		String s = "November 2016, December 2016, January 2017, ";
		s = s.substring(0, s.length() - 2);
		String sub = replaceLastBy(s, ", ", " and ");
//		int pos =  s.lastIndexOf(",");
//		String sub = s.substring(0, pos) + " and" + s.substring(pos+1, s.length());
		System.out.println(sub);
	}

}
