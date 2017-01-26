package com.celera.core.dm;

import java.util.List;

public interface IBlockTradeReport extends ITradeReport
{
	public List<ITradeReport> getList();
	public void setBlockStatus(EOrderStatus status, String remark, Long id);
	public boolean hasSplit();
}

