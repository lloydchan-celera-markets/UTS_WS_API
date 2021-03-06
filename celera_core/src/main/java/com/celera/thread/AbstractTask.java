package com.celera.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTask implements Runnable
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractTask.class);

	private final ExecutorService exec = Executors.newFixedThreadPool(1);
	private Future future = null;

	protected void submit(Runnable task) 
	{
		future = exec.submit(task);
	}
	
	protected void shutdown()
	{
		try
		{
			logger.info("shutting down Server");
			exec.shutdown();
			exec.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e)
		{
			logger.error("tasks interrupted");
		} finally
		{
			if (!exec.isTerminated())
			{
				logger.error("cancel non-finished tasks");
			}
			exec.shutdownNow();
			logger.info("shutdown finished");
		}
	}
}
