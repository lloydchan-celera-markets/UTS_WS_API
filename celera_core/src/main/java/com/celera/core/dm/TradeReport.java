package com.celera.core.dm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;

import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeReport implements IOrder
{
	Logger logger = LoggerFactory.getLogger(TradeReport.class);

	private static final int CMMF_SIZE = 69;

	private IInstrument instr = null;
	private EOrderStatus status = null;
	private ETradeReportType tradeReportType = null;
	private Integer qty = null;
	private Double price = null;
	private Long id = null;
	private ESide side = null;
	private String company = null;
	private String cpCompany = null;
	
	private LocalDate lastTime = null;
	

	public TradeReport()
	{
	}

	public TradeReport(IInstrument instr, EOrderStatus status, ETradeReportType tradeReportType,
			Integer qty, Double price, Long id, ESide side, String company, String cpCompany)
	{
		super();
		this.instr = instr;
		this.status = status;
		this.tradeReportType = tradeReportType;
		this.qty = qty;
		this.price = price;
		this.id = id;
		this.side = side;
		this.company = company;
		this.cpCompany = cpCompany;
	}

	public String getCpCompany()
	{
		return cpCompany;
	}

	public void setCpCompany(String cpCompany)
	{
		this.cpCompany = cpCompany;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
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

	
	public ESide getSide()
	{
		return side;
	}

	public void setSide(ESide side)
	{
		this.side = side;
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
				+ tradeReportType + ", qty=" + qty + ", price=" + price + ", id=" + id + ", side=" + side + ", company="
				+ company + ", cpCompany=" + cpCompany + ", lastTime=" + lastTime + "]";
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
		buf.put((byte)side.getAsInt());
		buf.putLong(id);
		buf.put(StringUtils.rightPad(this.company, 7).getBytes());
		buf.put(StringUtils.rightPad(this.cpCompany, 7).getBytes());
		
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
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public JsonObject json()
//	{
//		JsonObjectBuilder builder = Json.createObjectBuilder();
////		builder.add("symbol", instr.getSymbol());
////		builder.add("status", status.ordinal());
////		builder.add("orderType", orderType.ordinal());
////		builder.add("qty", qty);
////		builder.add("id", id);
////		builder.add("price", (price * IInstrument.CMMF_PRICE_FACTOR));
////		if (instr instanceof IDerivative) {
////			builder.add("strike", ((IDerivative)instr).getStrike() * IInstrument.CMMF_PRICE_FACTOR);
////			builder.add("delta", ((IDerivative)instr).getDelta() * IInstrument.CMMF_PRICE_FACTOR);
////		}
////		else { 
////			builder.add("strike", 0);
////			builder.add("delta", 0);
////		}
////		
//		JsonObject empJsonObject = builder.build();
//
//		logger.debug("Order JSON {}", empJsonObject);
//
//		return empJsonObject;
//	}
}
