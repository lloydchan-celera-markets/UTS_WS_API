package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

public class Underlyings extends GenericFactory implements IXmlNode 
{
	List<IXmlNode> Underlying = new ArrayList<IXmlNode>();
	
	public Underlyings()
	{
		super(Underlying.class);
	}

	public List<IXmlNode> getUnderlying()
	{
		return Underlying;
	}
}