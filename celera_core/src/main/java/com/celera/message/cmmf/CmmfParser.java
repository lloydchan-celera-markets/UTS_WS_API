package com.celera.message.cmmf;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EAdminAction;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EStatus;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.Instrument;
import com.celera.gateway.ESessionState;
import com.celera.gateway.HkexOapiUtil;


public class CmmfParser
{
	static final Logger logger = LoggerFactory.getLogger(CmmfParser.class);
	
	static final int SIZEOF_REASON = 150;
	
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
		EApp recv = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		Long id = buf.getLong();
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		String reason = new String(data, 12, SIZEOF_REASON);
		reason = reason.trim();
		logger.info("sender[{}], type[{}], cmd[{}] id[{}] status[{}] reason[{}]",
				sender, type, cmd, id, status, reason);
	}
	
	public static void parseCmmfOrderResponse(ByteBuffer buf, ICmmfProcessor cb)
	{
		byte data[] = buf.array();
		EApp sender = EApp.get((char)buf.get());
		EApp recv = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		Long id = buf.getLong();
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		String reason = new String(data, 12, SIZEOF_REASON);
		reason = reason.trim();
		logger.info("sender[{}], type[{}], cmd[{}] id[{}] status[{}] reason[{}]",
				sender, type, cmd, id, status, reason);
		
		cb.onOrder(id, status, reason);
	}
	
	public static void parseCmmfTradeResponse(ByteBuffer buf, ICmmfProcessor cb)
	{
		EApp sender = EApp.get((char)buf.get());
		EApp recv = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		Long id = buf.getLong();
		Long tradeId = buf.getLong();
		Long lPrice = buf.getLong();
		double price = (double) lPrice / (double) IInstrument.CMMF_PRICE_FACTOR;
		Long qty = buf.getLong();
		
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		Integer giveupNum = buf.getInt();
		logger.info("sender[{}], type[{}], cmd[{}] order_id[{}] trade_id[{}] status[{}] giveupNum[{}]",
				sender, type, cmd, id, tradeId, status, giveupNum);
		
		cb.onTrade(id, tradeId, price, (int) (long) qty, status, giveupNum);
//		cb.onTrade(id, status, "", giveupNum);
	}
	
	public static void parseCmmfTradeReportResponse(ByteBuffer buf, ICmmfProcessor cb)
	{
		byte data[] = buf.array();
		
		EApp sender = EApp.get((char)buf.get());
		EApp recv = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		Long id = buf.getLong();
//		Long refId = buf.getLong();
		EOrderStatus status = EOrderStatus.get((int)buf.get());
		int pos = buf.position();
		String reason = new String(data, 12, SIZEOF_REASON);
		buf.position(pos + SIZEOF_REASON);
		reason = reason.trim();
//		Integer giveupNum = buf.getInt();
//		logger.info("sender[{}], type[{}], cmd[{}] order_id[{}] status[{}] giveupNum[{}] reason[{}]",
//				sender, type, cmd, id, status, giveupNum, reason);
		logger.info("sender[{}], type[{}], cmd[{}] order_id[{}] status[{}] reason[{}]",
				sender, type, cmd, id, status, reason);
		cb.onTradeReport(id, status, reason/*, giveupNum*/);
	}
	
	public static void parseCmmfInstrumentUpdateResponse(ByteBuffer buf, ICmmfProcessor cb)
	{
		EApp sender = EApp.get((char)buf.get());
		EApp recv = EApp.get((char)buf.get());
		EMessageType msgType = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		
		byte[] bSymbol = new byte[32];
		buf.get(bSymbol, 0, 32);
//		buf.position(32);
		String symbol = new String(bSymbol);
		byte bSts = (byte)buf.get();
		ESessionState ss = ESessionState.get(bSts);
		EStatus status = HkexOapiUtil.toState(ss);
//		String reason = new String(data, 12, SIZEOF_REASON);
		logger.info("sender[{}], type[{}], cmd[{}] symbol[{}] sessionstate[{}] status[{}]",
				sender, msgType, cmd, symbol, ss, status);
		
		cb.onInstrumentUpdate(symbol, status);
	}

	public static void parseCmmfLastPriceResponse(ByteBuffer buf, ICmmfProcessor cb)
	{
		EApp sender = EApp.get((char)buf.get());
		EApp recv = EApp.get((char)buf.get());
		EMessageType msgType = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		
		byte[] bSymbol = new byte[32];
		buf.get(bSymbol, 0, 32);
//		buf.position(32);
		String symbol = new String(bSymbol);
		symbol = symbol.trim();
		Long lPrice = buf.getLong();
		double price = (double) lPrice / (double) IInstrument.CMMF_PRICE_FACTOR;
		logger.info("sender[{}], type[{}], cmd[{}] symbol[{}] price[{}]",
				sender, msgType, cmd, symbol, lPrice);
		
		cb.onLastPrice(symbol, price);
	}
	
	public static void print(byte[] data) 
	{
		for (int i=0; i<data.length; i++) {
			System.out.print(data[i] + ",");
		}
		System.out.println("");
	}
	
	public static boolean parseCmmfOgAdminResponse(ByteBuffer buf)
	{
		EApp sender = EApp.get((char)buf.get());
		EApp recv = EApp.get((char)buf.get());
		EMessageType type = EMessageType.get((char)buf.get());
		EFoCommand cmd = EFoCommand.get((char)buf.get());
		EAdminAction action = EAdminAction.get((char)buf.get());
		byte result = buf.get();
//		String reason = new String(data, 12, SIZEOF_REASON);
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
