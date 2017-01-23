package com.celera.gateway;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.dm.EStatus;
import com.celera.ipc.ILifeCycle;
import com.celera.message.cmmf.ICmmfListener;
import com.celera.message.cmmf.ICmmfProcessor;

public class OrderGatewayManager implements IOrderGatewayService
{
	static private List<IOrderGateway> gwList = new ArrayList<IOrderGateway>();
	
	static private OrderGatewayManager INSTANCE = null;

	public OrderGatewayManager() {
		HkexOapiGateway gw = new HkexOapiGateway();
		gwList.add(gw);
	}
	
	static synchronized public OrderGatewayManager instance()
	{
		if (INSTANCE == null) {
			INSTANCE = new OrderGatewayManager();
		}
		return INSTANCE;
	}

	static synchronized public void init() {
		for (IOrderGateway gw : gwList) {
			if (gw instanceof ILifeCycle) {
				((ILifeCycle)gw).init();
			}
		}
	}
	
	static synchronized public void start() {
		for (IOrderGateway gw : gwList) {
			if (gw instanceof ILifeCycle) {
				((ILifeCycle)gw).start();
			}
		}
	}
	
	static synchronized public void testSOD() {
		for (IOrderGateway gw : gwList) {
			if (gw instanceof IOrderGateway) {
				((IOrderGateway)gw).startTestSOD("geniumtesting");
			}
		}
	}
	
	@Override
	public IOrderGateway getOrderGateway(String symbol)
	{
		for (IOrderGateway l : gwList) {
			if (l.isTradedSymbol(symbol))
				return l;
		}
		return null;
	}
	
	static synchronized public void test(String lSymbol) {
		String[] tokens = lSymbol.split(",");
		for (IOrderGateway gw : gwList) {
			if (gw instanceof ICmmfProcessor) {
				for (String symbol : tokens) {
					((ICmmfProcessor)gw).onInstrumentUpdate(symbol, EStatus.ACTIVE);
				}
			}
		}
	}
	
}
