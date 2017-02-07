package com.celera.core.oms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dba.AbstractDatabaseAdapter;
import com.celera.core.dm.ITradeReport;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.mongo.repo.TradeReportRepo;

public class OrderLoggerServer extends AbstractDatabaseAdapter implements IOrderLoggerService
{
	private static final Logger logger = LoggerFactory.getLogger(OrderLoggerServer.class); 
	
	private final Date m_start;
	private final Date m_end;
	
	public OrderLoggerServer(Date start, Date end) {
		m_start = start;
		m_end = end;
	}
	
	@Override
	public void init()
	{
		loadTradeReports();
	}
	
	@Override
	public byte[] onQuery(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAdmin(byte[] data)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onResponse(byte[] data)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTask(byte[] data)
	{
		// TODO Auto-generated method stub
	}

	private void loadTradeReports () {
		TradeReportRepo repo = (TradeReportRepo) MongoDbAdapter.instance().get(TradeReportRepo.class);
		Collection<com.celera.mongo.entity.TradeReport> l = (Collection<com.celera.mongo.entity.TradeReport>) repo.findAll();
		l.forEach(c -> {
			if (m_start.getTime() <= c.getInputTime().getTime() 
					&& c.getInputTime().getTime() < m_end.getTime()) {
				customizedMap.put(c.getKey(), c);
				all.put(c.getId(), c);
			}
		});
		logger.info("load trade reports {}", l.size());
	}

	@Override
	public List<ITradeReport> getAllTradeReports()
	{
		List<ITradeReport> l = new ArrayList<ITradeReport>();
		for (Entry<String, IMongoDocument> e: all.entrySet()) {
			IMongoDocument doc = e.getValue();
			if (doc instanceof com.celera.mongo.entity.TradeReport) {
				l.add(((com.celera.mongo.entity.TradeReport)doc).fromEntityObject());
			}
		}
		return l;
	}
}
