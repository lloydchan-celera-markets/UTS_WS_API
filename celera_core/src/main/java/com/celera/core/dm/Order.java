package com.celera.core.dm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.gateway.HkexOapiUtil;
import com.celera.mongo.entity.TradeDetail;

public class Order implements IOrder
{
	Logger logger = LoggerFactory.getLogger(Order.class);

	private static final int CMMF_SIZE = 65;
	
	private EOrderStatus status = null;
	private IInstrument instr = null;
	private EOrderType orderType = null;
	private Long id = null;
	private Long refId = null;
	private String entity = null;
	private Double price = null;
	private Long lastUpdateTime = null;
	private Integer qty = null;
	private ESide side = null;
	private String giveup = null;

	public Order()
	{
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public Order(EOrderStatus status, IInstrument instr, EOrderType type, Long id, Long refId, String entity,
			Double price, Integer qty, ESide side, String giveup)
	{
		super();
		this.status = status;
		this.instr = instr;
		this.orderType = type;
		this.id = id;
		this.refId = refId;
		this.entity = entity;
		this.price = price;
		this.qty = qty;
		this.side = side;
		this.giveup = giveup;
		
		this.lastUpdateTime = System.currentTimeMillis();
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

	public Long getRefId()
	{
		return refId;
	}

	public void setRefId(Long refId)
	{
		this.refId = refId;
	}

	public Long getOrderId()
	{
		return id;
	}

	public void setOrderId(Long id)
	{
		if (id != null)
			this.id = id;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		if (entity != null)
			this.entity = entity;
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

	public void setLastUpdateTime(Long lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
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

	@Override
	public EOrderType getOrderType()
	{
		return this.orderType;
	}

	@Override
	public void setOrderType(EOrderType ordType)
	{
		this.orderType = ordType;
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
	public String toString()
	{
		return "Order [status=" + status + ", instr=" + instr + ", orderType=" + orderType + ", id=" + id + ", refId="
				+ refId + ", entity=" + entity + ", price=" + price + ", lastUpdateTime=" + lastUpdateTime + ", qty="
				+ qty + ", side=" + side + ", giveup=" + giveup + "]";
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
		buf.put((byte)orderType.value());
		buf.putLong(id);
//		buf.putLong(refId);
		buf.putLong((long)(price * (double) IInstrument.CMMF_PRICE_FACTOR));
		buf.putLong(qty);
//		if (instr instanceof IDerivative) {
//			buf.putLong((long)(((IDerivative)instr).getStrike() * (double)IInstrument.CMMF_PRICE_FACTOR));
//			buf.putLong((long)(((IDerivative)instr).getDelta() * (double)IInstrument.CMMF_PRICE_FACTOR));
//		}
//		else { 
//			buf.putLong(0);
//			buf.putLong(0);
//		}
		buf.put((byte)side.getAsInt());
		buf.put(HkexOapiUtil.rightPad(this.giveup, HkexOapiUtil.SIZE_GIVEUP_MEMBER).getBytes());
		if (this.giveup == null) {
			buf.put((byte)0x00);
		}
		else {
			buf.put((byte)0x01);
		}
		
		buf.flip();
		return buf.array();
	}

	@Override
	public Integer getQty()
	{
		return this.qty;
	}

	@Override
	public void setQty(Integer qty)
	{
		this.qty = qty;
	}
	
	@Override
	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("symbol", instr.getSymbol());
		builder.add("status", status.ordinal());
		builder.add("orderType", orderType.ordinal());
		builder.add("qty", qty);
		builder.add("id", id);
		builder.add("price", (price * IInstrument.CMMF_PRICE_FACTOR));
		if (instr instanceof IDerivative) {
			builder.add("strike", ((IDerivative)instr).getStrike() * IInstrument.CMMF_PRICE_FACTOR);
			builder.add("delta", ((IDerivative)instr).getDelta() * IInstrument.CMMF_PRICE_FACTOR);
		}
		else { 
			builder.add("strike", 0);
			builder.add("delta", 0);
		}
		
		JsonObject empJsonObject = builder.build();

		logger.debug("Order JSON {}", empJsonObject);

		return empJsonObject;
	}
	
//	public void readObject(ObjectInputStream iss) throws IOException
//	{
//		name = iss.readObject();
//		status = iss.readInt();
//		orderType = iss.readInt();
//		name = iss.readObject();
//		oss.writeInt(status.ordinal());
//		oss.writeInt(orderType.ordinal());
//		oss.writeLong(qty);
//		oss.writeLong(id);
//		oss.writeDouble(price);
//	}
}
