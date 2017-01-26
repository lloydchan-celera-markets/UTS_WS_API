package com.celera.message.cmmf;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOGAdmin;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EStatus;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.Instrument;


public class CmmfParser
{
	static final Logger logger = LoggerFactory.getLogger(CmmfParser.class);
	
//	private static Map dba = new HashMap<String, ICmmfListener>();
	
	
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

//	public static CmmfOrderResponse parse(byte[] data)
	public static void parse(byte[] data)
	{
//		for (int i=0; i<data.length; i++) {
//			System.out.print(data[i] + ",");
//		}
//		System.out.println("");
		ByteBuffer buf = ByteBuffer.allocate(data.length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(data);
		buf.flip();
		EApp sender = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		ECommand cmd = ECommand.get((char)buf.get());
		Long id = buf.getLong();
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		String reason = new String(data, 12, 50);
		logger.info("sender[{}], type[{}], cmd[{}] id[{}] status[{}] reason[{}]",
				sender, type, cmd, id, status, reason);
	}
	
	public static void parseCmmfOrderResponse(byte[] data, ICmmfProcessor cb)
	{
		ByteBuffer buf = ByteBuffer.allocate(data.length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(data);
		buf.flip();
		EApp sender = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		ECommand cmd = ECommand.get((char)buf.get());
		Long id = buf.getLong();
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		String reason = new String(data, 12, 50);
		logger.info("sender[{}], type[{}], cmd[{}] id[{}] status[{}] reason[{}]",
				sender, type, cmd, id, status, reason);
		
//		cb.onTradeReport(id, status, reason);
	}
	
	public static void parseCmmfTradeReportResponse(byte[] data, ICmmfProcessor cb)
	{
		ByteBuffer buf = ByteBuffer.allocate(data.length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(data);
		buf.flip();
		EApp sender = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		ECommand cmd = ECommand.get((char)buf.get());
		Long id = buf.getLong();
//		Long refId = buf.getLong();
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		String reason = new String(data, 12, 50);
		logger.info("sender[{}], type[{}], cmd[{}] order_id[{}] status[{}] reason[{}]",
				sender, type, cmd, id, status, reason);
		
		cb.onTradeReport(id, status, reason);
	}
	
	public static void parseCmmfInstrumentUpdateResponse(byte[] data, ICmmfProcessor cb)
	{
		ByteBuffer buf = ByteBuffer.allocate(data.length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(data);
		buf.flip();
		EApp sender = EApp.get((char)buf.get());
		EMessageType msgType = EMessageType.get((char)buf.get());
		ECommand cmd = ECommand.get((char)buf.get());
		
		byte[] bSymbol = new byte[32];
		buf.get(bSymbol, 0, 32);
//		buf.position(32);
		String symbol = new String(bSymbol);
		byte bSts = (byte)buf.get();
		EStatus status = EStatus.get(bSts);
//		String reason = new String(data, 12, 50);
		logger.info("sender[{}], type[{}], cmd[{}] symbol[{}] status[{}]",
				sender, msgType, cmd, symbol, status);
		
		cb.onInstrumentUpdate(symbol, status);
	}
	
	public static void print(byte[] data) 
	{
		for (int i=0; i<data.length; i++) {
			System.out.print(data[i] + ",");
		}
		System.out.println("");
	}
	
	public static boolean parseCmmfOgAdminResponse(byte[] data)
	{
		print(data);
		
		ByteBuffer buf = ByteBuffer.allocate(data.length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(data);
		buf.flip();
		EApp sender = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		ECommand cmd = ECommand.get((char)buf.get());
		EOGAdmin action = EOGAdmin.get((char)buf.get());
		byte result = buf.get();
//		String reason = new String(data, 12, 50);
//		logger.info("sender[{}], type[{}], cmd[{}] action[{}] result[{}] reason[{}]",
//				sender, type, cmd, action, result, reason);
		logger.info("sender[{}], type[{}], cmd[{}] action[{}] result[{}]",
				sender, type, cmd, action, result);
		
		return result == 0x01;
	}
	
	public static void dispatch() {
		
	}
	
	public static void main(String[] args)
	{

	}
}
