package com.celera.core.dm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.message.cmmf.CmmfJson;

public class TradeReport implements ITradeReport
{
	Logger logger = LoggerFactory.getLogger(TradeReport.class);

	private static final int CMMF_SIZE = 81;

	private IInstrument instr = null;
	private EOrderStatus status = null;
	private ETradeReportType tradeReportType = null;
	private ESide side = null;
	private Double price = null;
	private Integer qty = null;
	private Long id = null;
	private Long refId = null;
	private Long groupId = null;
	private String buyer = null;
	private String seller = null;
	
	private String remark = null;
	
	private Long inputTime = null;
	private Long lastUpdateTime = null;
	

	public TradeReport()
	{
		inputTime = System.currentTimeMillis();
		lastUpdateTime = System.currentTimeMillis();
	}

	public TradeReport(IInstrument instr, EOrderStatus status, ETradeReportType tradeReportType, ESide side, 
			Integer qty, Double price, Long id, Long refId, String buyer, String seller)
	{
		super();
		this.instr = instr;
		this.status = status;
		this.tradeReportType = tradeReportType;
		this.side = side;
		this.qty = qty;
		this.price = price;
		this.id = id;
		this.refId = refId;
		this.buyer = buyer;
		this.seller = seller;
		
		inputTime = System.currentTimeMillis();
		lastUpdateTime = System.currentTimeMillis();
	}

	public Long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(Long groupId)
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

	public EOrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(EOrderStatus status)
	{
		if (status != null)
			this.status = status;
		lastUpdateTime = System.currentTimeMillis();
	}

	public IInstrument getInstr()
	{
		return instr;
	}

	public void setInstr(IInstrument instr)
	{
		if (instr != null)
			this.instr = instr;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		if (id != null)
			this.id = id;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}

	public Long getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastTime)
	{
		this.lastUpdateTime = lastTime;
	}

	// @Override
	public void setPrice(Double p)
	{
		this.price = p;
	}

	// @Override
	public Double getPrice()
	{
		return price;
	}

	public ETradeReportType getTradeReportType()
	{
		return tradeReportType;
	}

	public void setTradeReportType(ETradeReportType tradeReportType)
	{
		this.tradeReportType = tradeReportType;
	}

	@Override
	public String toString()
	{
		return "TradeReport [instr=" + instr + ", orderStatus=" + status + ", tradeReportType="
				+ tradeReportType + ", side=" + side + ", price=" + price + ", qty=" + qty + ", id=" + id + ", refId="
				+ refId + ", groupId=" + groupId + ", buyer=" + buyer + ", seller=" + seller + ", remark=" + remark + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}

//	public byte[] toBytes()
//	{
//		try
//		{
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			ObjectOutputStream out = new ObjectOutputStream(bos);
//			writeObject(out);
//			// out.writeObject(this);
//			return bos.toByteArray();
//		} catch (IOException e)
//		{
//			logger.error("", e);
//		}
//		return null;
//	}
	
//	public void writeObject() throws IOException
	// TODO Unit test
	public byte[] toCmmf() throws IOException
	{
//		ByteBuffer buf = ByteBuffer.allocate(5);
//		buf.put((byte)1);
//		buf.putInt(1);

		ByteBuffer buf = ByteBuffer.allocate(CMMF_SIZE);
		buf.put(StringUtils.rightPad(instr.getSymbol(), 32).getBytes());
		buf.put((byte)status.ordinal());
		buf.put((byte)tradeReportType.value());
		buf.putLong(qty);
		buf.putLong((long)(price * (double) IInstrument.CMMF_PRICE_FACTOR));
		buf.put((byte)side.getAsInt());
		buf.putLong(id);
		buf.putLong(refId);
		buf.put(StringUtils.rightPad(this.buyer, 7).getBytes());
		buf.put(StringUtils.rightPad(this.seller, 7).getBytes());
		
		buf.flip();
		return buf.array();
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;		
	}

	public Integer getQty()
	{
		return qty;
	}

	@Override
	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (this.id != null)
			builder.add(CmmfJson.ORDER_ID, this.id);
		if (this.refId != null)
			builder.add(CmmfJson.REFERENCE_ID, this.refId);
		if (this.groupId != null)
			builder.add(CmmfJson.GROUP, this.groupId);
		
		builder.add(CmmfJson.TRADE_REPORT_TYPE, this.tradeReportType.getName());
		
		String symbol = this.instr.getSymbol();
		if (symbol != null)
			builder.add(CmmfJson.INSTRUMENT, symbol);
		if (symbol != null)
			builder.add(CmmfJson.SYMBOL, symbol);
		String ul = this.instr.getName();
		if (ul != null)
			builder.add(CmmfJson.UL, ul);
		if (this.price != null)
			builder.add(CmmfJson.PRICE, this.price);
		if (this.qty != null)
			builder.add(CmmfJson.QTY, this.qty);
		if (this.buyer != null)
			builder.add(CmmfJson.BUYER, this.buyer.toString());
		if (this.seller != null)
			builder.add(CmmfJson.SELLER, this.seller.toString());
		
		if (this.instr instanceof IDerivative) {
			String expiry = ((IDerivative) this.instr).getExpiry();
			if (expiry != null)
				builder.add(CmmfJson.EXPIRY, expiry);
			Double strike = ((IDerivative) this.instr).getStrike();
			if (strike != null)
				builder.add(CmmfJson.STRIKE, strike);
		}
		builder.add(CmmfJson.STATUS, this.status.toString());
		builder.add(CmmfJson.REMARK, this.remark == null ? "" : this.remark);
		builder.add(CmmfJson.LAST_UPDATE_TIME, this.lastUpdateTime);
		builder.add(CmmfJson.INPUT_TIME, this.inputTime);
		
		JsonObject empJsonObject = builder.build();

		logger.debug("Trade Report JSON {}", empJsonObject);

		return empJsonObject;
	}

	public Long getRefId()
	{
		return refId;
	}

	public void setRefId(Long refId)
	{
		this.refId = refId;
	}

	public EOrderType getOrderType()
	{
		return EOrderType.LIMIT;
	}

	public void setOrderType(EOrderType ordType)
	{
	}
}
