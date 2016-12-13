package com.celera.gateway;

import java.io.IOException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EOrderType;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;
import com.celera.core.dm.Instrument;
import com.celera.core.dm.Order;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineServer;
import com.celera.ipc.PipelineSinkCollector;
import com.celera.ipc.URL;
import com.celera.message.cmmf.ICmmfListener;

public class HkexOapiGateway implements ILifeCycle, ICmmfListener, IOrderGateway
{
	Logger logger = LoggerFactory.getLogger(HkexOapiGateway.class);
	
	public static final URL PUSH_URL;// = "tcp://10.100.1.119:20010";
	public static final URL SINK_URL;// = "tcp://10.100.1.119:20011";
	
	private final PipelineSinkCollector sink;
	private final PipelineServer server;
	
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
	}

	@Override
	public void onTask(byte[] data)
	{
	}

	@Override
	public void onSink(byte[] data)
	{
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
			byte b[] = o.toMessage();
			for (int i=0; i<b.length; i++) {
				System.out.println((int)b[i]);
			}
			server.send(b);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void modifyOrder(IOrder o)
	{
	}

	@Override
	public void cancelOrder(IOrder o)
	{
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
		IInstrument instr = new Instrument("HK", "HHI9800O7", EInstrumentType.EP, "European Put", "", "", "");
		IOrder order = new Order(EOrderStatus.SENT, instr, EOrderType.LIMIT, 1l, "", 439d, 100);
		
		while(true)
		{
			try
			{
				gw.createOrder(order);
				Thread.sleep(30000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
