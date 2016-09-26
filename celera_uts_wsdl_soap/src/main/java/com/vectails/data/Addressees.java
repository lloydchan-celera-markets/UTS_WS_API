package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class Addressees extends GenericFactory implements IXmlNode 
{
	List<IXmlNode> Addressee = new ArrayList<IXmlNode>();
	
	public Addressees()
	{
		super(Addressee.class);
	}
}
