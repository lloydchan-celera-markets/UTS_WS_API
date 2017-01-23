package come.celera.core.oms;

import java.util.List;
import java.util.Map;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.IBlockTradeReport;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITradeReport;

public interface IOMS
{
	public void addListener(IOMSListener l);
	
	public void sendOrder(IOrder order);
	public void updateOrder(IOrder order);
	
	public void sendTradeReport(ITradeReport order);
	public void sendBlockTradeReport(IBlockTradeReport block);
	public void sendBlockTradeReport(BlockTradeReport block ,Map<Long, List<ITradeReport>> map);
	
	public List<ITradeReport> getAllTradeReport();
}
