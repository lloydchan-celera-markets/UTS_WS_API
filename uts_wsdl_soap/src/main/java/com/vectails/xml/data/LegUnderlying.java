package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

public class LegUnderlying extends GenericFactory  implements IXmlNode
{
	// attributes
	private String Type = null;
	// element
	List<IXmlNode> LegSingleUnderlying = new ArrayList<IXmlNode>();
	
	public LegUnderlying()
	{
		super(LegSingleUnderlying.class);
	}

	public String getType()
	{
		return Type;
	}

	public void setType(String type)
	{
		Type = type;
	}

	public List<IXmlNode> getLegSingleUnderlying()
	{
		return LegSingleUnderlying;
	}

	public void setLegSingleUnderlying(List<IXmlNode> legSingleUnderlying)
	{
		LegSingleUnderlying = legSingleUnderlying;
	}
}