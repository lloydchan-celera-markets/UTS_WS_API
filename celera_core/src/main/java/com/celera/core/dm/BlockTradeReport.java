package com.celera.core.dm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.message.cmmf.CmmfJson;

public class BlockTradeReport implements IOrder
{
	Logger logger = LoggerFactory.getLogger(BlockTradeReport.class);

	private static final int CMMF_SIZE = 69;

	private IInstrument instr = null;
	private EOrderStatus status = null;
	private ETradeReportType tradeReportType = null;
	private Integer qty = null;
	private Double price = null;
	private Long id = null;
	private Long refId = null;
	private String buyer = null;
	private String seller = null;
	
	private LocalDate lastTime = null;
	
	private List<TradeReport> list = new ArrayList<TradeReport>();

	public BlockTradeReport()
	{
	}

	public BlockTradeReport(IInstrument instr, EOrderStatus status, ETradeReportType tradeReportType,
			Integer qty, Double price, Long id, Long refId, String buyer, String seller)
	{
		super();
		this.instr = instr;
		this.status = status;
		this.tradeReportType = tradeReportType;
		this.qty = qty;
		this.price = price;
		this.id = id;
		this.refId = refId;
		this.buyer = buyer;
		this.seller = seller;
	}

	public void add(TradeReport tr) {
		list.add(tr);
	}
	
	public List<TradeReport> getList()
	{
		return list;
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

	public Long getRefId()
	{
		return refId;
	}

	public void setRefId(Long refId)
	{
		this.refId = refId;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}

	public LocalDate getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(LocalDate lastTime)
	{
		this.lastTime = lastTime;
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
		return "TradeReport [logger=" + logger + ", instr=" + instr + ", status=" + status + ", tradeReportType="
				+ tradeReportType + ", qty=" + qty + ", price=" + price + ", id=" + id  + ", buyer="
				+ buyer + ", seller=" + seller + ", lastTime=" + lastTime + "]";
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
		buf.putInt(qty);
		buf.putLong((long)(price * (double) IInstrument.CMMF_PRICE_FACTOR));
		buf.putLong(id);
		buf.putLong(refId);
		buf.put(StringUtils.rightPad(this.buyer, 7).getBytes());
		buf.put(StringUtils.rightPad(this.seller, 7).getBytes());
		
		buf.flip();
		return buf.array();
	}

	@Override
	public EOrderType getOrderType()
	{
		return null;
	}

	@Override
	public void setOrderType(EOrderType ordType)
	{
	}

	@Override
	public void setQty(Integer qty)
	{
		this.qty = qty;		
	}

	@Override
	public Integer getQty()
	{
		return qty;
	}

	@Override
	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();
//		builder.add("symbol", instr.getSymbol());
		builder.add(CmmfJson.ORDER_ID, this.id);
		builder.add(CmmfJson.REFERENCE_ID, this.refId);
		builder.add(CmmfJson.TRADE_REPORT_TYPE, this.tradeReportType.getName());
		builder.add(CmmfJson.BUYER, this.buyer);
		builder.add(CmmfJson.SELLER, this.seller);
		builder.add(CmmfJson.QTY, this.qty);
		if (this.instr instanceof IDerivative) {
			IDerivative deriv = (IDerivative)this.instr;
			String expiry = deriv.getExpiry();
			if (expiry != null) {
				builder.add(CmmfJson.FUTURE_MATURITY, expiry);
			}
			String symbol = deriv.getSymbol();
			if (symbol != null) {
				builder.add(CmmfJson.SYMBOL, symbol);
			}
			Double delta = deriv.getDelta();
			if (delta != null) {
				builder.add(CmmfJson.DELTA, delta);
			}
		}
		builder.add(CmmfJson.STATUS, this.status.toString());
		
		JsonArrayBuilder legs = Json.createArrayBuilder();
		for (TradeReport tr : this.list) {
			legs.add(tr.json());
		}
		
		builder.add(CmmfJson.LEGS, legs);
		JsonObject empJsonObject = builder.build();

		logger.debug("Block Trade Report JSON {}", empJsonObject);

		return empJsonObject;
	}
}
