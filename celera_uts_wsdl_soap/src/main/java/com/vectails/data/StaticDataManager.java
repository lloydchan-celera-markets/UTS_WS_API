package com.vectails.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.IInstrument;

public class StaticDataManager
{
	final static Logger logger = LoggerFactory.getLogger(StaticDataManager.class);
	
	static Map<String, IInstrument> instrMap = new HashMap<String, IInstrument>();
	static Map<String, String> paramString = new HashMap<String, String>();
	
	/**
	 * @param id  flexible to client generated key
	 * @param instr
	 */
	static public void add(String key, IInstrument instr)
	{
		IInstrument old = instrMap.get(key);
		if (old == null)
		{
			instrMap.put(key, instr);
			
			logger.info(instr.toString());
		}
		else 
			old.setStatus(instr.getStatus());
	}
	
	static public IInstrument get(String key)
	{
		return instrMap.get(key);
	}

	static public void add(String type, String paramStr)
	{
		String old = paramString.get(type);
		if (old != null)
			System.out.println("=========not null=============");
		paramString.put(type, paramStr);
	}
	
	static public int size() {
		return instrMap.size();
	}

	public static void print()
	{
		for (Entry<String, IInstrument> e: instrMap.entrySet()) {
			System.out.println(e.getValue());
		}
	}
	
	
	
}
