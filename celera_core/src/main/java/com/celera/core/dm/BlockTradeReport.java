package com.celera.core.dm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.message.cmmf.CmmfBuilder;
import com.celera.message.cmmf.CmmfJson;
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ECommand;
import com.celera.message.cmmf.EMessageType;
import com.itextpdf.text.pdf.events.IndexEvents.Entry;

public class BlockTradeReport implements IBlockTradeReport
{
	Logger logger = LoggerFactory.getLogger(BlockTradeReport.class);

	private static final int CMMF_SIZE = 13;

	private IInstrument instr = null;
	private EOrderStatus status = null;
	private ETradeReportType tradeReportType = null;
	private Integer qty = null;
	private Double price = null;
	private Long id = null;
	private Long refId = null;	// Web UI assigned
	private Long groupId = null;	// Web UI assigned 
	private String buyer = null;
	private String seller = null;
	private String remark = null;
	
	private Long lastUpdateTime = null;
	
	private Map<Long, List<ITradeReport>> map = new LinkedHashMap<Long, List<ITradeReport>>();

	public BlockTradeReport()
	{
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public BlockTradeReport(BlockTradeReport other) {
		super();
		this.instr = other.instr;
		this.status = other.status;
		this.tradeReportType = other.tradeReportType;
		this.qty = other.qty;
		this.price = other.price;
		this.id = other.id;
		this.refId = other.refId;
		this.buyer = other.buyer;
		this.seller = other.seller;
		
		this.lastUpdateTime = System.currentTimeMillis();
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
		
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public void add(ITradeReport tr) {
		Long id = tr.getId();
		List l = map.get(id);
		if (l == null) {
			l = new ArrayList<ITradeReport>();
			map.put(id, l);
		}
		l.add(tr);
	}

	@SuppressWarnings("unchecked")
	public List<ITradeReport> getList()
	{
		List<ITradeReport> l = new ArrayList<ITradeReport>();
		for (List<ITradeReport> e: map.values()) {
			for (ITradeReport tr : e) {
				l.add(tr);
			}
		}
		return l;
//		List<ITradeReport> list = new ArrayList<ITradeReport>((Collection<? extends ITradeReport>) map.values());
//		return list;
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

//	public void setStatus(EOrderStatus status)
//	{
//		for (Map.Entry<Long, List<ITradeReport>>  e : this.map.entrySet()) {
//			for (ITradeReport tr : e.getValue()) {
//				if (tr instanceof IBlockTradeReport) {}
//				else {
//					tr.setStatus(status);
//				}
//			}
//		}
//	}
	
	public void setBlockStatus(EOrderStatus status, Long groupId)
	{
		for (Map.Entry<Long, List<ITradeReport>>  e : this.map.entrySet()) {
			for (ITradeReport tr : e.getValue()) {
				Long myId = tr.getId();
				if (myId == groupId) {
					if (tr instanceof IBlockTradeReport) {
						((IBlockTradeReport)tr).setBlockStatus(status, groupId);
					}
					else {
						tr.setStatus(status);
					}
				}
			}
		}
		
		boolean hasFilled = false;
		boolean hasDiff = false;
		EOrderStatus prev = null;
		
		for (Map.Entry<Long, List<ITradeReport>>  e : this.map.entrySet()) {
			ITradeReport tr = e.getValue().get(0);
			EOrderStatus current = tr.getStatus();
			if (prev == null) {
				prev = current;
			}
			else {
				if (prev != current) 
					hasDiff = true;
				if (current == EOrderStatus.FILLED)
					hasFilled = true;
			}
		}
		String sStatus = "";
		if (hasFilled) {
			sStatus += "FILLED";
			if (hasDiff) {
				sStatus = "PARTIAL_" + sStatus;
			}
		}
		else {
			sStatus += "REJECTED";
		}
		EOrderStatus ordStatus = EOrderStatus.get(sStatus);
		this.status = ordStatus;
		this.lastUpdateTime = System.currentTimeMillis();
	}
//	public void setBlockStatus(EOrderStatus status, Long id)
//	{
//		EOrderStatus minStatus = null;
//		EOrderStatus maxStatus = null;
//		for (Map.Entry<Long, List<ITradeReport>>  e : this.map.entrySet()) {
//			ITradeReport tr = e.getValue().get(0);
//			EOrderStatus current = tr.getStatus();
//			Long currentId = tr.getId();
//			
//			if (minStatus == null) {
//				minStatus = current;
//				maxStatus = current;
//			}
//			else {
//				
//				if (minStatus.ordinal() > current.ordinal()) {
//					minStatus = current;
//				}
//				if (maxStatus.ordinal() < current.ordinal()) {
//					maxStatus = current;
//				}
//			}
//			if (id == currentId) {
//				tr.setStatus(status);
//			}
//		}
//		this.lastUpdateTime = System.currentTimeMillis();
//	}

	private EOrderStatus deduceStatus(EOrderStatus s1, EOrderStatus s2) {
		if (s1.ordinal() == s2.ordinal()) 
			return s1;
		
		EOrderStatus sts;
		if (s1.ordinal() > s2.ordinal()) {
			sts =  EOrderStatus.get("PARTIAL_" + s2.getName());
			if (sts == null) {
				sts = EOrderStatus.get("PARTIAL_" + s1.getName());
			}
		}
		else {
			sts =  EOrderStatus.get("PARTIAL_" + s1.getName());
			if (sts == null) {
				sts = EOrderStatus.get("PARTIAL_" + s2.getName());
			}
		}
		return sts;
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
		return "BlockTradeReport [instr=" + instr + ", orderStatus=" + status + ", tradeReportType="
				+ tradeReportType + ", qty=" + qty + ", price=" + price + ", id=" + id + ", refId=" + refId + ", buyer="
				+ buyer + ", seller=" + seller + ", remark=" + remark + ", lastUpdateTime=" + lastUpdateTime + ", map="
				+ map + "]";
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
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put((byte)tradeReportType.value());
		buf.putLong(id);
		buf.putInt(this.map.size());
		for (Map.Entry<Long, List<ITradeReport>> e: this.map.entrySet()) {
			for (ITradeReport l : e.getValue())
				buf.put(l.toCmmf());
		}
		buf.flip();
		int limit = buf.limit();
		byte[] b = new byte[limit];
		buf.get(b, 0, limit);
		return b;
	}

	public Long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(Long groupId)
	{
		this.groupId = groupId;
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
		builder.add(CmmfJson.LAST_UPDATE_TIME, this.lastUpdateTime);
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
		for (Map.Entry<Long, List<ITradeReport>> e : this.map.entrySet()) {
			for (ITradeReport tr : e.getValue()) {
				if (tr instanceof IBlockTradeReport) {
					for (ITradeReport leg : ((IBlockTradeReport)tr).getList()) {
						legs.add(leg.json());
					}
				}
				else {
					legs.add(tr.json());
				}
			}
		}
		
		builder.add(CmmfJson.LEGS, legs);
		JsonObject empJsonObject = builder.build();

		logger.debug("Block Trade Report JSON {}", empJsonObject);

		return empJsonObject;
	}

	@Override
	public String getRemark()
	{
		return remark;		
	}

	@Override
	public void setRemark(String text)
	{
		this.remark = text;
	}

	@Override
	public void setStatus(EOrderStatus status)
	{
		logger.error("obsolete setStatus to [{}]", status);
	}
	
	public boolean hasSplit() {
		return this.map.keySet().size() > 1;
	}
}
