package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.tag.ParameterTag;

public class Leg extends GenericFactory  implements IXmlNode
{
	// attributes (Quotes)
	private String Name = null;
	private String Index = null;
	
	// attributes (DerivativeType)
	private String Label = null;
	private String MultiUnderlyingItemType = null;
	private String MultiUnderlyingItemIndex = null;

	// elements (DerivativeType)
	private String QuantoCurrency = null;
	private String Radius = null;
	private String Delta = null;
	private String FutureBasis = null;
	private String FutureSpot = null;
	private String Size = null;
	private String Spot = null;
	private String Until = null;
	
	private ParameterTag Barrier = null;
	private ParameterTag BarrierInPercent = null;
	private ParameterTag Cap = null;
	private ParameterTag Coupon = null;

	private ParameterTag DerivativeType = null;
	private ParameterTag DividendRequirement = null;
	private ParameterTag EndOfBarrierDate = null;
	
	@LegMapping(value = {"setExpiry","java.lang.String",""})
	private ParameterTag ExpiryDate = null;
	@LegMapping(value = {"setMultiplier","java.lang.Double","valueOf"})
	private ParameterTag Multiplier = null;
	@LegMapping(value = {"setStrike","java.lang.String",""})
	private ParameterTag Strike = null;
	@LegMapping(value = {"setStrike","java.lang.String",""})
	private ParameterTag StrikeInPercent = null;
	
	private ParameterTag Garantee = null;
	private ParameterTag KnockInBarrier = null;
	private ParameterTag Leverage = null;
	private ParameterTag LimitDown = null;
	private ParameterTag LimitUp = null;
	private ParameterTag Periodicity = null;
	private ParameterTag Currency = null;
	private ParameterTag Rate = null;
	private ParameterTag Rebate = null;
	private ParameterTag RebateInPercent = null;
	private ParameterTag ResetPeriodicity = null;
	private ParameterTag StartDate = null;


	// not in spec v1.8
	private ParameterTag NAV = null;
	private ParameterTag Frequency = null;

	// elements (DerivativeType)
	private List<IXmlNode> LegDerivative = new ArrayList<IXmlNode>();
	private List<IXmlNode> LegUnderlying = new ArrayList<IXmlNode>();
	
	public Leg()
	{
		super(LegDerivative.class);
	}

	public void setLabel(String label)
	{
		Label = label;
	}

	public void setMultiUnderlyingItemType(String multiUnderlyingItemType)
	{
		MultiUnderlyingItemType = multiUnderlyingItemType;
	}

	public void setMultiUnderlyingItemIndex(String multiUnderlyingItemIndex)
	{
		MultiUnderlyingItemIndex = multiUnderlyingItemIndex;
	}

	public void setDelta(String delta)
	{
		Delta = delta;
	}

	public void setFutureBasis(String futureBasis)
	{
		FutureBasis = futureBasis;
	}

	public void setFutureSpot(String futureSpot)
	{
		FutureSpot = futureSpot;
	}

	public void setQuantoCurrency(String quantoCurrency)
	{
		QuantoCurrency = quantoCurrency;
	}

	public void setRadius(String radius)
	{
		Radius = radius;
	}

	public void setSize(String size)
	{
		Size = size;
	}

	public void setSpot(String spot)
	{
		Spot = spot;
	}

