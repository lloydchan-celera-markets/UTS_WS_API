package come.celera.core.oms;

import com.celera.core.dm.IOrder;

public interface IOMS
{
	public void sendOrder(IOrder order);
	public void updateOrder(IOrder order);
}
