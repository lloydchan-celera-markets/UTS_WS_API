package com.celera.backoffice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.adapter.DatabaseAdapter;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.ipc.ILifeCycle;
import com.celera.ipc.PipelineClient;
import com.celera.ipc.URL;
import com.celera.message.cmmf.CmmfApp;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.ICmmfConst;
import com.celera.mongo.entity.TradeConfo;

public class BOServiceManager extends CmmfApp implements ILifeCycle
{
	private static final Logger logger = LoggerFactory.getLogger(BOServiceManager.class);
	
	private static final String CHANNEL_PULL_PROTOCOL;
	private static final String CHANNEL_PULL_IP;
	private static final Integer CHANNEL_PULL_PORT;
	private static final URL PULL_URL;
	
	private static final String CHANNEL_SINK_PROTOCOL;
	private static final String CHANNEL_SINK_IP;
	private static final Integer CHANNEL_SINK_PORT;
	private static final URL SINK_URL;
	
	private final static SimpleDateFormat cmmfSdf = new SimpleDateFormat(ICmmfConst.DATE_FMT);
	
	private ILifeCycle taskChannel;

	static
	{
		CHANNEL_PULL_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_PULL_PROT);
		CHANNEL_PULL_IP = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_PULL_IP);
		CHANNEL_PULL_PORT = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_PULL_PORT));
		PULL_URL = new URL(CHANNEL_PULL_PROTOCOL, CHANNEL_PULL_IP, CHANNEL_PULL_PORT);

		CHANNEL_SINK_PROTOCOL = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_SINK_PROT);
		CHANNEL_SINK_IP = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_SINK_IP);
		CHANNEL_SINK_PORT = Integer
				.valueOf(ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_CHL_SINK_PORT));
		SINK_URL = new URL(CHANNEL_SINK_PROTOCOL, CHANNEL_SINK_IP, CHANNEL_SINK_PORT);
	}
	
	public BOServiceManager()
	{
		super(EApp.BOServiceManager);
	}
	
	public byte[] onQuery(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void onAdmin(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

	public void onResponse(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

	public void onTask(byte[] data)
	{
		ECommand cmd = ECommand.get((char) data[ICmmfConst.HEADER_COMMAND_POS]);
		switch (cmd)
		{
		case EMAIL_INVOICE:
//			msg = DatabaseAdapter.getHistTradeConfo();
			break;
		case CREATE_INVOICE:
			String start = new String(data, ICmmfConst.HEADER_SIZE, ICmmfConst.DATE_LENGTH);
			String end = new String(data, ICmmfConst.HEADER_SIZE + ICmmfConst.DATE_LENGTH,
					ICmmfConst.DATE_LENGTH);
			try
			{
				Date dStart = cmmfSdf.parse(start);
				Date dEnd = cmmfSdf.parse(end);
				List<TradeConfo> l = DatabaseAdapter.getHistTradeConfo(dStart, dEnd);
				for (TradeConfo e : l) 
				{
				}
			} catch (ParseException e)
			{
				logger.error("", e);
			}
			break;
		}
		logger.debug(data.toString());
	}
	
	public void init()
	{
		taskChannel = new PipelineClient(PULL_URL, SINK_URL, this);
		taskChannel.init();
	}

	public void start()
	{
		taskChannel.start();
	}

	public void stop()
	{
		taskChannel.stop();		
	}
	
	public static void main(String[] args)
	{
		int interval = 10000;
		BOServiceManager sm = new BOServiceManager();
		sm.init();
		sm.start();

		for (;;)
		{
			try
			{
				logger.debug("sleep {}", interval);
				Thread.sleep(interval);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		// dba.stop();
	}
	
}