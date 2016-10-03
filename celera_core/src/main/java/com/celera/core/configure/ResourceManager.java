package com.celera.core.configure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceManager
{
	private static final String file;
	private static ResourceManager _instance;
	static
	{
		file = System.getProperty("config.file", null);
	}

	private Properties prop = null;

	protected ResourceManager()
	{
		prop = new Properties();
		InputStream in = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(ResourceManager.file);
		try
		{
			in = new FileInputStream(ResourceManager.file);
			prop.load(in);	
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try
			{
				in.close();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static ResourceManager instance()
	{
		if (_instance == null)
		{
			synchronized (ResourceManager.class)
			{
				if (_instance == null)
				{
					_instance = new ResourceManager();
				}
			}
		}
		return _instance;
	}
	
	public static String getProperties(String key)
	{
		return instance().prop.getProperty(key);
	}

	public static String getProperties(String key, String def)
	{
		return instance().prop.getProperty(key, def);
	}
}
