package com.vectails.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vectails.session.UtsClient;

public class Application
{
	public static void main(String args[]) 
	{
		ExecutorService exec = Executors.newFixedThreadPool(1);

		UtsClient client = new UtsClient();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
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
