package com.vectails.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.ResourceManager;
import com.vectails.session.UtsClient;

public class Application
{
	static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String args[]) 
	{
		BasicConfigurator.configure();
		logger.info("Read BasicConfigurator");
		
		ExecutorService exec = Executors.newFixedThreadPool(1);

		final UtsClient client = new UtsClient();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				client.stop();
			}
		}));

		exec.execute(client);
		
		for (;;)
		{
			try
			{
				Thread.sleep(10000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
//		System.exit(0);
	}
}
