package com.celera.core.common;

public class XNodeFactory
{
	public static <T> Object build(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		Class<T> _clazz;
		_clazz = clazz;
		return _clazz.newInstance();
	}
	
	public static void main(String[] args) 
	{
		try
		{
			Object o = XNodeFactory.build(Integer.class);
//			Integer o = Integer.class.newInstance();
			System.out.println(o);
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
