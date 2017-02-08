package com.celera.gateway;

import org.apache.commons.lang3.StringUtils;

import com.celera.core.dm.EStatus;

public class HkexOapiUtil
{
	public static final Integer SIZE_CUSTOMER_INFO = 7;
	public static final Integer SIZE_GIVEUP_MEMBER = 5;
	
	public static String rightPad(String s, int size) {
		String padStr = StringUtils.rightPad(s, size);
		return padStr == null ? StringUtils.right("", 7) : padStr;
	}

	@SuppressWarnings("incomplete-switch")
	public static EStatus toState(ESessionState ss) {
		switch (ss) {
		case AHT_OPEN: 
		case AHT_OPEN_PL :
		case AHT_PRE_MKT_ACT:
		case OPEN:
		case OPENALLOC:
		case OPEN_DPL:
		case OPEN_DPL_VCM:
		case OPEN_PL:
		case OPEN_VCM:
			return EStatus.ACTIVE;
		case CLOSE:
		case CLOSE_TODAY:
		case CLOSE_TODAY_E:
		case CL_CLOSE:
			return EStatus.CLOSE;
//		case CL_START(8),
//		case FAILOVER(20),
//		case PAUSE(6),
//		case PREOPEN(4),
		default:
			return EStatus.CLOSE;		
		}
	}
	
}
