package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

public class LegDerivativeItems extends GenericFactory implements IXmlNode
{
	public LegDerivativeItems()
	{
		super(LegDerivativeItem.class);
	}

	List<IXmlNode> LegDerivativeItem = new ArrayList<IXmlNode>();
	
}
