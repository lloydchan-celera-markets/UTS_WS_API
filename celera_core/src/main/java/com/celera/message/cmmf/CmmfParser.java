//package com.celera.message.cmmf;
//
//public class CmmfParser
//{
////	private static Map dba = new HashMap<String, ICmmfListener>();
//	
//	
////	public static void subscribe(ICmmfListener cb)
////	{
//////		if (cb instanceof DatabaseAdapter)
////			dba.put(cb.id(), cb);
////	}
//	
//	public static void parse(byte[] data, CmmfApp cb)
//	{
//		if (!cb.isMine((char) data[1]))
//			return;
//		
//		switch (data[2]) {
//		case 'A':
//			cb.onAdmin(data);
//			break;
//		case 'C':
//			cb.onCommand(data);
//			break;
//		default:
//			break;
//		}
//	}
//	
//	public static void dispatch() {
//		
//	}
//	
//	public static void main(String[] args)
//	{
//
//	}
//
//}