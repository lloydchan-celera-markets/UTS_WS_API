package com.celera.gateway;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOGAdmin;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EOrderType;
import com.celera.core.dm.ESide;
import com.celera.core.dm.IDerivative;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;
import com.celera.core.dm.Instrument;
import com.celera.core.dm.Order;
import com.celera.core.dm.TradeReport;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineServer;
import com.celera.ipc.PipelineSinkCollector;
import com.celera.ipc.URL;
import com.celera.message.cmmf.CmmfBuilder;
import com.celera.message.cmmf.CmmfParser;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.EMessageType;
import com.celera.message.cmmf.ICmmfListener;

public class HkexOapiGateway implements ILifeCycle, ICmmfListener, IOrderGatewayService
{
	Logger logger = LoggerFactory.getLogger(HkexOapiGateway.class);

	private static final int CMMF_LOGIN_ADMIN_SIZE = 68;
	
	public static final URL PUSH_URL;// = "tcp://10.100.1.119:20010";
	public static final URL SINK_URL;// = "tcp://10.100.1.119:20011";
	
	private final PipelineSinkCollector sink;
	private final PipelineServer server;
	
	
	private AtomicBoolean isWaitAdminResp = new AtomicBoolean(false);
	
	static
	{
		String protocol = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_PUSH_PROT);
		String ip = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_PUSH_IP);
		Integer port = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_PUSH_PORT));
		PUSH_URL = new URL(protocol, ip, port);

		protocol = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_SINK_PROT);
		ip = ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_SINK_IP);
		port = Integer.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_HKEX_GATEWAY_CHL_SINK_PORT));
		SINK_URL = new URL(protocol, ip, port);
	}
	
	public HkexOapiGateway()
	{
		sink = new PipelineSinkCollector(SINK_URL.toString(), this);
		server = new PipelineServer(PUSH_URL.toString(), this);
	}
	
	@Override
	public byte[] onMessage(byte[] buf)
	{
		return null;
	}

	@Override
	public byte[] onQuery(byte[] data)
	{
		return null;
	}

	@Override
	public void onAdmin(byte[] data)
	{
	}

	@Override
	public void onResponse(byte[] data)
	{
		ECommand cmd = ECommand.get((char)data[2]);
		switch (cmd) {
		case ORDER_REQUEST: 
		case TRADE_REPORT: 
		{
			CmmfParser.parseCmmfOrderResponse(data);
			break;
		}
		case ADMIN_REQUEST: {
			boolean result = CmmfParser.parseCmmfOgAdminResponse(data);
			isWaitAdminResp.set(false);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onTask(byte[] data)
	{
		ECommand cmd = ECommand.get((char)data[2]);
		switch (cmd) {
		case ORDER_REQUEST: {
			onResponse(data);
			break;
		}
//		case TASK: {
//			onResponse(data);
//			break;
//		}
		default:
			break;
		}
	}

	@Override
	public void onSink(byte[] data)
	{
		EMessageType type = EMessageType.get((char)data[1]);
		switch (type) {
		case RESPONSE: {
			onResponse(data);
			break;
		}
//		case TASK: {
//			onResponse(data);
//			break;
//		}
		default:
			break;
		}
	}

	@Override
	public void init()
	{
		sink.init();
		server.init();
	}

	@Override
	public void stop()
	{
		server.stop();
		sink.stop();
	}
	
	public void start()
	{
		sink.start();
		server.start();
	}

	
	
	@Override
	public void createOrder(IOrder o)
	{
		try
		{
			o.setStatus(EOrderStatus.PENDING_NEW);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, ECommand.ORDER_REQUEST, b);
//			JsonObject json = o.json();
//			byte b[] = json.toString().getBytes();
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void createTradeReport(IOrder o)
	{
		try
		{
			o.setStatus(EOrderStatus.PENDING_NEW);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, ECommand.TRADE_REPORT, b);
//			JsonObject json = o.json();
//			byte b[] = json.toString().getBytes();
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void createBlockTradeReport(List<IOrder> tradeReportList, Long id, String synSymbol)
	{
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try
		{
			for (IOrder o : tradeReportList) {
				o.setStatus(EOrderStatus.PENDING_NEW);
				byte b[] = o.toCmmf();
				buf.put(b);
			}
			buf.flip();
			int size = buf.limit();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, ECommand.TRADE_REPORT, buf.array(), size);
//			JsonObject json = o.json();
//			byte b[] = json.toString().getBytes();
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void cancelTradeReport(IOrder o)
	{
		cancel(o, ECommand.TRADE_REPORT);
	}
	
	@Override
	public void modifyOrder(IOrder o)
	{
		try
		{
			o.setStatus(EOrderStatus.PENDING_AMEND);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, ECommand.ORDER_REQUEST, b);
CmmfParser.print(msg);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void cancelOrder(IOrder o)
	{
		cancel(o, ECommand.ORDER_REQUEST);
	}

	public void cancel(IOrder o, ECommand cmd) {
		try
		{
			o.setStatus(EOrderStatus.PENDING_CANCEL);
			byte b[] = o.toCmmf();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.TASK, cmd, b);
CmmfParser.print(msg);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void createQuote(IQuote quote)
	{
	}

	@Override
	public void modifyQuote(IQuote q)
	{
	}

	@Override
	public void cancelQuote(IQuote q)
	{
	}
	
	
	public static void main(String[] args) 
	{
		HkexOapiGateway gw = new HkexOapiGateway();
		gw.init();
		gw.start();
		
//		EOrderStatus status, IInstrument instr, EOrderType type, Long id, String entity,
//		Double price, Integer qty
		IInstrument instr = new Derivative("HK", "HHI9800O7", EInstrumentType.EP, "European Put", null, null, null, 
				9800d, "O7", null, false, 0.5);
		IOrder order = new Order(EOrderStatus.SENT, instr, EOrderType.LIMIT, null, 1l, "", 439d, 100, ESide.BUY);
		
		while(true)
		{
			try
			{
				gw.createOrder(order);
				Thread.sleep(10000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void changePassword(String password, String newPassword)
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.CHANGE_PASSWORD.getChar());
			buf.put(password.getBytes());
			buf.put(newPassword.getBytes());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, ECommand.ADMIN_REQUEST, b);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void logout()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.LOGOUT.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, ECommand.ADMIN_REQUEST, b);
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	public void start(String password)
	{
		login(password);
		isWaitAdminResp.set(true);
		while (isWaitAdminResp.get()) {};
		SOD();
		isWaitAdminResp.set(true);
		while (isWaitAdminResp.get()) {};
		subscribeMarketData();
	}
	
	@Override
	public void login(String password)
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.LOGIN.getChar());
			buf.put(password.getBytes());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, ECommand.ADMIN_REQUEST, b);
//			for (int i=0; i<msg.length; i++) {
//				System.out.print((int)msg[i] + ",");
//			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void SOD()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.SOD.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, ECommand.ADMIN_REQUEST, b);
//			for (int i=0; i<msg.length; i++) {
//				System.out.print((int)msg[i] + ",");
//			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	public void subscribeMarketData()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.SUBSCRIBE_MARKET_DATA.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, ECommand.ADMIN_REQUEST, b);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void unsubscribeMarketData()
	{
		try
		{
			ByteBuffer buf = ByteBuffer.allocate(CMMF_LOGIN_ADMIN_SIZE);
			buf.put((byte)EOGAdmin.UNSUBSCRIBE_MARKET_DATA.getChar());
			buf.flip();
			byte b[] = buf.array();
			byte[] msg = CmmfBuilder.buildMessage(EApp.OMS, EMessageType.ADMIN, ECommand.ADMIN_REQUEST, b);
			for (int i=0; i<msg.length; i++) {
				System.out.print((int)msg[i] + ",");
			}
			server.send(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
}
