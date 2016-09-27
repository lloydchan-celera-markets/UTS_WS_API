package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class Addressees extends GenericFactory implements IXmlNode 
{
	List<IXmlNode> Addressee = new ArrayList<IXmlNode>();
	
	public Addressees()
	{
		super(Addressee.class);
	}
}
