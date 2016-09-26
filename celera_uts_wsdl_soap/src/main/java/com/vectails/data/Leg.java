package com.vectails.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;
//import com.vectails.data.IXmlParser.Leg;

public class Leg extends GenericFactory  implements IXmlNode
{

	// attributes
	private String Label = null;
	private String MultiUnderlyingItemType = null;
	private String MultiUnderlyingItemIndex = null;

	// elements
	private String Barrier = null;
	private String BarrierInPercent = null;
	private String Cap = null;
	private String Coupon = null;
	private String Delta = null;
	private String DerivativeType = null;
	private String DividendRequirement = null;
	private String EndOfBarrierDate = null;
	private String ExpiryDate = null;
	private String FutureBasis = null;
	private String FutureSpot = null;
	private String Garantee = null;
	private String KnockInBarrier = null;
	private String Leverage = null;
	private String LimitDown = null;
	private String LimitUp = null;
	private String Multiplier = null;
	private String Periodicity = null;
	private String QuantoCurrency = null;
	private String Currency = null;
	private String Radius = null;
	private String Rate = null;
	private String Rebate = null;
	private String RebateInPercent = null;
	private String ResetPeriodicity = null;
	private String Size = null;
	private String Spot = null;
	private String StartDate = null;
	private String Strike = null;
	private String StrikeInPercent = null;
	private String Until = null;
	// not in spec v1.8
	private String NAV = null;
	private String Frequency = null;

