package com.vectails.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.celera.core.common.GenericFactory;
import com.celera.core.common.IGenericFactory;
import com.celera.core.common.IXmlNode;

@SuppressWarnings("rawtypes")
public class UtsDirectAccessResponse extends GenericFactory implements IXmlNode 
	{
		public UtsDirectAccessResponse()
		{
			super(DerivativeTypes.class);
		}

		List<IXmlNode> DerivativeTypes = new ArrayList<IXmlNode>();
		List<IXmlNode> Currencies = new ArrayList<IXmlNode>();
		List<IXmlNode> Underlyings = new ArrayList<IXmlNode>();
		List<IXmlNode> IndexFutures = new ArrayList<IXmlNode>();
		List<IXmlNode> AllowedQuoteCreators = new ArrayList<IXmlNode>();
		List<IXmlNode> Addressees = new ArrayList<IXmlNode>();

		public List<IXmlNode> getDerivativeTypes()
		{
			return DerivativeTypes;
		}

		public List<IXmlNode> getCurrencies()
		{
			return Currencies;
		}

		@Override
		public void parseAttribute(Element root){}
		
		@Override
		public void parseNode(Element root) // root = <Legs>
		{
			parseAttribute(root);

			String nodeName = root.getNodeName();

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
						field.setAccessible(true);
						
						if (Collection.class.isAssignableFrom(field.getType()))
						{
//							IXmlNode o = null;
//							switch (nodeName) {
//							case "AllowedQuoteCreators":
//								o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$AllowedQuoteCreators");	
//								break;
//							case "IndexFutures":
//								o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$IndexFutures");	
//								break;
//							case "Underlyings":
//								o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$Underlyings");	
//								break;
//							case "Currencies":
//								o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$Currencies");	
//								break;
//							case "DerivativeTypes":
//								o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$DerivativeTypes");	
//								break;
//							}
							IXmlNode o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data." + nodeName);	
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