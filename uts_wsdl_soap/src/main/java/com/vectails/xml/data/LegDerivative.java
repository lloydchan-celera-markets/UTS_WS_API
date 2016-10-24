package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class LegDerivative extends GenericFactory implements IXmlNode
{
	public LegDerivative()
	{
		super(LegDerivativeItems.class);
	}

	private String TypeName = null;
	private String TypeCode = null;

	List<IXmlNode> LegDerivativeItems = new ArrayList<IXmlNode>();

	public String getTypeName()
	{
		return TypeName;
	}

	public void setTypeName(String typeName)
	{
		TypeName = typeName;
	}

	public String getTypeCode()
	{
		return TypeCode;
	}

	public void setTypeCode(String typeCode)
	{
		TypeCode = typeCode;
	}

	public List<IXmlNode> getLegDerivativeItems()
	{
		return LegDerivativeItems;
	}

	public void setLegDerivativeItems(List<IXmlNode> legDerivativeItems)
	{
		LegDerivativeItems = legDerivativeItems;
	}
}