	public Leg()
	{
		super(Leg.class);
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

	public void setBarrier(String barrier)
	{
		Barrier = barrier;
	}

	public void setBarrierInPercent(String barrierInPercent)
	{
		BarrierInPercent = barrierInPercent;
	}

	public void setCap(String cap)
	{
		Cap = cap;
	}

	public void setCoupon(String coupon)
	{
		Coupon = coupon;
	}

	public void setDelta(String delta)
	{
		Delta = delta;
	}

	public void setDerivativeType(String derivativeType)
	{
		DerivativeType = derivativeType;
	}

	public void setDividendRequirement(String dividendRequirement)
	{
		DividendRequirement = dividendRequirement;
	}

	public void setEndOfBarrierDate(String endOfBarrierDate)
	{
		EndOfBarrierDate = endOfBarrierDate;
	}

	public void setExpiryDate(String expiryDate)
	{
		ExpiryDate = expiryDate;
	}

	public void setFutureBasis(String futureBasis)
	{
		FutureBasis = futureBasis;
	}

	public void setFutureSpot(String futureSpot)
	{
		FutureSpot = futureSpot;
	}

	public void setGarantee(String guarantee)
	{
		Garantee = guarantee;
	}

	public void setKnockInBarrier(String knockInBarrier)
	{
		KnockInBarrier = knockInBarrier;
	}

	public void setLeverage(String leverage)
	{
		Leverage = leverage;
	}

	public void setLimitDown(String limitDown)
	{
		LimitDown = limitDown;
	}

	public void setLimitUp(String limitUp)
	{
		LimitUp = limitUp;
	}

	public void setMultiplier(String multiplier)
	{
		Multiplier = multiplier;
	}

	public void setPeriodicity(String periodicity)
	{
		Periodicity = periodicity;
	}

	public void setQuantoCurrency(String quantoCurrency)
	{
		QuantoCurrency = quantoCurrency;
	}

	public void setCurrency(String currency)
	{
		Currency = currency;
	}

	public void setRadius(String radius)
	{
		Radius = radius;
	}

	public void setRate(String rate)
	{
		Rate = rate;
	}

	public void setRebate(String rebate)
	{
		Rebate = rebate;
	}

	public void setRebateInPercent(String rebateInPercent)
	{
		RebateInPercent = rebateInPercent;
	}

	public void setResetPeriodicity(String resetPeriodicity)
	{
		ResetPeriodicity = resetPeriodicity;
	}

	public void setSize(String size)
	{
		Size = size;
	}

	public void setSpot(String spot)
	{
		Spot = spot;
	}

	public void setStartDate(String startDate)
	{
		StartDate = startDate;
	}

	public void setStrike(String strike)
	{
		Strike = strike;
	}

	public void setStrikeInPercent(String strikeInPercent)
	{
		StrikeInPercent = strikeInPercent;
	}

	public void setUntil(String until)
	{
		Until = until;
	}

	public void setNAV(String nAV)
	{
		NAV = nAV;
	}

	public void setFrequency(String frequency)
	{
		Frequency = frequency;
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

	public String getBarrier()
	{
		return Barrier;
	}

	public String getBarrierInPercent()
	{
		return BarrierInPercent;
	}

	public String getCap()
	{
		return Cap;
	}

	public String getCoupon()
	{
		return Coupon;
	}

	public String getDelta()
	{
		return Delta;
	}

	public String getDerivativeType()
	{
		return DerivativeType;
	}

	public String getDividendRequirement()
	{
		return DividendRequirement;
	}

	public String getEndOfBarrierDate()
	{
		return EndOfBarrierDate;
	}

	public String getExpiryDate()
	{
		return ExpiryDate;
	}

	public String getFutureBasis()
	{
		return FutureBasis;
	}

	public String getFutureSpot()
	{
		return FutureSpot;
	}

	public String getGarantee()
	{
		return Garantee;
	}

	public String getKnockInBarrier()
	{
		return KnockInBarrier;
	}

	public String getLeverage()
	{
		return Leverage;
	}

	public String getLimitDown()
	{
		return LimitDown;
	}

	public String getLimitUp()
	{
		return LimitUp;
	}

	public String getMultiplier()
	{
		return Multiplier;
	}

	public String getPeriodicity()
	{
		return Periodicity;
	}

	public String getQuantoCurrency()
	{
		return QuantoCurrency;
	}

	public String getCurrency()
	{
		return Currency;
	}

	public String getRadius()
	{
		return Radius;
	}

	public String getRate()
	{
		return Rate;
	}

	public String getRebate()
	{
		return Rebate;
	}

	public String getRebateInPercent()
	{
		return RebateInPercent;
	}

	public String getResetPeriodicity()
	{
		return ResetPeriodicity;
	}

	public String getSize()
	{
		return Size;
	}

	public String getSpot()
	{
		return Spot;
	}

	public String getStartDate()
	{
		return StartDate;
	}

	public String getStrike()
	{
		return Strike;
	}

	public String getStrikeInPercent()
	{
		return StrikeInPercent;
	}

	public String getUntil()
	{
		return Until;
	}

	public String getNAV()
	{
		return NAV;
	}

	public String getFrequency()
	{
		return Frequency;
	}

//		@Override
//		public void parseNode(Element root) // root = <Legs>
//		{
//			parseAttribute(root);
//
//			String nodeName = root.getNodeName();
//
//			NodeList nodes = root.getChildNodes();
//			int lenNodes = nodes.getLength();
//			for (int l = 0; l < lenNodes; l++)
//			{ // elements
//				Node n = nodes.item(l);
//
//				if (n.getNodeType() == Node.ELEMENT_NODE)
//				{
//					nodeName = n.getNodeName();
//					try
//					{
//						Field field = this.getClass().getDeclaredField(nodeName);
//
//						if (Collection.class.isAssignableFrom(field.getType()))
//						{
//							IXmlNode o = (IXmlNode) this.build();
//							o.parseNode((Element) n);
//
//						} else
//						{
//							Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
//							setter.invoke(this, n.getTextContent());
//						}
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	
	// public default void parseNode(Element root) { // root = <Legs>
	//
	// String nodeName = root.getNodeName();
	// System.out.println(nodeName);
	//
	// NodeList nodes = root.getChildNodes();
	// int lenNodes = nodes.getLength();
	// for (int l = 0; l < lenNodes; l++) { // elements
	// Node n = nodes.item(l);
	//
	// if (n.getNodeType() == Node.ELEMENT_NODE) {
	// parseNode((Element)n);
	// }
	// }
	// }
}