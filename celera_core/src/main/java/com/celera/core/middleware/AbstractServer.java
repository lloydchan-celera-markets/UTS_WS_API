package com.celera.core.middleware;

import org.zeromq.ZMQ;

public abstract class AbstractServer
{
	protected String port;
	protected String ip;

	public AbstractServer(String ip, String port)
	{
		this.port = port;
		this.ip = ip;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public abstract void bind() throws Exception;
	public abstract void send(String msg);

//	public static void main(String[] args)
//	{
//		 try (ZMQ.Context ctx = ZMQ.context(1); ZMQ.Socket publisher =
//		 ctx.socket(ZMQ.PUB))
//		 {
//		 publisher.bind("tcp://*:5556");
//		
//		 Random random = new Random();
//		 while (true)
//		 {
//		 int id = random.nextInt(100000);
//		 int data = random.nextInt(500);
//		 publisher.send(String.format("%05d %d", id, data));
//		 }
//		 }
//	}
}