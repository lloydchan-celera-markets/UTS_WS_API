package com.vectails.sds;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IStaticDataService;
import com.vectails.message.processor.Uts2Dm;

public class UtsStaticDataService implements IStaticDataService
{
	final static Logger logger = LoggerFactory.getLogger(UtsStaticDataService.class);

	Map<String, IInstrument> instrMap = null;
	// unused - Quote has all values
	// static Map<String, String> paramString = new HashMap<String, String>();

	private static UtsStaticDataService _instance = null;

	protected UtsStaticDataService()
	{
		instrMap = new HashMap<String, IInstrument>();
	}

	public static UtsStaticDataService instance()
	{
		if (_instance == null)
		{
			synchronized (UtsStaticDataService.class)
			{
				if (_instance == null)
				{
					_instance = new UtsStaticDataService();
				}
			}
		}
		return _instance;
	}

	/**
	 * @param id
	 *            flexible to client generated key
	 * @param instr
	 */
	public void add1(String key, IInstrument instr)
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

	public IInstrument get1(String key)
	{
		return instrMap.get(key);
	}

//	static public void add(String type, String paramStr)
//	{
//		String old = paramString.get(type);
//		if (old != null)
//			System.out.println("=========not null=============");
//		paramString.put(type, paramStr);
//	}

	public int size()
	{
		return instrMap.size();
	}

	public void print()
	{
		for (Entry<String, IInstrument> e : instrMap.entrySet())
		{
			System.out.println(e.getValue());
		}
	}

	// enforce key to EInstrumentType
	public void add(EInstrumentType type, IInstrument instr)
	{
		add1(type.toString(), instr);
	}

	public IInstrument get(EInstrumentType type)
	{
		String key = type.toString();
		return get1(key);
	}

	public void onInstrumentUpdate(IInstrument instr)
	{
		String key = toKey(instr);
		IInstrument old = instrMap.get(key);
		if (old == null)
		{
			instrMap.put(key, instr);
			logger.info(instr.toString());
		}
		else
			old.setStatus(instr.getStatus());
	}

	public IInstrument get(String derivTypeCode)
	{
		String key = genKey(derivTypeCode);
		return instrMap.get(key);
	}

	public String toKey(IInstrument instr)
	{
		return instr.getType().toString();
	}

	public String genKey(String derivTypeCode)
	{
		EInstrumentType derivType = Uts2Dm.toInstrumentType(derivTypeCode);
		return derivType.toString();
	}
}
