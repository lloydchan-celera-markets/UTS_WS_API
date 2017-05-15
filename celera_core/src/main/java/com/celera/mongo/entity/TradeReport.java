package com.celera.mongo.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.ESide;
import com.celera.core.dm.ETradeReportType;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.ITradeReport;
import com.celera.core.service.staticdata.StaticDataService;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.repo.TradeReportRepo;

@Document(collection = "tradereport")
public class TradeReport implements ICustomizeMongoDocument<TradeReportRepo>
{
	Logger logger = LoggerFactory.getLogger(TradeReport.class);

	private String id = null;
	
	private String symbol = null;
	private String status = null;
	private String tradeReportType = null;
	private String side = null;
	private Integer qty = null;
	private Double price = null;
	private String ordId = null;
	private String refId = null;	// Web UI assigned
	private String groupId = null;	// Web UI assigned 
	private String buyer = null;
	private String seller = null;
	private String remark = null;
	
	
	private Date inputTime = null;
	private Date lastModified = null;
	
	private List<ITradeReport> legs = null;

	@PersistenceConstructor
	public TradeReport(String id, String symbol, String status, String tradeReportType, String side, Integer qty, Double price,
			String ordId, String refId, String groupId, String buyer, String seller, String remark, Date inputTime,
			Date lastModified, List<ITradeReport> legs)
	{
		super();
		this.symbol = symbol;
		this.status = status;
		this.tradeReportType = tradeReportType;
		this.qty = qty;
		this.price = price;
		this.side = side;
		this.ordId = ordId;
		this.refId = refId;
		this.groupId = groupId;
		this.buyer = buyer;
		this.seller = seller;
		this.remark = remark;
		this.inputTime = inputTime;
		this.lastModified = lastModified;
		this.legs = legs;
	}
	
	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	public List<ITradeReport> getLegs()
	{
		return legs;
	}

	public String getBuyer()
	{
		return buyer;
	}

	public void setBuyer(String buyer)
	{
		this.buyer = buyer;
	}

	public String getSeller()
	{
		return seller;
	}

	public void setSeller(String seller)
	{
		this.seller = seller;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getTradeReportType()
	{
		return tradeReportType;
	}

	public void setTradeReportType(String tradeReportType)
	{
		this.tradeReportType = tradeReportType;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public String getRefId()
	{
		return refId;
	}

	public void setRefId(String refId)
	{
		this.refId = refId;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Date getInputTime()
	{
		return inputTime;
	}

	public void setInputTime(Date inputTime)
	{
		this.inputTime = inputTime;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	public void setLegs(List<ITradeReport> legs)
	{
		this.legs = legs;
	}

	@Override
	public String getId()
	{
		return this.ordId;
	}

	@Override
	public String toString() {
		String s = "TradeReport [symbol=" + symbol + ", status=" + status + ", tradeReportType=" + tradeReportType
				+ ", side=" + side + ", qty=" + qty + ", price=" + price + ", id=" + ordId + ", refId=" + refId
				+ ", groupId=" + groupId + ", buyer=" + buyer + ", seller=" + seller + ", remark=" + remark
				+ ", inputTime=" + inputTime + ", lastModified=" + lastModified + ", legs=";
		for (ITradeReport tr : legs) {
			s += tr.toString() + ",";
		};
		s += "]";
		return s;
	}
	
	@Override
	public void setLastModified(Date d)
	{
		this.lastModified = d;
	}

	@Override
	public Class getRepo()
	{
		return TradeReportRepo.class;
	}

	@Override
	public void setId(String id)
	{
		this.ordId = id;
	}

	@Override
	public String getKey()
	{
		return this.ordId + "_" + this.groupId + "_" + this.refId;
	}
	
	public ITradeReport fromEntityObject()
	{
		IInstrument instr = StaticDataService.instance().getInstr(this.symbol);
		EOrderStatus status = EOrderStatus.get(this.status);
		ETradeReportType tradeReportType = ETradeReportType.get(this.tradeReportType);
		ESide side = ESide.get(this.side);
		Integer qty = this.qty;
		Double price = this.price;
		Long ordId = Long.valueOf(this.ordId);
		Long refId = Long.valueOf(this.refId);
		Long groupId = Long.valueOf(this.groupId);
		Long lut = this.lastModified.getTime();
		Long it = this.inputTime.getTime();
		
		if (this.legs == null) {	// trade report
			com.celera.core.dm.TradeReport tr = new com.celera.core.dm.TradeReport(instr, status, tradeReportType,
					side, qty, price, ordId, refId, this.buyer, this.seller);
			tr.setLastUpdateTime(lut);
			tr.setInputTime(it);
			return tr;
		}
		else {	// block trade report
			BlockTradeReport block = new BlockTradeReport(instr, status, tradeReportType, qty, price, ordId,
					refId, groupId, this.buyer, this.seller, this.remark, it, lut, null);
			for (ITradeReport tr : legs) {
				block.add(tr);
			}
			return block;
		}
	}
}
