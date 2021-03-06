package com.vectails.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vectails.common.GenericFactory;
import com.vectails.common.IGenericFactory;

import sun.reflect.generics.factory.GenericsFactory;

public interface IXmlParser
{
	Logger logger = LoggerFactory.getLogger(IXmlParser.class);
	
	public default Object create()
	{
		Object o = null;
		try
		{
			final Constructor<?> ctr = getClass().getConstructor();
			o = ctr.newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}

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
			} catch (Exception e)
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public default void parseLeaveNodes(Element root)
	{ // root = <Legs>

		String nodeName = null;

		NodeList nodes = root.getChildNodes();
		int lenNodes = nodes.getLength();
		for (int l = 0; l < lenNodes; l++)
		{ // elements
			Node n = nodes.item(l);

			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				nodeName = n.getNodeName();
				try
				{
					Field field = this.getClass().getDeclaredField(nodeName);
					if (Collection.class.isAssignableFrom(field.getType()))
					{
						parseLeaveNodes((Element) n);
					} else
					{
						Method setter = this.getClass().getMethod("set" + nodeName,
								// "set" + nodeName.substring(0,
								// 1).toUpperCase() +
								// nodeName.substring(1),
								field.getType());
						setter.invoke(this, n.getTextContent());
					}

				} catch (Exception e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
