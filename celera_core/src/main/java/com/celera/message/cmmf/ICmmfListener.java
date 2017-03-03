package com.celera.message.cmmf;

import java.nio.ByteBuffer;

import com.celera.core.dm.EAdminAction;

public interface ICmmfListener 
{
	public byte[] onQuery(byte[] data);
	public void onAdmin(byte[] data);
	public void onResponse(byte[] data);
	public void onTask(byte[] data);
	public void onSink(byte[] data);	// task sink result
	
	default public byte[] onMessage(byte[] data)
	{
		EMessageType type = EMessageType.get((char) data[ICmmfConst.HEADER_MESSAGE_TYPE_POS]);	// messageType
//		EMessageType type = EMessageType.get((char) data[1]);	// messageType
		byte[] b = null;
		
		switch (type)
		{
		case ADMIN:
			onAdmin(data);
			break;
		case QUERY:
			b = onQuery(data);
			break;
		case TASK:
			onTask(data);
			break;
		}
		return b;
	}

}
