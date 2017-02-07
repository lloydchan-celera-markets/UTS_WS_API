package com.celera.core.oms;

import java.util.List;
import java.util.Map;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.IBlockTradeReport;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITradeReport;
import com.celera.ipc.ILifeCycle;

public interface IOMS extends ILifeCycle
{
	public void addListener(IOMSListener l);
	
	public void sendOrder(IOrder order);
	public void updateOrder(IOrder order);
	
	public boolean sendTradeReport(ITradeReport order);
//	public boolean sendBlockTradeReport(IBlockTradeReport block);
	public boolean sendBlockTradeReport(BlockTradeReport block ,Map<Long, List<ITradeReport>> map);
	
	public List<ITradeReport> getAllTradeReport();
}
