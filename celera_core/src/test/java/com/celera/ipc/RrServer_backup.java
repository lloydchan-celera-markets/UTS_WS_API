//package com.celera.ipc;
///*
//  Copyright (c) 2007-2010 iMatix Corporation
//
//  This file is part of 0MQ.
//
//  0MQ is free software; you can redistribute it and/or modify it under
//  the terms of the Lesser GNU General Public License as published by
//  the Free Software Foundation; either version 3 of the License, or
//  (at your option) any later version.
//
//  0MQ is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Lesser GNU General Public License for more details.
//
//  You should have received a copy of the Lesser GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//*/
//
//import java.nio.ByteBuffer;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.zeromq.ZMQ;
//
//import com.celera.message.cmmf.ICmmfMessageListener;
//import com.celera.message.cmmf.AbstractCmmfService;
//import com.celera.thread.AbstractTask;
//
//public class RrServer_backup extends AbstractTask implements AbstractCmmfService, ILifeCycle
//{
//	private static final Logger logger = LoggerFactory.getLogger(RrServer_backup.class);
//	
//	private ZMQ.Context ctx = null; // = ZMQ.context (1);
//	private ZMQ.Socket s = null; // = ctx.socket (ZMQ.REP);
//	private static final String TCP = "tcp";
//	private final int ioThread;
//	private final String ip;
//	private final int port;
//	private final ICmmfMessageListener listener;
//
////	private final ExecutorService exec = Executors.newFixedThreadPool(1);
////	private Future future;
//	
//	public RrServer_backup(int ioThread, String ip, int port, ICmmfMessageListener listener)
//	{
//		this.ioThread = ioThread;
//		this.ip = ip;
//		this.port = port;
//		this.listener = listener;
//	}
//
//	private void bind()
//	{
//		s.bind(TCP + "://" + ip + ":" + port);
//		
//		logger.debug("bind REP {},{}", ioThread, TCP + "://" + ip + ":" + port);
//	}
//
//	@Override
//	public void start()
//	{
//		bind();
//		
//		super.submit(this);
//		
//	}
//
//	@Override
//	public void stop()
//	{
//		super.shutdown();
//		
//		s.close();
//		ctx.term();
//	}
//	
//	public void setMessageListener(ICmmfMessageListener listener)
//	{
////		this.listener = listener;		
//	}
//
//	@Override
//	public void run()
//	{
//		logger.info("start RrServer");
//		
////		while (!Thread.currentThread().isInterrupted())
//		while (true)
//		{
//			// Wait for next request from client
//			byte[] request = s.recv(0);
//			String string = new String(request);
//			 System.out.println("Received request: ["+string+"].");
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
////			s.send(response, 0);
//			// Send reply back to client
//			s.send("World".getBytes(), 0);
//			try
//			{
//				logger.info("server sleep(1)");
//				Thread.sleep(1);
//			} catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void init()
//	{
//		if (ctx == null || s == null)
//		{
//			ctx = ZMQ.context(ioThread);
//			s = ctx.socket(ZMQ.PAIR);
//		}
//	}
//	
//	public static void main(String[] args)
//	{
//		Miniserver srv = new Miniserver();
//		ExecutorService exec = Executors.newFixedThreadPool(1);
//		exec.submit(srv);
//	}
//	
//	public static void main1(String[] args)
//	{
//		if (args.length != 3)
//		{
//			System.out.println("usage: local_lat <bind-to> " + "<message-size> <roundtrip-count>");
//			return;
//		}
//
//		String bindTo = args[0];
//		int messageSize = Integer.parseInt(args[1]);
//		int roundtripCount = Integer.parseInt(args[2]);
//		
//		ZMQ.Context ctx = ZMQ.context(1);
//		ZMQ.Socket s = ctx.socket(ZMQ.REP);
//
//		// Add your socket options here.
//		// For example ZMQ_RATE, ZMQ_RECOVERY_IVL and ZMQ_MCAST_LOOP for PGM.
//
//		s.bind("tcp://127.0.0.1:6555");
//
//		for (int i = 0; i != roundtripCount; i++)
//		{
//			byte[] data = s.recv(0);
//			assert (data.length == messageSize);
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
//
//	}
//
//}
