//package com.celera.exchange;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.celera.backoffice.BOServiceManager;
//import com.celera.core.configure.IResourceProperties;
//import com.celera.core.configure.ResourceManager;
//import com.celera.core.service.staticdata.StaticDataService;
//import com.celera.ipc.ILifeCycle;
//import com.celera.ipc.URL;
//import com.celera.message.cmmf.CmmfApp;
//import com.celera.message.cmmf.EApp;
//
//import come.celera.core.oms.OMS;
//
//public class ExchangeConnector  extends CmmfApp implements ILifeCycle
//{
//	private static final Logger logger = LoggerFactory.getLogger(ExchangeConnector.class);
//	
//	private static final String CHANNEL_PULL_PROTOCOL;
//	private static final String CHANNEL_PULL_IP;
//	private static final Integer CHANNEL_PULL_PORT;
//	private static final URL PULL_URL;
//	
//	private static final String CHANNEL_SINK_PROTOCOL;
//	private static final String CHANNEL_SINK_IP;
//	private static final Integer CHANNEL_SINK_PORT;
//	private static final URL SINK_URL;
//	
//	static
//	{
//		CHANNEL_PULL_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_EXCHANGE_CONNECTOR_CHL_PULL_PORT);
//		CHANNEL_PULL_IP = ResourceManager.getProperties(IResourceProperties.PROP_EXCHANGE_CONNECTOR_CHL_PULL_IP);
//		CHANNEL_PULL_PORT = Integer
//				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_EXCHANGE_CONNECTOR_CHL_PULL_PORT));
//		PULL_URL = new URL(CHANNEL_PULL_PROTOCOL, CHANNEL_PULL_IP, CHANNEL_PULL_PORT);
//
//		CHANNEL_SINK_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_EXCHANGE_CONNECTOR_CHL_SINK_PROT);
//		CHANNEL_SINK_IP = ResourceManager.getProperties(IResourceProperties.PROP_EXCHANGE_CONNECTOR_CHL_SINK_IP);
//		CHANNEL_SINK_PORT = Integer
//				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_EXCHANGE_CONNECTOR_CHL_SINK_PORT));
//		SINK_URL = new URL(CHANNEL_SINK_PROTOCOL, CHANNEL_SINK_IP, CHANNEL_SINK_PORT);
//	}
//	
//	private StaticDataService sds; 
//	private OMS oms;
//	
//	public ExchangeConnector() {
//		super(EApp.ExchangeConnector);
//	}
//
//	public static void main(String[] args)
//	{
//
//	}
//
//	@Override
//	public byte[] onQuery(byte[] data)
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onAdmin(byte[] data)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onResponse(byte[] data)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onTask(byte[] data)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void init()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void start()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void stop()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//}
