package com.celera.message.cmmf;

public interface ICmmfConst
{
	public static final String DATE_FMT = "yyyyMMdd";
	public static final String MONTH_FORMAT= "yyyyMM";
	public static final String JSON_LOG_DATE_FORMAT= "yyyy-MM-dd HH:mm:ss";
	// cmmf header
	public static final int HEADER_SIZE = 4;
	public static final int HEADER_SENDER_POS = 0;
	public static final int HEADER_RECEIVER_POS = 1;
	public static final int HEADER_MESSAGE_TYPE_POS = 2;
	public static final int HEADER_COMMAND_POS = 3;
	
	public static final int DATE_LENGTH = 8;
	public static final int MONTH_LENGTH = 6;
	public static final int DOC_ID_LENGTH = 24;
}
