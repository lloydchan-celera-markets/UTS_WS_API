package com.celera.message.cmmf;

public interface ICmmfConst
{
	public static final String DATE_FMT = "yyyyMMdd";
	// cmmf header
	public static final int HEADER_SIZE = 4;
	public static final int HEADER_SENDER_POS = 0;
	public static final int HEADER_RECEIVER_POS = 1;
	public static final int HEADER_MESSAGE_TYPE_POS = 2;
	public static final int HEADER_COMMAND_POS = 3;
	
	public static final int DATE_LENGTH = 8;
}
