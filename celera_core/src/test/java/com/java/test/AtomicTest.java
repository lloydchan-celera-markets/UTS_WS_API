package com.java.test;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicTest
{

	public static void atomic_bool() {
		AtomicBoolean ab = new AtomicBoolean(true);
		boolean expect = true;
		boolean update = false;
		System.out.print("expect=" + expect +",update=" + update + ",actual=" + ab.get());
		boolean res = ab.compareAndSet(expect, update);
		System.out.println(", res=" + res + ",actual=" + ab.get());
		expect = false; update = true;
		System.out.print("expect=" + expect +",update=" + update + ",actual=" + ab.get());
		res = ab.compareAndSet(expect, update);
		System.out.println(", res=" + res + ",actual=" + ab.get());
		expect = true; update = true;
		System.out.print("expect=" + expect +",update=" + update + ",actual=" + ab.get());
		res = ab.compareAndSet(expect, update);
		System.out.println(", res=" + res + ",actual=" + ab.get());
		expect = false; update = false;
		System.out.print("expect=" + expect +",update=" + update + ",actual=" + ab.get());
		res = ab.compareAndSet(expect, update);
		System.out.println(", res=" + res + ",actual=" + ab.get());
	}
	
	public static void main(String[] args)
	{
		atomic_bool();
	}

}
