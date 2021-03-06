package com.celera.ipc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import com.celera.message.cmmf.ICmmfClient;
import com.celera.message.cmmf.ICmmfServer;
import com.celera.message.cmmf.ICmmfListener;
import com.celera.message.cmmf.AbstractCmmfService;

public class PipelineClient extends AbstractCmmfService implements ILifeCycle, ICmmfClient/*, ICmmfServer*/
{
	Logger logger = LoggerFactory.getLogger(PipelineClient.class);

	private final ZMQ.Context context = ZMQ.context(1);
	// socket to pull task
	private ZMQ.Socket pull = context.socket(ZMQ.PULL);
	// Socket to sink tasks
	private ZMQ.Socket sink = context.socket(ZMQ.PUSH);

	private final URL pullUrl;
	private final URL sinkUrl;

	public PipelineClient(URL pull, URL sink, ICmmfListener listener)
	{
		super(listener);
		
		this.pullUrl = pull;
		this.sinkUrl = sink;
	}

	@Override
	public void init()
	{
		connect();
	}

	@Override
	public void start()
	{
//		this.connect();
		super.submit(this);
	}

	@Override
	public void stop()
	{
		super.shutdown();
		
		sink.close();
		pull.close();
		context.term();
	}

	@Override
	public void connect()
	{
		try
		{
			pull.connect(pullUrl.toString());
			logger.info("Bind pull {}", pullUrl.toString());
			sink.connect(sinkUrl.toString());
			logger.info("Bind sink {}", sinkUrl.toString());
			
		} catch (Exception e)
		{
			logger.error("connect error", e);
			System.exit(-1);
		}
	}

	public void sink(byte[] buf) {
		logger.info("sink {}", new String(buf));
		sink.send(buf, 0);
	}
	
	@Override
	public void run()
	{
		logger.info("start");
		// Process tasks forever
		while (!Thread.currentThread().isInterrupted())
		{
			byte[] buf = pull.recv(0);
			byte[] result = listener.onMessage(buf);
//			long msec = Long.parseLong(string);
//			// Simple progress indicator for the viewer
//			System.out.flush();
//			System.out.print(string + '.');
//
//			// Do the work
//			Thread.sleep(msec);

			if (result != null)
			{
				// Send results to sink
				sink.send(result, 0);
			}
			
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				logger.debug("", e);
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		ZMQ.Context context = ZMQ.context(1);

		// Socket to receive messages on
		ZMQ.Socket receiver = context.socket(ZMQ.PULL);
		receiver.connect("tcp://localhost:5557");
//		receiver.connect("tcp://localhost:5557");

		// Socket to send messages to
		ZMQ.Socket sender = context.socket(ZMQ.PUSH);
//		sender.connect("tcp://localhost:5558");
		sender.connect("tcp://localhost:5558");

		// Process tasks forever
		while (!Thread.currentThread().isInterrupted())
		{
			String string = new String(receiver.recv(0)).trim();
			long msec = Long.parseLong(string);
			// Simple progress indicator for the viewer
			System.out.flush();
			System.out.print(string + '.');

			// Do the work
			Thread.sleep(msec);

			// Send results to sink
			sender.send("".getBytes(), 0);
		}
		sender.close();
		receiver.close();
		context.term();
	}

//	@Override
//	public void bind()
//	{
//		try
//		{
//			pull.bind(pullUrl.toString());
//			logger.info("Bind pull {}", pullUrl.toString());
//			
//			sink.bind(sinkUrl.toString());
//			logger.info("Bind sink {}", sinkUrl.toString());
//			
//		} catch (Exception e)
//		{
//			logger.error("connect error", e);
//			System.exit(-1);
//		}
//	}
}
