package com.celera.core.oms;

import java.util.List;

import com.celera.core.dm.ITradeReport;

public interface IOrderLoggerService
{
	public List<ITradeReport> getAllTradeReports();
}
