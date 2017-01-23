package come.celera.core.oms;

import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;

public interface IOMSListener
{
	public void onOrder(IOrder o); 
	public void onQuote(IOrder q); 
	public void onTrade(ITrade t); 
	
	public void onTradeReport(ITradeReport tr); 
//	public void onBlockTradeReport(ITradeReport block); 
}
