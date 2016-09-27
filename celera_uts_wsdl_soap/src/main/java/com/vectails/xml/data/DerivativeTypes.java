package com.vectails.xml.data;

import java.util.ArrayList;
import java.util.List;

import com.vectails.common.GenericFactory;
import com.vectails.xml.IXmlNode;

@SuppressWarnings("rawtypes")
public class DerivativeTypes extends GenericFactory implements IXmlNode
{
	public DerivativeTypes()
	{
		super(DerivativeType.class);
	}

//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DerivativeTypes [");
//		for (IXmlNode l: DerivativeType) {
//			sb.append(l.toString()).append(", ");
//		}
//		sb.append("]");
//		return sb.toString();
//	}
	
	List<IXmlNode> DerivativeType = new ArrayList<IXmlNode>();

	public List<IXmlNode> getDerivativeType()
	{
		return DerivativeType;
	}

}