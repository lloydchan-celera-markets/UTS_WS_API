package com.celera.ipc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import com.celera.message.cmmf.ICmmfListener;
import com.celera.message.cmmf.ICmmfServer;
import com.celera.message.cmmf.AbstractCmmfService;
import com.celera.message.cmmf.ICmmfClient;

public class PipelineServer extends AbstractCmmfService implements ILifeCycle, ICmmfServer, ICmmfClient
{
	Logger logger = LoggerFactory.getLogger(PipelineServer.class);

	private final ZMQ.Context context = ZMQ.context(1);
	// socket to send task
	private ZMQ.Socket push = context.socket(ZMQ.PUSH);
//	// Socket to sink tasks
//	private ZMQ.Socket sink = context.socket(ZMQ.PUSH);

	private final String pushUrl;

	public PipelineServer(String push, ICmmfListener listener)
	{
		super(listener);
		
		this.pushUrl = push;
	}

	@Override
	public void init()
	{
//		bind();
	}

	@Override
	public void start()
	{
		super.submit(this);
	}

	@Override
	public void stop()
	{
		super.shutdown();
		
//		sink.close();
		push.close();
		context.term();
	}

	public void bind()
	{
		try
		{
			push.bind(pushUrl);
			
			logger.info("bind push {}", pushUrl);
		} catch (Exception e)
		{
			logger.error("connect error", e);
			System.exit(-1);
		}
	}
	
	@Override
	public void connect()
	{
		try
		{
			push.connect(pushUrl);
			
			logger.info("connect push {}", pushUrl);
		} catch (Exception e)
		{
			logger.error("connect error", e);
			System.exit(-1);
		}
	}

	@Override
	public void send(byte b[])
	{
		logger.info("send {}", new String(b));
		// Process tasks forever
//		int count = 0;
//		while (!Thread.currentThread().isInterrupted())
//		{
//			byte[] buf = push.recv(0);
//			byte[] result = listener.onMessage(buf);

//			if (result != null)
			{
				// Send results to sink
//				push.send(result, 0);
			}

			push.send(b, 0);
			
//			try
//			{
//				Thread.sleep(1);
//			} catch (InterruptedException e)
//			{
//				logger.debug("", e);
//			}
//		}
	}
	
	@Override
	public void run()
	{
	}
	
	public static void main(String[] args) throws Exception
	{
		ZMQ.Context context = ZMQ.context(1);

		// Socket to receive messages on
		ZMQ.Socket sender = context.socket(ZMQ.PUSH);
		sender.bind("tcp://*:5557");

		// Socket to send messages to
		ZMQ.Socket sink = context.socket(ZMQ.PUSH);
		sink.connect("tcp://localhost:5558");

		int count = 0;
		System.out.println("Press enter when the workers are ready");
		System.in.read();
		System.out.println("sending task to workers\n");
		
		sink.send("0", 0);
		
		// Process tasks forever
		while (count < 100)
		{
			sender.send(Integer.toString(count++), 0);
			System.out.println("send ");
			
			// Do the work
			Thread.sleep(10000);

			// Send results to sink
			sender.send("".getBytes(), 0);
			System.out.println("send sink");
		}
		sender.close();
		sink.close();
		context.term();
	}

}
