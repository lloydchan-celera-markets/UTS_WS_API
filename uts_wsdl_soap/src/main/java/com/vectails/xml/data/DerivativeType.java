package com.vectails.xml.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.uts.tools.Uts2Dm;
import com.vectails.common.GenericFactory;
import com.vectails.session.IUtsLastTimeUpdateListener;
import com.vectails.xml.IUtsLastTimeUpdater;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class DerivativeType extends GenericFactory implements IXmlNode, IUtsLastTimeUpdater
{
	public DerivativeType()
	{
		super(Legs.class);
	}

	private String Code = null;
	private String Name = null;
	private String IsPriceInPercent = null;
	private String LegCount = null;
	private String IsBasic = null;
	private String UnderlyingClass = null;
	private String LastUpdateDateTime = null;

	private String ParameterString = null;

	List<IXmlNode> Legs = new ArrayList<IXmlNode>();
	
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DerivativeType [Code=").append(Code)
//			.append(", Name=").append(Name)
//			.append(", IsPriceInPercent=").append(IsPriceInPercent)
//			.append(", LegCount=").append(LegCount)
//			.append(", IsBasic=").append(IsBasic)
//			.append(", UnderlyingClass=").append(UnderlyingClass)
//			.append(", LastUpdateDateTime=").append(LastUpdateDateTime)
//			.append(", ");
//		for (IXmlNode l: Legs) {
//			sb.append(l.toString()).append(", ");
//		}
//		sb.append("]");
//		return sb.toString();
//	}
	
	public String getCode()
	{
		return Code;
	}

	public String getName()
	{
		return Name;
	}

	public String getIsPriceInPercent()
	{
		return IsPriceInPercent;
	}

	public String getLegCount()
	{
		return LegCount;
	}

	public String getIsBasic()
	{
		return IsBasic;
	}

	public String getUnderlyingClass()
	{
		return UnderlyingClass;
	}

	public String getLastUpdateDateTime()
	{
		return LastUpdateDateTime;
	}

	public String getParameterString()
	{
		return ParameterString;
	}

	public List<IXmlNode> getLegs()
	{
		return Legs;
	}

	public void setCode(String code)
	{
		Code = code;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public void setIsPriceInPercent(String isPriceInPercent)
	{
		IsPriceInPercent = isPriceInPercent;
	}

	public void setLegCount(String legCount)
	{
		LegCount = legCount;
	}

	public void setIsBasic(String isBasic)
	{
		IsBasic = isBasic;
	}

	public void setUnderlyingClass(String underlyingClass)
	{
		UnderlyingClass = underlyingClass;
	}

	public void setLastUpdateDateTime(String lastUpdateDateTime)
	{
		LastUpdateDateTime = lastUpdateDateTime;
	}

	public void setParameterString(String parameterString)
	{
		ParameterString = parameterString;
	}
	
	@Override
	public LocalDate getLastTime()
	{
		return Uts2Dm.toLocalDate(LastUpdateDateTime);
	}

	@Override
	public void updateLastTime(IUtsLastTimeUpdateListener l)
	{
		l.setDerivTypeLT(getLastTime());
	}
}
