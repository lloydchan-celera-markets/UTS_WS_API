package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class IndexFutures extends GenericFactory implements IXmlNode
{
	public IndexFutures()
	{
		super(IndexFuture.class);
	}

	List<IXmlNode> IndexFuture = new ArrayList<IXmlNode>();
}