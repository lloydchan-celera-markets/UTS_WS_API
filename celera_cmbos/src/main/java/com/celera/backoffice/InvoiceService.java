package com.celera.backoffice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceService
{
	public static String PREFIX_INVOICE = "CEL-"; 
	public static final AtomicInteger id = new AtomicInteger(0);
	public static final SimpleDateFormat sdf_yy = new SimpleDateFormat("yy");
	
	public static String invoiceId(Date invoiceDate) {
		String yy = sdf_yy.format(invoiceDate);
		int newId = id.incrementAndGet();
		return PREFIX_INVOICE + yy + String.format("%04d",  newId);
	}
	
	public static void main(String[] args) 
	{
		System.out.println(String.format("%04d",  1));
		System.out.println(String.format("%04d",  54321));
		System.out.println(InvoiceService.invoiceId(new Date()));
	}
}
