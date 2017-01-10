package come.celera.core.oms;

import java.util.List;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;

public interface IOMSListener
{
	public void onOrder(IOrder o); 
	public void onQuote(IOrder q); 
	public void onTrade(ITrade t); 
	
	
	public void onTradeReport(IOrder tr); 
	public void onBlockTradeReport(IOrder block); 
}
