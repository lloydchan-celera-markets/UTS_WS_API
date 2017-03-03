package com.celera.core.dm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.service.staticdata.StaticDataService;
import com.celera.gateway.HkexOapiUtil;
import com.celera.message.cmmf.CmmfJson;
import com.celera.mongo.entity.ICustomizeMongoDocument;
import com.celera.mongo.repo.TradeReportRepo;

public class TradeReport implements ITradeReport
{
	private static final Logger logger = LoggerFactory.getLogger(TradeReport.class);

	private static final int CMMF_SIZE = 92;

//	private String id = null;
	
	private IInstrument instr = null;
	private EOrderStatus status = null;
	private ETradeReportType tradeReportType = null;
	private ESide side = null;
	private Double price = null;
	private Integer qty = null;
	private Long ordId = null;
	private Long refId = null;
	private Long groupId = null;
	private String buyer = null;
	private String seller = null;
	private String bGiveup = null;
	private String sGiveup = null;
	private Integer giveupNum = null;
	
	private String remark = null;
	
	private Long inputTime = null;
	private Long lastUpdateTime = null;
	

	public TradeReport()
	{
		inputTime = System.currentTimeMillis();
		lastUpdateTime = System.currentTimeMillis();
	}

	public TradeReport(IInstrument instr, EOrderStatus status, ETradeReportType tradeReportType, ESide side, 
			Integer qty, Double price, Long ordId, Long refId, String buyer, String seller)
	{
		super();
//		this.id = id;
		
		this.instr = instr;
		this.status = status;
		this.tradeReportType = tradeReportType;
		this.side = side;
		this.qty = qty;
		this.price = price;
		this.ordId = ordId;
		this.refId = refId;
		this.buyer = buyer;
		this.seller = seller;
		this.bGiveup = StaticDataService.instance().getClearingMember(buyer);
		this.sGiveup = StaticDataService.instance().getClearingMember(seller);
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

	public Long getOrderId()
	{
		return ordId;
	}

	public void setOrderId(Long id)
	{
		if (id != null)
			this.ordId = id;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public Long getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastTime)
	{
		this.lastUpdateTime = lastTime;
	}

	public Long getInputTime()
	{
		return inputTime;
	}

	public void setInputTime(Long inputTime)
	{
		this.inputTime = inputTime;
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
		return "TradeReport [instr=" + instr + ", status=" + status + ", tradeReportType=" + tradeReportType + ", side="
				+ side + ", price=" + price + ", qty=" + qty + ", ordId=" + ordId + ", refId=" + refId + ", groupId="
				+ groupId + ", buyer=" + buyer + ", seller=" + seller + ", bGiveup=" + bGiveup + ", sGiveup=" + sGiveup
				+ ", remark=" + remark + ", inputTime=" + inputTime + ", lastUpdateTime=" + lastUpdateTime + "]";
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
		buf.putLong(ordId);
		buf.putLong(refId);
		buf.put(HkexOapiUtil.rightPad(this.buyer, HkexOapiUtil.SIZE_CUSTOMER_INFO).getBytes());
		buf.put(HkexOapiUtil.rightPad(this.seller, HkexOapiUtil.SIZE_CUSTOMER_INFO).getBytes());
		buf.put(HkexOapiUtil.rightPad(this.bGiveup, HkexOapiUtil.SIZE_GIVEUP_MEMBER).getBytes());
		buf.put(HkexOapiUtil.rightPad(this.sGiveup, HkexOapiUtil.SIZE_GIVEUP_MEMBER).getBytes());
		if (this.bGiveup == null && this.sGiveup == null) {
			buf.put((byte)0x00);
		}
		else {
			buf.put((byte)0x01);
		}
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
		if (this.ordId != null)
			builder.add(CmmfJson.ORDER_ID, this.ordId);
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

	public ESide getSide()
	{
		return side;
	}

	public void setSide(ESide side)
	{
		this.side = side;
	}

	
	
	@Override
	public com.celera.mongo.entity.TradeReport toEntityObject()
	{
		String symbol = this.instr.getSymbol();
		String status = this.status.getName();
		String tradeReportType = this.tradeReportType.getName();
		Date dInputTime = new Date(this.inputTime);;
		Date dLastModified = new Date(this.lastUpdateTime);
		String side = this.side.getName();
		
//		List legs = new ArrayList();
		com.celera.mongo.entity.TradeReport tr = new com.celera.mongo.entity.TradeReport(null, symbol, status,
				tradeReportType, side, qty, price, ordId.toString(), refId.toString(), groupId.toString(), buyer, seller,
				remark, dInputTime, dLastModified, null);
		return tr;
	}

	@Override
	public void setGiveupNumber(Integer giveupNum)
	{
		this.giveupNum = giveupNum;
	}

	@Override
	public void update(Double price, Integer qty, String giveup, ESessionState state)
	{
	}
	
	@Override
	public IOrder clone() {
		ITradeReport clone = new TradeReport(this.instr, this.status, this.tradeReportType, this.side, 
				this.qty, this.price, this.ordId, this.refId, this.buyer, this.seller);
		return clone;
	}
	
	@Override
	public void addTrade(ITrade trade) {
	}
}
