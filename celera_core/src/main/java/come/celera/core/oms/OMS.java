package come.celera.core.oms;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.TradeReport;
import com.celera.gateway.IOrderGatewayListener;

public class OMS implements IOMS, IOrderGatewayListener
{
	final static Logger logger = LoggerFactory.getLogger(OMS.class);

	private Map<Long, IOrder> orders = new ConcurrentHashMap<Long, IOrder>();
	private Map<Long, ITrade> trades = new ConcurrentHashMap<Long, ITrade>();

	private List<IOMSListener> listeners = new ArrayList<IOMSListener>();
	
	static private OMS INSTANCE = null;

	private Long id = 1l;
	
	static synchronized public OMS instance()
	{
		if (INSTANCE == null)
			INSTANCE = new OMS();
		return INSTANCE;
	}

	public void addListener(IOMSListener l) {
		listeners.add(l);
	}
	
	public IOrder get(Long id)
	{
		return orders.get(id);
	}

	public void onOrder(IOrder o)
	{
		Long id = o.getId();
		orders.put(id, o);
		logger.info("onOrder: " + o.toString());
	}

	public void onQuote(IOrder o)
	{
		Long id = o.getId();
		if (!orders.containsKey(id))
		{
			orders.put(id, o);
		}
		logger.info("onQuote: " + o.toString());
	}

	public void onTrade(ITrade o)
	{
		Long id = o.getId();
		ITrade t = trades.put(id, o);
		if (t != null)
		{
			logger.info("onTrade update: " + o.toString());
		}
		else
		{
			logger.info("onTrade new: " + o.toString());
		}
	}
	
	public void sendOrder(IOrder order){}
	public void updateOrder(IOrder order){}

	public void sendTradeReport(IOrder order)
	{
		try
		{
			long id = this.id++;
			// TODO: simulate result for testing
			Thread.sleep(1000);
			order.setId(id);
			order.setStatus(EOrderStatus.SENT);
			for (IOMSListener l : listeners) {
				l.onTradeReport(order);
			}
			orders.put(id, order);
			
		} catch (InterruptedException e)
		{
			logger.error("{}", e);
		}
	}
	
	public void sendBlockTradeReport(BlockTradeReport block)
	{
		try
		{
			long id = this.id++;
			// TODO: simulate result for testing
			Thread.sleep(1000);
			block.setId(id);
			block.setStatus(EOrderStatus.SENT);
			for (IOMSListener l : listeners) {
				l.onBlockTradeReport(block);
			}
			
			orders.put(id, block);
			
		} catch (InterruptedException e)
		{
			logger.error("{}", e);
		}
	}
	
	public List<IOrder> getAllTradeReport() {
		List<IOrder> l = new ArrayList<IOrder>();
		for (IOrder order : this.orders.values()) {
			if (order instanceof TradeReport || order instanceof BlockTradeReport)
				l.add(order);
		}
		Collections.sort(l, new Comparator<IOrder>() {
			public int compare(IOrder o1, IOrder o2) {
				return o1.getLastUpdateTime().compareTo(o2.getLastUpdateTime());
			}
		});
		return l;
	}
}
