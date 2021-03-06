package com.celera.ipc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

public class Miniserver implements Runnable
{
	private static final Logger logger = LoggerFactory.getLogger(Miniserver.class);
	
	ZMQ.Context ctx = ZMQ.context(1);
	ZMQ.Socket s = ctx.socket(ZMQ.REP);
	
	public Miniserver(){
		s.bind("tcp://127.0.0.1:6555");
	};

//	@Override
//	public void run()
//	{
//		for (int i = 0; i != 1000; i++)
//		{
//			byte[] data = s.recv(0);
//			assert (data.length == 10);
//			s.send(data, 0);
//		}
//
//		try
//		{
//			Thread.sleep(1000);
//		} catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public void run()
	{
		System.out.println("start RrServer");
		
//		while (!Thread.currentThread().isInterrupted())
		while (true)
		{
			// Wait for next request from client
			byte[] request = s.recv(0);
			String string = new String(request);
			 System.out.println("Received request: ["+string+"].");
//			byte[] response = null;
//			if (listener != null)
//			{
//				try
//				{
//					response = listener.onMessage(request);
//				} catch (Exception e)
//				{
//					logger.error("Error process message: {}", new String(request), e);
//				}
//			}
//			s.send(response, 0);
			// Send reply back to client
			s.send(string.getBytes(), 0);
			try
			{
				System.out.println("server sleep(1)");
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}