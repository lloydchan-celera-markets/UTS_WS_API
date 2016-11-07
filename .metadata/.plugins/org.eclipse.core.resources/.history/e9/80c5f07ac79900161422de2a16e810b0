package com.vectails.xml.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vectails.common.GenericFactory;
import com.vectails.common.IGenericFactory;
import com.vectails.message.processor.Uts2Dm;
import com.vectails.session.IUtsLastTimeUpdateListener;
import com.vectails.xml.IUtsLastTimeUpdater;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.IXmlTag;

@SuppressWarnings("rawtypes")
public class UtsDirectAccessResponse extends GenericFactory implements IXmlNode, IUtsLastTimeUpdater
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

	public void parseAttribute(Element root)
	{
	}

	public LocalDate getLastTime()
	{
		return Uts2Dm.toLocalDate(TimeOfLastRecoveredQuotes);
	}
	
	public void updateLastTime(IUtsLastTimeUpdateListener l)
	{
		l.setTimeofLastRecoveredQuotes(TimeOfLastRecoveredQuotes);
	}
}