package com.celera.core.dm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import come.celera.core.oms.OMS;

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
		Long groupId = tr.getGroupId();
		List l = map.get(groupId);
		if (l == null) {
			l = new ArrayList<ITradeReport>();
			map.put(groupId, l);
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
	
	public void setBlockStatus(EOrderStatus status, String remark, Long groupId)
	{
//		for (Map.Entry<Long, List<ITradeReport>>  e : this.map.entrySet()) {
//			for (ITradeReport tr : e.getValue()) {
		List<ITradeReport> l = this.map.get(groupId);
		if (l == null) {
			logger.error("group id[{}] not exist in block {}" , groupId, this.toString());
			return;
		}
		
			for (ITradeReport tr : l) {
//				Long myId = tr.getId();
//				if (myId == groupId) {
					if (tr instanceof IBlockTradeReport) {
						((IBlockTradeReport)tr).setBlockStatus(status, remark, groupId);
					}
					else {
						tr.setStatus(status);
						tr.setRemark(remark);
					}
//				}
			}
//		}
		
		boolean hasFilled = false;
		boolean hasDiff = false;
		EOrderStatus prev = null;
		
		for (Map.Entry<Long, List<ITradeReport>>  e : this.map.entrySet())
		{
			ITradeReport tr = e.getValue().get(0);
			EOrderStatus current = tr.getStatus();
			if (prev == null) {
				prev = current;
				if (current == EOrderStatus.FILLED)
					hasFilled = true;
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
				+ tradeReportType + ", qty=" + qty + ", price=" + price + ", id=" + id + ", refId=" + refId + ", groupId=" + groupId + 
				", buyer=" + buyer + ", seller=" + seller + ", remark=" + remark + ", lastUpdateTime=" + lastUpdateTime + ", map="
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
		List<ITradeReport> l = new ArrayList<ITradeReport>();
		for (Map.Entry<Long, List<ITradeReport>> e: this.map.entrySet()) {
			for (ITradeReport tr : e.getValue()) {
				l.add(tr);
			}
		}
		buf.putInt(l.size());
		for (ITradeReport tr : l) {
			buf.put(tr.toCmmf());
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
		builder.add(CmmfJson.REMARK, this.remark == null ? "" : this.remark.toString());
		
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
	
	public static void main_block_split_PartialFilled(String[] arg)
	{
		OMS oms = OMS.instance();

		int pos = 1;
		Long groupId = 1l;
		Integer numLegs = 3;
		// Double legPrice = Double.parseDouble(tokens[pos++]);
		// Integer legQty = Integer.parseInt(tokens[pos++]);
		// String sTrType = tokens[pos++];
		// ETradeReportType trtype = ETradeReportType.get(sTrType);

		Long refId = new Date().getTime();

		IInstrument instr = new Derivative("HK", "SYHN", EInstrumentType.ECDIAG, EInstrumentType.ECDIAG.getName(), null,
				null, null, null, "", null, false, 10d);
		BlockTradeReport block = new BlockTradeReport(instr, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				1100, 10d, null, refId, "HKCEL", "HKCEL");
		block.setGroupId(1l);

		Map<Long, java.util.List<ITradeReport>> split = new HashMap<Long, java.util.List<ITradeReport>>();
		ArrayList<ITradeReport> l1 = new ArrayList<ITradeReport>();

		IInstrument instr1 = new Derivative("HK", "HSI18600L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr1 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 20d, null, refId, "HKCEL", "HKCEL");
		tr1.setGroupId(groupId);
		l1.add(tr1);

		IInstrument instr2 = new Derivative("HK", "HSI19400L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr2 = new TradeReport(instr2, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 8d, null, refId, "HKCEL", "HKCEL");
		tr2.setGroupId(groupId);
		l1.add(tr2);

		IInstrument instr3 = new Derivative("HK", "HSIG7", EInstrumentType.FUTURE, EInstrumentType.FUTURE.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr3 = new TradeReport(instr3, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 110, 19300d, null, refId, "HKCEL", "HKCEL");
		tr3.setGroupId(groupId);
		l1.add(tr3);

		split.put(groupId, l1);

		// split
		groupId = 2l;
		ArrayList<ITradeReport> l2 = new ArrayList<ITradeReport>();
		ITradeReport tr4 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 100, 20d, null, refId, "HKCEL", "HKCEL");
		tr4.setGroupId(groupId);
		l2.add(tr4);
		ITradeReport tr5 = new TradeReport(instr3, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 375, 8d, null, refId, "HKCEL", "HKCEL");
		tr5.setGroupId(groupId);
		l2.add(tr5);
		split.put(groupId, l2);
		
		oms.sendBlockTradeReport(block, split);
		block.setBlockStatus(EOrderStatus.FILLED, "", groupId);
		
		System.out.println(block);
	}
	
//	public static void main_block_split(String[] arg)
	public static void main(String[] arg)
	{
		OMS oms = OMS.instance();

		int pos = 1;
		Long groupId = 1l;
		Integer numLegs = 3;
		// Double legPrice = Double.parseDouble(tokens[pos++]);
		// Integer legQty = Integer.parseInt(tokens[pos++]);
		// String sTrType = tokens[pos++];
		// ETradeReportType trtype = ETradeReportType.get(sTrType);

		Long refId = new Date().getTime();

		IInstrument instr = new Derivative("HK", "SYHN", EInstrumentType.ECDIAG, EInstrumentType.ECDIAG.getName(), null,
				null, null, null, "", null, false, 10d);
		BlockTradeReport block = new BlockTradeReport(instr, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				1100, 10d, null, refId, "HKCEL", "HKCEL");
		block.setGroupId(1l);

		Map<Long, java.util.List<ITradeReport>> split = new HashMap<Long, java.util.List<ITradeReport>>();
		ArrayList<ITradeReport> l1 = new ArrayList<ITradeReport>();

		IInstrument instr1 = new Derivative("HK", "HSI18600L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr1 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 20d, null, refId, "HKCEL", "HKCEL");
		tr1.setGroupId(groupId);
		l1.add(tr1);

		IInstrument instr2 = new Derivative("HK", "HSI19400L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr2 = new TradeReport(instr2, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 8d, null, refId, "HKCEL", "HKCEL");
		tr2.setGroupId(groupId);
		l1.add(tr2);

		IInstrument instr3 = new Derivative("HK", "HSIG7", EInstrumentType.FUTURE, EInstrumentType.FUTURE.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr3 = new TradeReport(instr3, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 110, 19300d, null, refId, "HKCEL", "HKCEL");
		tr3.setGroupId(groupId);
		l1.add(tr3);

		split.put(groupId, l1);

		// split 2
		groupId = 2l;
		ArrayList<ITradeReport> l2 = new ArrayList<ITradeReport>();
		ITradeReport tr4 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 20d, null, refId, "HKCEL", "HKCEL");
		tr4.setGroupId(groupId);
		l2.add(tr4);
		ITradeReport tr5 = new TradeReport(instr2, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 8d, null, refId, "HKCEL", "HKCEL");
		tr5.setGroupId(groupId);
		l2.add(tr5);
		split.put(groupId, l2);
		
		// split 3
		groupId = 3l;
		ArrayList<ITradeReport> l3 = new ArrayList<ITradeReport>();
		ITradeReport tr6 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 100, 20d, null, refId, "HKCEL", "HKCEL");
		tr6.setGroupId(groupId);
		l3.add(tr6);
		ITradeReport tr7 = new TradeReport(instr2, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 375, 8d, null, refId, "HKCEL", "HKCEL");
		tr7.setGroupId(groupId);
		l3.add(tr7);
		split.put(groupId, l3);
		
		oms.sendBlockTradeReport(block, split);
		block.setBlockStatus(EOrderStatus.FILLED, "", 1l);	// PARTIAL FILLED
		System.out.println(block);
		System.out.println("==============================");	
		block.setBlockStatus(EOrderStatus.FILLED, "", 2l); // PARTIAL FILLED
		System.out.println(block);
		System.out.println("==============================");
		block.setBlockStatus(EOrderStatus.FILLED, "", 3l); // FILLED
		System.out.println(block);
		System.out.println("==============================");
		block.setBlockStatus(EOrderStatus.REJECTED, "", 1l); // PARTIAL FILLED
		System.out.println(block);
		System.out.println("==============================");
	}
	
	public static void main_block_split_T2_T1_Filled(String[] arg)
//	public static void main(String[] arg)
	{
		OMS oms = OMS.instance();
		
		int pos = 1;
		Long groupId = 1l;
		Integer numLegs = 3;
		// Double legPrice = Double.parseDouble(tokens[pos++]);
		// Integer legQty = Integer.parseInt(tokens[pos++]);
		// String sTrType = tokens[pos++];
		// ETradeReportType trtype = ETradeReportType.get(sTrType);
		
		Long refId = new Date().getTime();
		
		IInstrument instr = new Derivative("HK", "SYHN", EInstrumentType.ECDIAG, EInstrumentType.ECDIAG.getName(), null,
				null, null, null, "", null, false, 10d);
		BlockTradeReport block = new BlockTradeReport(instr, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				1100, 10d, null, refId, "HKCEL", "HKCEL");
		block.setGroupId(1l);
		
		Map<Long, java.util.List<ITradeReport>> split = new HashMap<Long, java.util.List<ITradeReport>>();
		ArrayList<ITradeReport> l1 = new ArrayList<ITradeReport>();
		
		IInstrument instr1 = new Derivative("HK", "HSI18600L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr1 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 20d, null, refId, "HKCEL", "HKCEL");
		tr1.setGroupId(groupId);
		l1.add(tr1);
		
		IInstrument instr2 = new Derivative("HK", "HSI19400L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr2 = new TradeReport(instr2, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 8d, null, refId, "HKCEL", "HKCEL");
		tr2.setGroupId(groupId);
		l1.add(tr2);
		
		IInstrument instr3 = new Derivative("HK", "HSIG7", EInstrumentType.FUTURE, EInstrumentType.FUTURE.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr3 = new TradeReport(instr3, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 110, 19300d, null, refId, "HKCEL", "HKCEL");
		tr3.setGroupId(groupId);
		l1.add(tr3);
		
		split.put(groupId, l1);
		
		// split
		groupId = 2l;
		ArrayList<ITradeReport> l2 = new ArrayList<ITradeReport>();
		ITradeReport tr4 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 100, 20d, null, refId, "HKCEL", "HKCEL");
		tr4.setGroupId(groupId);
		l2.add(tr4);
		split.put(groupId, l2);
		
		oms.sendBlockTradeReport(block, split);
		block.setBlockStatus(EOrderStatus.FILLED, "", 1l);
		System.out.println(block);
		block.setBlockStatus(EOrderStatus.FILLED, "", 2l);
		System.out.println(block);
		block.setBlockStatus(EOrderStatus.REJECTED, "", 1l);
		System.out.println(block);
	}
	
	public static void main_block_nosplit(String[] arg)
//	public static void main(String[] arg)
	{
		OMS oms = OMS.instance();
		
		int pos = 1;
		Long groupId = 1l;
		Integer numLegs = 3;
		// Double legPrice = Double.parseDouble(tokens[pos++]);
		// Integer legQty = Integer.parseInt(tokens[pos++]);
		// String sTrType = tokens[pos++];
		// ETradeReportType trtype = ETradeReportType.get(sTrType);
		
		Long refId = new Date().getTime();
		
		IInstrument instr = new Derivative("HK", "SYHN", EInstrumentType.ECDIAG, EInstrumentType.ECDIAG.getName(), null,
				null, null, null, "", null, false, 10d);
		BlockTradeReport block = new BlockTradeReport(instr, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				1100, 10d, null, refId, "HKCEL", "HKCEL");
		block.setGroupId(1l);
		
		Map<Long, java.util.List<ITradeReport>> split = new HashMap<Long, java.util.List<ITradeReport>>();
		ArrayList<ITradeReport> l1 = new ArrayList<ITradeReport>();
		
		IInstrument instr1 = new Derivative("HK", "HSI18600L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr1 = new TradeReport(instr1, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 20d, null, refId, "HKCEL", "HKCEL");
		tr1.setGroupId(groupId);
		l1.add(tr1);
		
		IInstrument instr2 = new Derivative("HK", "HSI19400L7", EInstrumentType.CALL, EInstrumentType.CALL.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr2 = new TradeReport(instr2, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 1000, 8d, null, refId, "HKCEL", "HKCEL");
		tr2.setGroupId(groupId);
		l1.add(tr2);
		
		IInstrument instr3 = new Derivative("HK", "HSIG7", EInstrumentType.FUTURE, EInstrumentType.FUTURE.getName(),
				null, null, null, null, "", null, false, 0d);
		ITradeReport tr3 = new TradeReport(instr3, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
				ESide.CROSS, 110, 19300d, null, refId, "HKCEL", "HKCEL");
		tr3.setGroupId(groupId);
		l1.add(tr3);
		
		split.put(groupId, l1);
		
		// split
		
		oms.sendBlockTradeReport(block, split);
		block.setBlockStatus(EOrderStatus.FILLED, "", 1l);
		System.out.println(block);
		block.setBlockStatus(EOrderStatus.FILLED, "", 2l);
		System.out.println(block);
	}
}
