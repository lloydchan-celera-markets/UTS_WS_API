package com.celera.core.service.staticdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.core.dm.IInstrument;

public class StaticDataService implements IStaticDataService
{
	private static final Logger logger = LoggerFactory.getLogger(StaticDataService.class);
	
	static private IStaticDataService INSTANCE = null;
	private Map<String, IInstrument> map = new ConcurrentHashMap<String, IInstrument>();
	
	private static final String TRADABLE_SYMBOL;
	private static final Set<String> tradableSymbol = new HashSet<String>();

	private static final Map<String, String> c_clearingMember = new ConcurrentHashMap<String, String>();
	
	static
	{
		TRADABLE_SYMBOL = ResourceManager.getProperties(IResourceProperties.PROP_WEB_TRADER_TRADABLE_SYMBOL);
		for(String symbol : TRADABLE_SYMBOL.split(","))
			tradableSymbol.add(symbol);
		
		c_clearingMember.put("HKCEL", "CCEL");
		c_clearingMember.put("HKTOM", "CTOM");
	}
	
	static synchronized public IStaticDataService instance()
	{
		if (INSTANCE == null)
			INSTANCE = new StaticDataService();
		return INSTANCE;
	}
	
	public StaticDataService() {
	}
	
	@Override
	public IInstrument getInstr(String name)
	{
		return map.get(name);
	}

	@Override
	public List<IInstrument> getAllInstruments()
	{
		List<IInstrument> l = new ArrayList<IInstrument>();
		for (Map.Entry<String, IInstrument> e : map.entrySet()) {
			l.add(e.getValue());
		}
		return l;
	}
	
	@Override
	public void onInstrumentUpdate(IInstrument instr)
	{
		logger.info("update instr {}", instr);
		
		// TODO ? 1) update or replace 2) only HHI, HSI
		String symbol0_3 = instr.getSymbol().substring(0, 3);
		if (tradableSymbol.contains(symbol0_3))
				map.put(instr.getSymbol(), instr);
	}
	
	@Override
	public String getClearingMember(String code)
	{
		String member = c_clearingMember.get(code);
		return member;
	}
}
