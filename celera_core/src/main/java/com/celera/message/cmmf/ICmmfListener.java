package com.celera.message.cmmf;

public interface ICmmfListener 
{
	public byte[] onMessage(byte[] buf);
	public byte[] onQuery(byte[] data);
	public void onAdmin(byte[] data);
	public void onResponse(byte[] data);
	public void onTask(byte[] data);
	public void onSink(byte[] data);	// task sink result
}
