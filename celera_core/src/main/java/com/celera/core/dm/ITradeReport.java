package com.celera.core.dm;

import java.io.IOException;

import javax.json.JsonObject;

public interface ITradeReport extends IOrder 
{
	public ETradeReportType getTradeReportType();
	public void setTradeReportType(ETradeReportType trType);
	
	public String getBuyer();
	public String getSeller();
	
	public String getRemark();
	public void setRemark(String text);
	
	public Long getGroupId();
	public void setGroupId(Long groupId);
	
	public void setGiveupNumber(Integer giveupNum);
	
	public com.celera.mongo.entity.TradeReport toEntityObject();
}
