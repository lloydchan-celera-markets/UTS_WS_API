//package com.celera.exchange;
//
//import java.util.UUID;
//
//import com.celera.ipc.ILifeCycle;
//import com.celera.ipc.PipelineClient;
//import com.celera.message.cmmf.EApp;
//import com.celera.message.cmmf.EMessageType;
//import com.celera.message.cmmf.ICmmfConst;
//import com.celera.message.cmmf.ICmmfMessageListener;
//
//public abstract class CmmfApp implements ICmmfMessageListener
//{
//	String uniqueID = UUID.randomUUID().toString();
//
//	protected EApp me;
//	protected ILifeCycle taskChannel;
//	
//	public CmmfApp(EApp me)
//	{
//		this.me = me;
//	}
//
//	public boolean isMine(char other)
//	{
//		return me == EApp.get(other);
//	}
//
//	public String id()
//	{
//	
//		return uniqueID;
//	}
//	
//	@Override
//	public byte[] onMessage(byte[] data)
//	{
//		EMessageType type = EMessageType.get((char) data[ICmmfConst.HEADER_MESSAGE_TYPE_POS]);
//		byte[] b = null;
//		
//		switch (type)
//		{
//		case ADMIN:
//			onAdmin(data);
//			break;
//		case QUERY:
//			b = onQuery(data);
//			break;
//		case TASK:
//			onTask(data);
//			break;
//		}
//		return b;
//	}
//	
//	public void init()
//	{
//		taskChannel = new PipelineClient(PULL_URL, SINK_URL, this);
//		taskChannel.init();
//	}
//
//	public void start()
//	{
//		taskChannel.start();
//	}
//
//	public void stop()
//	{
//		taskChannel.stop();		
//	}
//}
