package com.celera.core.dm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

public class Quote implements IQuote
{
	private EOrderStatus status = null;
	private IInstrument instr = null;
	private Long id = null;
	private Long refId = null;
	private String entity = null;
	private String quoteType = null;
	private Double bidPrice = null;
	private Long bidTime = null;
	private Long bidQty = null;
	private Double askPrice = null;
	private Long askTime = null;
	private Long askQty = null;
	
	private List<Addressee> addressees = new ArrayList<Addressee>();
	
	public Quote()
	{
	}
	
	public Quote(EOrderStatus status, IInstrument instr, Long id, Long refId, String entity, String quoteType, Double bidPrice,
			Long bidTime, Long bidQty, Double askPrice, Long askTime, Long askQty)
	{
		super();
		this.status = status;
		this.instr = instr;
		this.id = id;
		this.refId = refId;
		this.entity = entity;
		this.quoteType = quoteType;
		this.bidPrice = bidPrice;
		this.bidTime = bidTime;
		this.bidQty = bidQty;
		this.askPrice = askPrice;
		this.askTime = askTime;
		this.askQty = askQty;
	}

	public void setAddressees(List<Addressee> list)
	{
		addressees = list;
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

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		if (entity != null)
			this.entity = entity;
	}

	public String getQuoteType()
	{
		return quoteType;
	}

	public void setQuoteType(String quoteType)
	{
		this.quoteType = quoteType;
	}

	public Double getBidPrice()
	{
		return bidPrice;
	}

	public void setBidPrice(Double bidPrice)
	{
		if (bidPrice != null)
			this.bidPrice = bidPrice;
	}

	public Long getBidTime()
	{
		return bidTime;
	}

	public void setBidTime(Long bidTime)
	{
		if (bidTime != null)
			this.bidTime = bidTime;
	}

	public Long getBidQty()
	{
		return bidQty;
	}

	public void setBidQty(Long bidQty)
	{
		if (bidQty != null)
			this.bidQty = bidQty;
	}

	public Double getAskPrice()
	{
		return askPrice;
	}

	public void setAskPrice(Double askPrice)
	{
		if (askPrice != null)
			this.askPrice = askPrice;
	}

	public Long getAskTime()
	{
		return askTime;
	}

	public void setAskTime(Long askTime)
	{
		if (askTime != null)
			this.askTime = askTime;
	}

	public Long getAskQty()
	{
		return askQty;
	}

	public void setAskQty(Long askQty)
	{
		if (askQty != null)
			this.askQty = askQty;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Quote [status=").append(status).append(", instr=").append(instr)
				.append(", id=").append(id).append(", refId=").append(refId).append(", entity=").append(entity)
				.append(", quoteType=").append(quoteType).append(", bidPrice=").append(bidPrice).append(", bidTime=")
				.append(bidTime).append(", bidQty=").append(bidQty).append(", askPrice=").append(askPrice)
				.append(", askTime=").append(askTime).append(", askQty=").append(askQty).append(", [");
		for (Addressee o: addressees) {
			sb.append(o.toString()).append(",");
		}
		sb.append("]]");
		return sb.toString();
	}

//	@Override
	public void setQty(Long qty)
	{
	}

//	@Override
	public Integer getQty()
	{
		return null;
	}

//	@Override
	public void setPrice(Double p)
	{
	}

//	@Override
	public Double getPrice()
	{
		return null;
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
	public byte[] toCmmf()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setQty(Integer qty)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonObject json()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getLastUpdateTime()
	{
		return this.bidTime.compareTo(this.askTime) > 0 ? this.bidTime : this.askTime;
	}
}
