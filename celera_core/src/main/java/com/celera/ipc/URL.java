package com.celera.ipc;

public class URL
{
	private final String protocol;
	private final String ip;
	private final Integer port;
	
	public URL(String protocol, String ip, Integer port)
	{
		this.protocol = protocol;
		this.ip = ip;
		this.port = port;
	}

	@Override
	public String toString()
	{
		// tcp://localhost:5557
		return protocol + "://" + ip + ":" + port;
	}
}
