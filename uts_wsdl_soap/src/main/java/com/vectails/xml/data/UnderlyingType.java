package com.vectails.xml.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class UnderlyingType extends GenericFactory implements IXmlNode
{
	private String FinancialMarketCode = null; 
	private String UnderlyingTypeCode = null;
	
	List<String> UnderlyingCode = new ArrayList<String>();
	
	public UnderlyingType()
	{
		super(UnderlyingType.class);
	}
	
	public String getFinancialMarketCode()
	{
		return FinancialMarketCode;
	}
	public void setFinancialMarketCode(String financialMarketCode)
	{
		FinancialMarketCode = financialMarketCode;
	}
	public String getUnderlyingTypeCode()
	{
		return UnderlyingTypeCode;
	}
	public void setUnderlyingTypeCode(String underlyingTypeCode)
	{
		UnderlyingTypeCode = underlyingTypeCode;
	}
	
	@Override
	public void parseNode(Element root) // root = <Legs>
	{
		parseAttribute(root);

		String nodeName = root.getNodeName();

		NodeList nodes = root.getChildNodes();
		int lenNodes = nodes.getLength();
		for (int l = 0; l < lenNodes; l++)
		{ // elements
			Node n = nodes.item(l);

			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				nodeName = n.getNodeName();
				try
				{
					Field field = this.getClass().getDeclaredField(nodeName);
					field.setAccessible(true);
					
					if (Collection.class.isAssignableFrom(field.getType()))
					{
						Object obj = field.get(this);
						Method m = field.getType().getDeclaredMethod("add", Object.class);
						m.invoke(obj, n.getTextContent());
					} else
					{
						Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
						setter.invoke(this, n.getTextContent());
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}