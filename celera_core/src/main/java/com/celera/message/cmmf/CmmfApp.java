package com.celera.message.cmmf;

import java.util.UUID;

public abstract class CmmfApp
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
	
	public abstract byte[] onQuery(byte[] data);
	public abstract void onAdmin(byte[] data);
	public abstract void onResponse(byte[] data);
}
