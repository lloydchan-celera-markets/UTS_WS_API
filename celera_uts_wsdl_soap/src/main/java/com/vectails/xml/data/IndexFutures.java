package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class IndexFutures extends GenericFactory implements IXmlNode
{
	public IndexFutures()
	{
		super(IndexFuture.class);
	}

	List<IXmlNode> IndexFuture = new ArrayList<IXmlNode>();
}