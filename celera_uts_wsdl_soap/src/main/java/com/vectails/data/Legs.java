package com.vectails.data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IXmlNode;

public class Legs extends GenericFactory implements IXmlNode
{
	List<Leg> Leg = new ArrayList<Leg>();
	
	public Legs()
	{
		super(Leg.class);
	}

//	@Override
//	public String toString()
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append(this.getClass().getSimpleName()).append(" [");
//
//		for (Field field : getClass().getDeclaredFields())
//		{
//			try
//			{
//				if (Collection.class.isAssignableFrom(field.getType()))
//				{
//					Object listObj = field.get(this);
//
//					Method mthGet = field.getType().getDeclaredMethod("iterator", null);
//					Iterator begin = (Iterator) mthGet.invoke(listObj, null);
//					for (Iterator it = begin; it.hasNext(); ) {
//						sb.append(it.next().toString()).append(", ");
//					}
//				} else
//				{
//					String nodeName = field.getName();
//					Method setter = this.getClass().getMethod("get" + nodeName, null);
//					String s = (String) setter.invoke(this, null);
//					sb.append(s).append(", ");
//				}
//
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		sb.append("]");
//		return sb.toString();
//	}
	
	
//	@Override
//	public void parseNode(Element root) // root = <Legs>
//	{
//		parseAttribute(root);
//
//		String nodeName = root.getNodeName();
//
//		NodeList nodes = root.getChildNodes();
//		int lenNodes = nodes.getLength();
//		for (int l = 0; l < lenNodes; l++)
//		{ // elements
//			Node n = nodes.item(l);
//
//			if (n.getNodeType() == Node.ELEMENT_NODE)
//			{
//				nodeName = n.getNodeName();
//				try
//				{
//					Field field = this.getClass().getDeclaredField(nodeName);
//
//					if (Collection.class.isAssignableFrom(field.getType()))
//					{
//						IXmlNode o = (IXmlNode) this.build();
//						o.parseNode((Element) n);
//
//						Object obj = field.get(this);
//						Method m = field.getType().getDeclaredMethod("add", Object.class);
//						m.invoke(obj, o);
//					} else
//					{
//						Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
//						setter.invoke(this, n.getTextContent());
//					}
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}
