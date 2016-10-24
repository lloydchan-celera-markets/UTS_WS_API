package com.vectails.main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uts.tools.Uts2Dm;
import com.vectails.session.UtsClient;

public class Application
{
	static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String args[]) 
	{
		BasicConfigurator.configure();
		logger.info("Read BasicConfigurator");

		System.out.println("start : " +  Uts2Dm.toLocalDateTime(LocalDateTime.now()));

		ExecutorService exec = Executors.newFixedThreadPool(1);

		final UtsClient client = new UtsClient();
		
//		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//			public void run() {
//				client.stop();
//			}
//		}));

		exec.execute(client);
		
		
		System.out.println("Press \"ENTER\" to continue...");
		try
		{
			int read = System.in.read(new byte[2]);
			client.stop();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("end : " +  Uts2Dm.toLocalDateTime(LocalDateTime.now()));
//		for (;;)
//		{
//			try
//			{
//				Thread.sleep(10000);
//			}
//			catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//		}
		
//		System.exit(0);
	}
}
