package com.celera.backoffice;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BOFormatter
{
	private static final Logger logger = LoggerFactory.getLogger(BOFormatter.class);
	
    private static final NumberFormat nf = new DecimalFormat("#,###.#");
	
	public static String displayFee(Double fee, String currency) throws Exception 
	{
		try {
			switch (currency)
			{
			case "HKD": return "HK$" + nf.format(fee);
			case "USD": return "US$" + nf.format(fee);
			case "KRW": return "US$" + nf.format(fee);
			case "JPY": return "JP$" + nf.format(fee);
			default: return currency + nf.format(fee); 
			}
		}
		catch (Exception e) {
			logger.error("fee[{}] currency[{}]", fee, currency, e);
			throw new Exception();
		}
	}

	public static String displayCurncy(String currency) 
	{
		switch (currency)
		{
		case "HKD": return "HK$";
		case "USD": return "US$";
		case "KRW": return "US$";
		case "JPY": return "JP$";
		default: return currency; 
		}
	}
	
	public static String displayNumber(Double fee) 
	{
		return nf.format(fee); 
	}
}
