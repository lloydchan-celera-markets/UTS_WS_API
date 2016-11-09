package com.uts.tradeconfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.celera.tools.CSVReader;

public class InvoiceRegister {

	public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	public static SimpleDateFormat sdfMMMyy = new SimpleDateFormat("MMMyy");
	
	String date;
	String invoice;
	String customer;
	String accountNumber;
	String curncy;
	Double amount;
	
	public InvoiceRegister(String date, String invoice, String accountNumber, String curncy, String customer, Double amount) {
		this.date = date;
		this.invoice = invoice;
		this.customer = customer.toUpperCase();
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.curncy = curncy.toUpperCase();
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String key() {
		Date d;
		try {
			d = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MONTH, -1);
//			String dKey = sdfMMMyy.format(cal.getTime());
			String key = customer + "_" + curncy + "_" + sdfMMMyy.format(d);
			key = key.toUpperCase();
//			String key = customer + "_" + curncy + "_" + dKey;
System.out.println("===================================================map.key=" + key);
			return key;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public InvoiceRegister (){
	}
	
//	public static void main(String[] args) {
//		InvoiceRegister ir = CSVReader.map.get("");
//try {
//	Thread.sleep(1);
//} catch (InterruptedException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//	}

}
