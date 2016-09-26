package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class SubscribedUnderlyings extends GenericFactory implements IXmlNode
{
	List<IXmlNode> UnderlyingType = new ArrayList<IXmlNode>();

	public SubscribedUnderlyings()
	{
		super(UnderlyingType.class);
	}
}