	public void setUntil(String until)
	{
		Until = until;
	}

//	@Override
//	public String toString()
//	{
//		return "Leg [Label=" + Label + ", MultiUnderlyingItemType=" + MultiUnderlyingItemType
//				+ ", MultiUnderlyingItemIndex=" + MultiUnderlyingItemIndex + ", Barrier=" + Barrier
//				+ ", BarrierInPercent=" + BarrierInPercent + ", Cap=" + Cap + ", Coupon=" + Coupon + ", Delta=" + Delta
//				+ ", DerivativeType=" + DerivativeType + ", DividendRequirement=" + DividendRequirement
//				+ ", EndOfBarrierDate=" + EndOfBarrierDate + ", ExpiryDate=" + ExpiryDate + ", FutureBasis="
//				+ FutureBasis + ", FutureSpot=" + FutureSpot + ", Garantee=" + Garantee + ", KnockInBarrier="
//				+ KnockInBarrier + ", Leverage=" + Leverage + ", LimitDown=" + LimitDown + ", LimitUp=" + LimitUp
//				+ ", Multiplier=" + Multiplier + ", Periodicity=" + Periodicity + ", QuantoCurrency=" + QuantoCurrency
//				+ ", Currency=" + Currency + ", Radius=" + Radius + ", Rate=" + Rate + ", Rebate=" + Rebate
//				+ ", RebateInPercent=" + RebateInPercent + ", ResetPeriodicity=" + ResetPeriodicity + ", Size=" + Size
//				+ ", Spot=" + Spot + ", StartDate=" + StartDate + ", Strike=" + Strike + ", StrikeInPercent="
//				+ StrikeInPercent + ", Until=" + Until + ", NAV=" + NAV + ", Frequency=" + Frequency + "]";
//	}
	
//	@Override
//	public String toString()
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append(this.getClass().getSimpleName()).append(" [");
//
//		for (Field field : getClass().getDeclaredFields())
//		{
//			try
//			{
//				if (Collection.class.isAssignableFrom(field.getType()))
//				{
//					Object listObj = field.get(this);
//
//					Method mthGet = field.getType().getDeclaredMethod("iterator", null);
//					Iterator begin = (Iterator) mthGet.invoke(listObj, null);
//					for (Iterator it = begin; it.hasNext(); ) {
//						sb.append(it.next().toString()).append(", ");
//					}
//				} else
//				{
//					String nodeName = field.getName();
//					Method setter = this.getClass().getMethod("get" + nodeName, null);
//					String s = (String) setter.invoke(this, null);
//					sb.append(s).append(", ");
//				}
//
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		sb.append("]");
//		return sb.toString();
//	}
	
	public String getLabel()
	{
		return Label;
	}

	public String getMultiUnderlyingItemType()
	{
		return MultiUnderlyingItemType;
	}

	public String getMultiUnderlyingItemIndex()
	{
		return MultiUnderlyingItemIndex;
	}

	public String getDelta()
	{
		return Delta;
	}

	public String getFutureBasis()
	{
		return FutureBasis;
	}

	public String getFutureSpot()
	{
		return FutureSpot;
	}

	public String getQuantoCurrency()
	{
		return QuantoCurrency;
	}

	public String getRadius()
	{
		return Radius;
	}

	public String getSize()
	{
		return Size;
	}

	public String getSpot()
	{
		return Spot;
	}

	public String getUntil()
	{
		return Until;
	}

