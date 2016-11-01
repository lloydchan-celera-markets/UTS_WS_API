package com.celera.message.cmmf;

import java.util.UUID;

public abstract class CmmfApp implements ICmmfMessageListener
{
	String uniqueID = UUID.randomUUID().toString();

	protected EApp me;

	public CmmfApp(EApp me)
	{
		this.me = me;
	}

	public boolean isMine(char other)
	{
		return me == EApp.get(other);
	}

	public String id()
	{
	
		return uniqueID;
	}
	
	@Override
	public byte[] onMessage(byte[] data)
	{
		EMessageType type = EMessageType.get((char) data[ICmmfConst.HEADER_MESSAGE_TYPE_POS]);
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
