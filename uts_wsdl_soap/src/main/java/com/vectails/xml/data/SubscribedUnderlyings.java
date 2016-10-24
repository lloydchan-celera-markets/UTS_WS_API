package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class SubscribedUnderlyings extends GenericFactory implements IXmlNode
{
	List<IXmlNode> UnderlyingType = new ArrayList<IXmlNode>();

	public SubscribedUnderlyings()
	{
		super(UnderlyingType.class);
	}
}