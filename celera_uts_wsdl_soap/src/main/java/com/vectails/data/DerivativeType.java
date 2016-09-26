package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class DerivativeType extends GenericFactory implements IXmlNode
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

	List<Legs> Legs = new ArrayList<Legs>();
	
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

	public List<Legs> getLegs()
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
	
//	@Override
//	public void parseNode(Element root) // root = <Legs>
//	{
//		parseAttribute(root);
//		
//		String nodeName = root.getNodeName();
//		
//		NodeList nodes = root.getChildNodes();
//		int lenNodes = nodes.getLength();
//		for (int l = 0; l < lenNodes; l++)
//		{ // elements
//			Node n = nodes.item(l);
//
//			if (n.getNodeType() == Node.ELEMENT_NODE)
//			{
//				nodeName = n.getNodeName();
////				try
////				{
////					Class<?> clazz = Class.forName("com.vectails.data.IXmlParser$" + nodeName);
////					if (IGenericFactory.class.isAssignableFrom(clazz))
////					{
//						Field field;
//						try
//						{
//							field = this.getClass().getDeclaredField(nodeName);
//
//							if (Collection.class.isAssignableFrom(field.getType()))
//							{
//								IXmlNode o = new IXmlParser.Legs();
//								o.parseNode((Element) n);
//								
//								Object obj = field.get(this);
//								Method m = field.getType().getDeclaredMethod("add", Object.class);
//								m.invoke(obj, o);
//							} else
//							{
//								Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
//								setter.invoke(this, n.getTextContent());
//							}
//						} catch (Exception e)
//						{
//							System.out.println("nodeName="+nodeName);
//							e.printStackTrace();
//						}
////					}
////				} catch (ClassNotFoundException e)
////				{
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			}
//		}
//	}
}
