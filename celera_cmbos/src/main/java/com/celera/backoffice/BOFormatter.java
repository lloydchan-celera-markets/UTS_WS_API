package com.celera.backoffice;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BOFormatter
{
    private static final NumberFormat nf = new DecimalFormat("#,###.#");
	
	public static String displayFee(Double fee, String currency) 
	{
		switch (currency)
		{
		case "HKD": return "HK$" + nf.format(fee);
		case "USD": return "US$" + nf.format(fee);
		default: return currency + nf.format(fee); 
		}
	}
	
	public static String displayNumber(Double fee) 
	{
		return nf.format(fee); 
	}
}