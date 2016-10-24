package com.vectails.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vectails.common.IGenericFactory;
import com.vectails.message.processor.UtsMessageProcessor;
import com.vectails.xml.data.tag.ParameterTag;

public interface IXmlNode
{
	final static Logger logger = LoggerFactory.getLogger(IXmlNode.class);
	
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
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	public default void parseNode(Element root) // root = <Legs>
	{
		parseAttribute(root);
		
		String nodeName = null;

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
					Class clazz = field.getType();
					
					if (Collection.class.isAssignableFrom(clazz))
					{
						// IXmlNode o = (IXmlNode) ((IGenericFactory)this).build();
						IXmlNode o = (IXmlNode) ((IGenericFactory) this)
								.build(IXmlTag.PACKAGE_XML_DATA_PREFIX + nodeName);
						o.parseNode((Element) n);

						Object obj = field.get(this);
						Method m = field.getType().getDeclaredMethod("add", Object.class);
						m.invoke(obj, o);
					} else if (ParameterTag.class.isAssignableFrom(clazz))
					{
						Node attr = n.getAttributes().getNamedItem("NameInString");
						ParameterTag tag = new ParameterTag();
						
						if (attr != null)
						{
							String nameInString = attr.getTextContent();
							tag.setNameInString(nameInString);
						}

						String value = n.getTextContent();
						tag.setValue(value);
						
						Method setter = this.getClass().getMethod("set" + nodeName, field.getType());
						setter.invoke(this, tag);
					}
					else
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
//	public default void parseNode(Element root) // root = <Legs>
//	{
//		parseAttribute(root);
//
//		String nodeName = root.getNodeName();
//
//		NodeList nodes = root.getChildNodes();
//		int lenNodes = nodes.getLength();
//		for (int l = 0; l < lenNodes; l++)
//		{
//			// elements
//			Node n = nodes.item(l);
//
//			if (n.getNodeType() == Node.ELEMENT_NODE)
//			{
//				nodeName = n.getNodeName();
//				try
//				{
//					Field field = this.getClass().getDeclaredField(nodeName);
//					field.setAccessible(true);
//
//					if (Collection.class.isAssignableFrom(field.getType()))
//					{
//						// IXmlNode o = (IXmlNode) ((IGenericFactory)this).build();
//						IXmlNode o = (IXmlNode) ((IGenericFactory) this)
//								.build(IXmlTag.PACKAGE_XML_DATA_PREFIX + nodeName);
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