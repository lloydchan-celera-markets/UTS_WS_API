package com.celera.ipc;

import java.nio.ByteBuffer;

public interface ITcpServerListener
{
	public byte[] onMessage(byte[] buf);
}
