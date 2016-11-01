package com.celera.message.cmmf;

import java.nio.ByteBuffer;

public interface ICmmfMessageListener
{
	public byte[] onMessage(byte[] buf);
	public byte[] onQuery(byte[] data);
	public void onAdmin(byte[] data);
	public void onResponse(byte[] data);
	public void onTask(byte[] data);
}
