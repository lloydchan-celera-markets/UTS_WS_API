package com.celera.ipc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import com.celera.message.cmmf.ICmmfListener;
import com.celera.message.cmmf.ICmmfServer;
import com.celera.message.cmmf.AbstractCmmfService;

public class PipelineSinkCollector extends AbstractCmmfService implements ILifeCycle, ICmmfServer
{
	Logger logger = LoggerFactory.getLogger(PipelineSinkCollector.class);

	private final ZMQ.Context context = ZMQ.context(1);
	// socket to send task
	private ZMQ.Socket sink = context.socket(ZMQ.PULL);
	private final String sinkUrl;
	
	public PipelineSinkCollector(String sink, ICmmfListener listener)
	{
		super(listener);
		this.sinkUrl = sink;
	}

	@Override
	public void init()
	{
		bind();
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
		
		sink.close();
		context.term();
	}

	public void bind()
	{
		try
		{
			sink.bind(sinkUrl);
			logger.info("bind sink {}", sinkUrl);
		} catch (Exception e)
		{
			logger.error("connect error", e);
			System.exit(-1);
		}
	}

	@Override
	public void send(byte b[])
	{
	}
	
	@Override
	public void run()
	{
		for (;;)
		{
			byte[] b = sink.recv(0);
//			logger.debug("sink receive result: " + new String(b));
			listener.onSink(b);
		}
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
