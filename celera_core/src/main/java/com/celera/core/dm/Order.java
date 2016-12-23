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

import com.celera.mongo.entity.TradeDetail;

public class Order implements IOrder
{
	Logger logger = LoggerFactory.getLogger(Order.class);

	private static final int CMMF_SIZE = 55;
	
	private EOrderStatus status = null;
	private IInstrument instr = null;
	private EOrderType orderType = null;
	private Long id = null;
	private String entity = null;
	private Double price = null;
	private LocalDate lastTime = null;
	private Integer qty = null;
	private ESide side = null;

	public Order()
	{
	}

	public Order(EOrderStatus status, IInstrument instr, EOrderType type, Long id, String entity,
			Double price, Integer qty, ESide side)
	{
		super();
		this.status = status;
		this.instr = instr;
		this.orderType = type;
		this.id = id;
		this.entity = entity;
		this.price = price;
		this.qty = qty;
		this.side = side;
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
		return "Order [logger=" + logger + ", status=" + status + ", instr=" + instr + ", orderType=" + orderType
				+ ", id=" + id + ", entity=" + entity + ", price=" + price + ", lastTime=" + lastTime + ", qty=" + qty
				+ "]";
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
		buf.putInt(qty);
		buf.putLong(id);
		buf.putLong((long)(price * (double) IInstrument.CMMF_PRICE_FACTOR));
//		if (instr instanceof IDerivative) {
//			buf.putLong((long)(((IDerivative)instr).getStrike() * (double)IInstrument.CMMF_PRICE_FACTOR));
//			buf.putLong((long)(((IDerivative)instr).getDelta() * (double)IInstrument.CMMF_PRICE_FACTOR));
//		}
//		else { 
//			buf.putLong(0);
//			buf.putLong(0);
//		}
		buf.put((byte)side.getAsInt());
		
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
