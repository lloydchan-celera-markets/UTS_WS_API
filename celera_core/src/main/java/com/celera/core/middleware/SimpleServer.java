package com.celera.core.middleware;

import java.util.Random;

import org.zeromq.ZMQ;

public class SimpleServer extends AbstractServer
{
	private ZMQ.Context ctx;
	private final ZMQ.Socket socket;
	// private ZMQ.Socket socket = ctx.socket(ZMQ.PUB);

	public SimpleServer(String ip, String port, int socketType)
	{
		super(ip, port);
		this.ctx = ZMQ.context(1);
		this.socket = ctx.socket(socketType);
	}

	public void bind() throws Exception
	{
		socket.bind("tcp://" + ip + ": " + port);
	}

	@Override
	public void send(String msg)
	{
		// int id = 1;
		// socket.send(String.format("%05d %s", id, msg));
		socket.send(msg);
	}

	public static void main(String[] args) throws Exception
	{
		SimpleServer server = new SimpleServer("*", "5556", ZMQ.PUB);
		server.bind();
		Random random = new Random();
		while (true)
		{
			int id = random.nextInt(100000);
			int data = random.nextInt(500);
			server.send(String.format("%05d %d", id, data));
		}

		// try (ZMQ.Context ctx = ZMQ.context(1); ZMQ.Socket publisher =
		// ctx.socket(ZMQ.PUB))
		// {
		// publisher.bind("tcp://*:5556");
		//
		// Random random = new Random();
		// while (true)
		// {
		// int id = random.nextInt(100000);
		// int data = random.nextInt(500);
		// publisher.send(String.format("%05d %d", id, data));
		// }
		// }
	}
}