	public String getName()
	{
		return Name;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public String getIndex()
	{
		return Index;
	}

	public void setIndex(String index)
	{
		Index = index;
	}

	public void setBarrier(ParameterTag barrier)
	{
		Barrier = barrier;
	}

	public void setBarrierInPercent(ParameterTag barrierInPercent)
	{
		BarrierInPercent = barrierInPercent;
	}

	public void setCap(ParameterTag cap)
	{
		Cap = cap;
	}

	public ParameterTag getBarrier()
	{
		return Barrier;
	}

	public ParameterTag getBarrierInPercent()
	{
		return BarrierInPercent;
	}

	public ParameterTag getCap()
	{
		return Cap;
	}

	public ParameterTag getCoupon()
	{
		return Coupon;
	}

	public void setCoupon(ParameterTag coupon)
	{
		Coupon = coupon;
	}

	public ParameterTag getDerivativeType()
	{
		return DerivativeType;
	}

	public void setDerivativeType(ParameterTag derivativeType)
	{
		DerivativeType = derivativeType;
	}

	public ParameterTag getDividendRequirement()
	{
		return DividendRequirement;
	}

	public void setDividendRequirement(ParameterTag dividendRequirement)
	{
		DividendRequirement = dividendRequirement;
	}

	public ParameterTag getEndOfBarrierDate()
	{
		return EndOfBarrierDate;
	}

	public void setEndOfBarrierDate(ParameterTag endOfBarrierDate)
	{
		EndOfBarrierDate = endOfBarrierDate;
	}

	public ParameterTag getExpiryDate()
	{
		return ExpiryDate;
	}

	public void setExpiryDate(ParameterTag expiryDate)
	{
		ExpiryDate = expiryDate;
	}

	public ParameterTag getGarantee()
	{
		return Garantee;
	}

	public void setGarantee(ParameterTag garantee)
	{
		Garantee = garantee;
	}

	public ParameterTag getKnockInBarrier()
	{
		return KnockInBarrier;
	}

	public void setKnockInBarrier(ParameterTag knockInBarrier)
	{
		KnockInBarrier = knockInBarrier;
	}

	public ParameterTag getLeverage()
	{
		return Leverage;
	}

	public void setLeverage(ParameterTag leverage)
	{
		Leverage = leverage;
	}

	public ParameterTag getLimitDown()
	{
		return LimitDown;
	}

	public void setLimitDown(ParameterTag limitDown)
	{
		LimitDown = limitDown;
	}

	public ParameterTag getLimitUp()
	{
		return LimitUp;
	}

	public void setLimitUp(ParameterTag limitUp)
	{
		LimitUp = limitUp;
	}

	public ParameterTag getMultiplier()
	{
		return Multiplier;
	}

	public void setMultiplier(ParameterTag multiplier)
	{
		Multiplier = multiplier;
	}

	public ParameterTag getPeriodicity()
	{
		return Periodicity;
	}

	public void setPeriodicity(ParameterTag periodicity)
	{
		Periodicity = periodicity;
	}

	public ParameterTag getCurrency()
	{
		return Currency;
	}

	public void setCurrency(ParameterTag currency)
	{
		Currency = currency;
	}

	public ParameterTag getRate()
	{
		return Rate;
	}

	public void setRate(ParameterTag rate)
	{
		Rate = rate;
	}

	public ParameterTag getRebate()
	{
		return Rebate;
	}

	public void setRebate(ParameterTag rebate)
	{
		Rebate = rebate;
	}

	public ParameterTag getRebateInPercent()
	{
		return RebateInPercent;
	}

	public void setRebateInPercent(ParameterTag rebateInPercent)
	{
		RebateInPercent = rebateInPercent;
	}

	public ParameterTag getResetPeriodicity()
	{
		return ResetPeriodicity;
	}

	public void setResetPeriodicity(ParameterTag resetPeriodicity)
	{
		ResetPeriodicity = resetPeriodicity;
	}

	public ParameterTag getStartDate()
	{
		return StartDate;
	}

	public void setStartDate(ParameterTag startDate)
	{
		StartDate = startDate;
	}

	public ParameterTag getStrike()
	{
		return Strike;
	}

	public void setStrike(ParameterTag strike)
	{
		Strike = strike;
	}

	public ParameterTag getStrikeInPercent()
	{
		return StrikeInPercent;
	}

	public void setStrikeInPercent(ParameterTag strikeInPercent)
	{
		StrikeInPercent = strikeInPercent;
	}

	public ParameterTag getNAV()
	{
		return NAV;
	}

	public void setNAV(ParameterTag nAV)
	{
		NAV = nAV;
	}

	public ParameterTag getFrequency()
	{
		return Frequency;
	}

	public void setFrequency(ParameterTag frequency)
	{
		Frequency = frequency;
	}

	public List<IXmlNode> getLegDerivative()
	{
		return LegDerivative;
	}

	public void setLegDerivative(List<IXmlNode> legDerivative)
	{
		LegDerivative = legDerivative;
	}

	public List<IXmlNode> getLegUnderlying()
	{
		return LegUnderlying;
	}

	public void setLegUnderlying(List<IXmlNode> legUnderlying)
	{
		LegUnderlying = legUnderlying;
	}
}