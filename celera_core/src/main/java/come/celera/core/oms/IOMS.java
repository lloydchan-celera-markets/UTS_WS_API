package come.celera.core.oms;

import java.util.List;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.IOrder;

public interface IOMS
{
	public void addListener(IOMSListener l);
	
	public void sendOrder(IOrder order);
	public void updateOrder(IOrder order);
	
	public void sendTradeReport(IOrder order);
	public void sendBlockTradeReport(BlockTradeReport block);
	
	public List<IOrder> getAllTradeReport();
}
