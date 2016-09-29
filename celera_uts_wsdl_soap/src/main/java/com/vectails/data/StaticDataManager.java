package com.vectails.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.IInstrument;
import com.vectails.session.UtsDirectAccessClient;

public class StaticDataManager
{
	final static Logger logger = LoggerFactory.getLogger(StaticDataManager.class);
	
	static Map<String, IInstrument> instrMap = new HashMap<String, IInstrument>();
	
	static public void add(IInstrument instr)
	{
		String key = instr.key();
		IInstrument old = instrMap.get(key);
		if (old == null)
		{
			instrMap.put(instr.key(), instr);
			
			logger.info(instr.toString());
		}
		else 
			old.setStatus(instr.getStatus());
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
