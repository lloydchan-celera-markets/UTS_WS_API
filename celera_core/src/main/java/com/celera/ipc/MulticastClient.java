package com.celera.ipc;
/*
  Copyright (c) 2007-2010 iMatix Corporation

  This file is part of 0MQ.

  0MQ is free software; you can redistribute it and/or modify it under
  the terms of the Lesser GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  0MQ is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  Lesser GNU General Public License for more details.

  You should have received a copy of the Lesser GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ICmmfListener;

public class MulticastClient implements IMulticastClient
{
	Logger logger = LoggerFactory.getLogger(MulticastClient.class);

	ZMQ.Context ctx; // = ZMQ.context (1);
	ZMQ.Socket s; // = ctx.socket (ZMQ.REP);
	private static final String TCP = "tcp";
	private int ioThread;
	private String ip;
	private int port;
	private ICmmfListener listener;
	private List<String> subList = new ArrayList<String>();

	private AtomicBoolean isSub = new AtomicBoolean(false);
	private AtomicBoolean isStart = new AtomicBoolean(false);

	public MulticastClient(int ioThread, String ip, int port, ICmmfListener listener)
	{
		this.ioThread = ioThread;
		this.ip = ip;
		this.port = port;
		this.listener = listener;
	}

	private void connect()
	{
		ctx = ZMQ.context(ioThread);
		s = ctx.socket(ZMQ.SUB);
		s.connect(TCP + "://" + ip + ":" + port);

		logger.debug("connect SUB {},{}", ioThread, TCP + "://" + ip + ":" + port);
	}

	@Override
	public void subscribe(EApp app)
	{
		isSub.compareAndSet(false, true);

		if (ctx == null || s == null)
			connect();

		String sub = app.toString();
		subList.add(sub);
		s.subscribe(sub.getBytes());

		isSub.compareAndSet(true, false);

		logger.info("subscribe multicast {}", app.toString());
	}

	@Override
	public void unsubscribe(EApp app)
	{
		isSub.compareAndSet(false, true);

		String sub = app.toString();
		subList.remove(sub);
		s.unsubscribe(sub.getBytes());
		if (subList.size() > 0)	// all unsubscribe
		{
			isSub.compareAndSet(true, false);
			s.close();
			ctx.term();
			s = null;
			ctx = null;
		}

		logger.info("unsubscribe multicast {}", app.toString());
	}

	@Override
	public void run()
	{
		logger.info("start multicast client");

		for (;;)
		{
			if (!isSub.get())
			{
				// Wait for next request from client
				byte[] address = s.recv(0);
				byte[] contents = s.recv(0);
				try
				{
					listener.onMessage(contents);
				} catch (Exception e)
				{
					logger.error("Error process message: [{}] {}", new String(address), new String(contents), e);
				}

			} else
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					logger.error("", e);
				}
			}
		}
	}

	public static void main(String[] args)
	{
		if (args.length != 3)
		{
			System.out.println("usage: local_lat <bind-to> " + "<message-size> <roundtrip-count>");
			return;
		}

		String bindTo = args[0];
		int messageSize = Integer.parseInt(args[1]);
		int roundtripCount = Integer.parseInt(args[2]);

		ZMQ.Context ctx = ZMQ.context(1);
		ZMQ.Socket s = ctx.socket(ZMQ.REP);

		// Add your socket options here.
		// For example ZMQ_RATE, ZMQ_RECOVERY_IVL and ZMQ_MCAST_LOOP for PGM.

		s.bind("tcp://127.0.0.1:5555");

		for (int i = 0; i != roundtripCount; i++)
		{
			byte[] data = s.recv(0);
			assert (data.length == messageSize);
			s.send(data, 0);
		}

		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void setTcpListener(ICmmfListener listener)
	{
		this.listener = listener;
	}
}
