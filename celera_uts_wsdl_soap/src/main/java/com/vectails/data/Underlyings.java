package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

public class Underlyings extends GenericFactory implements IXmlNode 
{
	List<IXmlNode> Underlying = new ArrayList<IXmlNode>();
	
	public Underlyings()
	{
		super(Underlying.class);
	}
}