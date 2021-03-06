package com.celera.zeromq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * Hello World server Connects REP socket to tcp://*:5560 Expects "Hello" from
 * client, replies with "World"
 *	request , response server
 *
 * Christophe Huntzinger <chuntzin_at_wanadoo.fr>
 */
public class RrClient
{
	ZMQ.Context context = null;
	// Socket to talk to clients
	ZMQ.Socket socket = null;
	
	public RrClient(int ioThread) 
	{
		context = ZMQ.context(ioThread);
		socket = context.socket(ZMQ.REP);
	}
	
	public void bind(){
//		s.bind(addr);
	}
	
	public void connect()
	{
		// Socket to talk to clients
		socket.connect("tcp://localhost:9998");
	}
	
	public void run()
	{
		System.out.println("launch and connect server.");
		try
		{

			// while (!Thread.currentThread().isInterrupted()) {
			while (true)
			{
				// Wait for next request from client
				byte[] request = socket.recv(0);
				String string = new String(request);
				System.out.println("Received request: [" + string + "].");

				// Do some 'work'
				// try {
				socket.send("World".getBytes(), 0);
				Thread.sleep(1);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				// Send reply back to client
				// responder.send("World".getBytes(), 0);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		// We never get here but clean up anyhow
		socket.close();
		context.term();
	}
	
	public static void main(String[] args)
	{
		RrClient server = new RrClient(1);
		server.connect();
		server.run();
		server.close();
//		System.out.println("launch and connect server.");
//		try
//		{
//
//			// while (!Thread.currentThread().isInterrupted()) {
//			while (true)
//			{
//				// Wait for next request from client
//				byte[] request = responder.recv(0);
//				String string = new String(request);
//				System.out.println("Received request: [" + string + "].");
//
//				// Do some 'work'
//				// try {
//				responder.send("World".getBytes(), 0);
//				Thread.sleep(1);
//				// } catch (InterruptedException e) {
//				// e.printStackTrace();
//				// }
//
//				// Send reply back to client
//				// responder.send("World".getBytes(), 0);
//			}
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//
//
//		context.term();
	}
}
