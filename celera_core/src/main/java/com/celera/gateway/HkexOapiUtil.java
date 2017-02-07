package com.celera.gateway;

import org.apache.commons.lang3.StringUtils;

public class HkexOapiUtil
{
	public static final Integer SIZE_CUSTOMER_INFO = 7;
	public static final Integer SIZE_GIVEUP_MEMBER = 5;
	
	public static String rightPad(String s, int size) {
		String padStr = StringUtils.rightPad(s, size);
		return padStr == null ? StringUtils.right("", 7) : padStr;
	}
}
