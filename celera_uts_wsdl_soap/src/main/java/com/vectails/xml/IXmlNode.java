package com.vectails.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collection;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vectails.common.IGenericFactory;

public interface IXmlNode
{
	public default void parseAttribute(Element root)
	{
		NamedNodeMap m = root.getAttributes();
		int len = m.getLength();
		String nodeName;

		for (int j = 0; j < len; j++)
		{
			Node n = m.item(j);
			nodeName = n.getNodeName();
			try
			{
				Field field = this.getClass().getDeclaredField(nodeName);
				Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
				setter.invoke(this, n.getTextContent());
			} catch (NoSuchFieldException e)
			{
				System.out.println("NoSuchFieldException:" + e.getMessage() + "," + nodeName);
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				System.out.println("IllegalArgumentException:" + e.getMessage() + "," + nodeName);
				e.printStackTrace();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public default void parseNode(Element root) // root = <Legs>
	{
		parseAttribute(root);

		String nodeName = root.getNodeName();

		NodeList nodes = root.getChildNodes();
		int lenNodes = nodes.getLength();
		for (int l = 0; l < lenNodes; l++)
		{
			// elements
			Node n = nodes.item(l);

			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				nodeName = n.getNodeName();
				try
				{
					Field field = this.getClass().getDeclaredField(nodeName);
					field.setAccessible(true);

					if (Collection.class.isAssignableFrom(field.getType()))
					{
						// IXmlNode o = (IXmlNode) ((IGenericFactory)this).build();
						IXmlNode o = (IXmlNode) ((IGenericFactory) this)
								.build(IXmlTag.PACKAGE_XML_DATA_PREFIX + nodeName);
						o.parseNode((Element) n);

						Object obj = field.get(this);
						Method m = field.getType().getDeclaredMethod("add", Object.class);
						m.invoke(obj, o);
					} else
					{
						Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
						setter.invoke(this, n.getTextContent());
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}