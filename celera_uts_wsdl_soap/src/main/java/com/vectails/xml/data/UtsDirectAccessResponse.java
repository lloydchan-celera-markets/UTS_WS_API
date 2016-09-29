package com.vectails.xml.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vectails.common.GenericFactory;
import com.vectails.common.IGenericFactory;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.IXmlTag;

@SuppressWarnings("rawtypes")
public class UtsDirectAccessResponse extends GenericFactory implements IXmlNode
{
	public UtsDirectAccessResponse()
	{
		super(DerivativeTypes.class);
	}

	private String TimeOfLastRecoveredQuotes = null;
	private String Error = null;

	List<IXmlNode> DerivativeTypes = new ArrayList<IXmlNode>();
	List<IXmlNode> Currencies = new ArrayList<IXmlNode>();
	List<IXmlNode> Underlyings = new ArrayList<IXmlNode>();
	List<IXmlNode> IndexFutures = new ArrayList<IXmlNode>();
	List<IXmlNode> AllowedQuoteCreators = new ArrayList<IXmlNode>();
	List<IXmlNode> Addressees = new ArrayList<IXmlNode>();
	List<IXmlNode> Quotes = new ArrayList<IXmlNode>();

	public String getError()
	{
		return Error;
	}

	public void setError(String error)
	{
		Error = error;
	}

	public String getTimeOfLastRecoveredQuotes()
	{
		return TimeOfLastRecoveredQuotes;
	}

	public void setTimeOfLastRecoveredQuotes(String timeOfLastRecoveredQuotes)
	{
		TimeOfLastRecoveredQuotes = timeOfLastRecoveredQuotes;
	}

	public List<IXmlNode> getDerivativeTypes()
	{
		return DerivativeTypes;
	}

	public List<IXmlNode> getCurrencies()
	{
		return Currencies;
	}

	public List<IXmlNode> getUnderlyings()
	{
		return Underlyings;
	}

	public List<IXmlNode> getIndexFutures()
	{
		return IndexFutures;
	}

	public List<IXmlNode> getAddressees()
	{
		return Addressees;
	}

	public List<IXmlNode> getQuotes()
	{
		return Quotes;
	}

	@Override
	public void parseAttribute(Element root)
	{
	}

	// @Override
	// public void parseNode(Element root) // root = <Legs>
	// {
	// parseAttribute(root);
	//
	// String nodeName = root.getNodeName();
	//
	// NodeList nodes = root.getChildNodes();
	// int lenNodes = nodes.getLength();
	// for (int l = 0; l < lenNodes; l++)
	// { // elements
	// Node n = nodes.item(l);
	//
	// if (n.getNodeType() == Node.ELEMENT_NODE)
	// {
	// nodeName = n.getNodeName();
	// try
	// {
	// Field field = this.getClass().getDeclaredField(nodeName);
	// field.setAccessible(true);
	//
	// if (Collection.class.isAssignableFrom(field.getType()))
	// {
	//// IXmlNode o = null;
	//// switch (nodeName) {
	//// case "AllowedQuoteCreators":
	//// o = (IXmlNode)((IGenericFactory)
	// this).build("com.vectails.data.IXmlParser$AllowedQuoteCreators");
	//// break;
	//// case "IndexFutures":
	//// o = (IXmlNode)((IGenericFactory)
	// this).build("com.vectails.data.IXmlParser$IndexFutures");
	//// break;
	//// case "Underlyings":
	//// o = (IXmlNode)((IGenericFactory)
	// this).build("com.vectails.data.IXmlParser$Underlyings");
	//// break;
	//// case "Currencies":
	//// o = (IXmlNode)((IGenericFactory)
	// this).build("com.vectails.data.IXmlParser$Currencies");
	//// break;
	//// case "DerivativeTypes":
	//// o = (IXmlNode)((IGenericFactory)
	// this).build("com.vectails.data.IXmlParser$DerivativeTypes");
	//// break;
	//// }
	// IXmlNode o = (IXmlNode)((IGenericFactory)
	// this).build(IXmlTag.PACKAGE_XML_DATA_PREFIX + nodeName);
	// o.parseNode((Element) n);
	//
	// Object obj = field.get(this);
	// Method m = field.getType().getDeclaredMethod("add", Object.class);
	// m.invoke(obj, o);
	// } else
	// {
	// Method setter = this.getClass().getMethod("set" + nodeName,
	// field.getType());
	// setter.invoke(this, n.getTextContent());
	// }
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
}