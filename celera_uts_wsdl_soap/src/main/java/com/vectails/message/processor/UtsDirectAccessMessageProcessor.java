package com.vectails.message.processor;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.vectails.data.StaticDataManager;
import com.vectails.message.ICommonFields;
import com.vectails.session.IOnUpdateNode;
import com.vectails.session.UtsDirectAccessClient;
import com.vectails.xml.INodeUpdateListener;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.DerivativeType;
import com.vectails.xml.data.DerivativeTypes;
import com.vectails.xml.data.IndexFuture;
import com.vectails.xml.data.IndexFutures;
import com.vectails.xml.data.Leg;
import com.vectails.xml.data.LegDerivative;
import com.vectails.xml.data.LegDerivativeItem;
import com.vectails.xml.data.LegDerivativeItems;
import com.vectails.xml.data.LegMapping;
import com.vectails.xml.data.LegSingleUnderlying;
import com.vectails.xml.data.LegUnderlying;
import com.vectails.xml.data.Legs;
import com.vectails.xml.data.Quote;
import com.vectails.xml.data.Quotes;
import com.vectails.xml.data.Underlying;
import com.vectails.xml.data.Underlyings;
import com.vectails.xml.data.UtsDirectAccessResponse;
import com.vectails.xml.data.tag.ParameterTag;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EStatus;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IMarket;
import com.celera.core.dm.Instrument;

public class UtsDirectAccessMessageProcessor
{
	final static Logger logger = LoggerFactory.getLogger(UtsDirectAccessMessageProcessor.class);
	
	private static IOnUpdateNode nodeUpdCb;
	
	public static void setCallback(IOnUpdateNode cb) {
		nodeUpdCb = cb;
	}
	
	public static IXmlNode parseXml(String resp)
	{
		IXmlNode o = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;

			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(resp)));

			dom.getDocumentElement().normalize();

			Element docEle = dom.getDocumentElement();

			o = (IXmlNode) new UtsDirectAccessResponse();
			o.parseNode(docEle);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}

	public static void dispatch(IXmlNode o)
	{
		// List<IXmlNode> DerivativeTypes = new ArrayList<IXmlNode>();
		// List<IXmlNode> Currencies = new ArrayList<IXmlNode>();
		// List<IXmlNode> Underlyings = new ArrayList<IXmlNode>();
		// List<IXmlNode> IndexFutures = new ArrayList<IXmlNode>();
		// List<IXmlNode> AllowedQuoteCreators = new ArrayList<IXmlNode>();
		// List<IXmlNode> Addressees = new ArrayList<IXmlNode>();
		// List<IXmlNode> Quotes = new ArrayList<IXmlNode>();
		IInstrument instr = null;
		
		if (o instanceof UtsDirectAccessResponse)
		{
			UtsDirectAccessResponse resp = (UtsDirectAccessResponse) o;

			// List currencies = resp.getCurrencies();
//			List derivTypes = resp.getDerivativeTypes();
			List<IXmlNode> underlyingsList = resp.getUnderlyings();
			for (IXmlNode node : underlyingsList)
			{
				dispatch(node);
			}
			List<IXmlNode> derivList = resp.getDerivativeTypes();
			for (IXmlNode node : derivList)
			{
				dispatch(node);
			}
			List<IXmlNode> addrList = resp.getAddressees();
			for (IXmlNode node : addrList)
			{
				dispatch(node);
			}
			List<IXmlNode> idxFutList = resp.getIndexFutures();
			for (IXmlNode node : idxFutList)
			{
				dispatch(node);
			}
			List<IXmlNode> quotesList = resp.getQuotes();
			for (IXmlNode node : quotesList)
			{
				dispatch(node);
			}
		}
		else if (o instanceof Underlyings) 
		{
			List<IXmlNode> underlyingList = ((Underlyings)o).getUnderlying();
			for (IXmlNode node : underlyingList)
			{
				dispatch(node);
			}
		}
		else if (o instanceof IndexFutures) 
		{
			List<IXmlNode> list = ((IndexFutures)o).getIndexFuture();
			for (IXmlNode node : list)
			{
				dispatch(node);
			}
		}
		else if (o instanceof DerivativeTypes) 
		{
			List<IXmlNode> list = ((DerivativeTypes)o).getDerivativeType();
			for (IXmlNode node : list)
			{
				dispatch(node);
			}
		}
		else if (o instanceof Quotes) 
		{
			List<IXmlNode> list = ((Quotes)o).getQuote();
			for (IXmlNode node : list)
			{
				dispatch(node);
			}
		}
		else if (o instanceof Underlying) 
		{
			instr = convertInstrument((Underlying)o);
		}
		else if (o instanceof IndexFuture) 
		{
			instr = convertInstrument((IndexFuture)o);
		}
		else if (o instanceof DerivativeType) 
		{
			instr = convertInstrument((DerivativeType)o);
			List<IXmlNode> l1 = ((DerivativeType)o).getLegs();
			for (IXmlNode o1 : l1)
			{
				if (o1 instanceof Legs) 
				{
					List<IXmlNode> l2 = ((Legs)o1).getLeg();
					for (IXmlNode o2 : l2)
					{
						if (o2 instanceof Leg) 
						{
							IInstrument leg;
							leg = convertInstrument((Leg)o2);
							((Derivative)instr).addLeg(leg.getName(), leg);	
						}
					}
				}
			}
		}
		else if (o instanceof Quote) {
			instr = convertInstrument((Quote) o); 	
		}
		
		if (instr != null) {
			EInstrumentType type = instr.getType();
			StaticDataManager.add(type.toString(), instr);
			nodeUpdCb.onUpdateNode((INodeUpdateListener)o);
		}
	}

	public static IInstrument convertInstrument(IndexFuture u)
	{
		try
		{
			String code = u.getIndexCode();
			String expiry = u.getExpiryDate();
			String typeCode = u.getIndexUnderlyingTypeCode();
//			EStatus sts = EStatus.ACTIVE;
			String lastUpdTime = u.getLastUpdateDateTime();
			LocalDate lastUpdate = LocalDate.parse(lastUpdTime, ICommonFields.DT_FORMATTER);
			EInstrumentType type = toInstrumentType(typeCode);

			return new Derivative(IMarket.HK, code, type, null, null, null, null, lastUpdate, null, expiry, null, null);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static IInstrument convertInstrument(Underlying u)
	{
		try
		{
			String code = u.getCode();
			String typeCode = u.getUnderlyingTypeCode();
			String name = u.getName();
			String ric = u.getReuters();
			String bbg = u.getBloomberg();
			String isin = u.getISIN();
			String isDead = u.getIsObsolete();
//			EStatus sts = EStatus.ACTIVE;
//			if ("True".equals(isDead))
//				sts = EStatus.OBSOLETED;
			String lastUpdTime = u.getLastUpdateDateTime();
			LocalDate lastUpdate = LocalDate.parse(lastUpdTime, ICommonFields.DT_FORMATTER);
			EInstrumentType type = toInstrumentType(typeCode);

			return new Instrument(IMarket.HK, code, type, name, isin, bbg, ric, lastUpdate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static IInstrument convertInstrument(DerivativeType u)
	{
		try
		{
			String code = u.getCode();
			String name = u.getName();
			String sIsPriceInPercent = u.getIsPriceInPercent();
			boolean isPriceInPercent = Boolean.valueOf(sIsPriceInPercent);
			// String legCnt = u.getLegCount();
			// String isBasic = u.getIsBasic();
			// EStatus sts = EStatus.ACTIVE;
			String lastUpdTime = u.getLastUpdateDateTime();

			String paramStr = u.getParameterString();
			StaticDataManager.add(code, paramStr);
			
			LocalDate lastUpdate = LocalDate.parse(lastUpdTime, ICommonFields.DT_FORMATTER);
			EInstrumentType type = toInstrumentType(code);

			return new Derivative(IMarket.HK, code, type, name, null, null, null, lastUpdate, null, null, null, isPriceInPercent);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static IInstrument convertInstrument(Leg u) 
	{
		try
		{
			ParameterTag tag = u.getDerivativeType();
			String code = tag.getValue();
			String name = u.getLabel(); 		// Derivative.Legs.Leg.Label <-> Quote.Legs.Leg.Name
			tag = u.getMultiplier();
			String sMultiplier = tag.getValue();
			Double multiplier = null;
			try
			{
				multiplier = Double.valueOf(sMultiplier);
			} catch (Exception e)
			{
			}
			EInstrumentType type = toInstrumentType(code);
			return new com.celera.core.dm.Leg(IMarket.HK, code, type, name, null, null, null, null, null, null, null,
					null, multiplier);
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.error(u.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public static IInstrument convertInstrument(Quote u)
	{
		try
		{
			String derivType = u.getProductDerivativeTypeCode();
			Derivative instr = (Derivative) StaticDataManager.get(derivType);

			List<IXmlNode> legsList = u.getLegs();
			for (IXmlNode e : legsList)
			{
				try
				{
					List<IXmlNode> legList = ((Legs) e).getLeg();
					for (IXmlNode e1 : legList)
					{
						Leg leg = (Leg) e1;
						String name = leg.getName();
						com.celera.core.dm.Leg oldLeg = instr.getLeg(name); // Derivative.Legs.Leg.Label
																			// <->
																			// Quote.Legs.Leg.Name

						for (IXmlNode e2 : leg.getLegUnderlying())
						{
							for (IXmlNode e3 : ((LegUnderlying) e2).getLegSingleUnderlying())
							{
								String spot = ((LegSingleUnderlying) e3).getSpot();
								oldLeg.setPrice(Double.valueOf(spot));
							}
						}
						for (IXmlNode e2 : leg.getLegDerivative()) // LegDerivative
						{
							for (IXmlNode e3 : ((LegDerivative) e2).getLegDerivativeItems()) // LegDerivativeItems
							{
								for (IXmlNode e4 : ((LegDerivativeItems) e3).getLegDerivativeItem()) // LegDerivativeItem
								{
									try
									{
										String itemName = ((LegDerivativeItem) e4).getName();
										String value = ((LegDerivativeItem) e4).getValue();

										Field f = Leg.class.getDeclaredField(itemName);
										f.setAccessible(true);
										LegMapping annotation = f.getDeclaredAnnotation(LegMapping.class);

										// Annotation[] annotations =
										// f.getDeclaredAnnotations();
										String[] values = annotation.value();
										String mtd1 = values[0];
										String type = values[1];
										String mtd2 = values[2];
										Class clz = Class.forName(type);

										Object o = value;
										if (!(mtd2 == null || mtd2.length() == 0)) 
										{
											Method valueOf = clz.getMethod(mtd2, String.class);
											o = valueOf.invoke(oldLeg, value);
										}
										Method setter = oldLeg.getClass().getMethod(mtd1, clz);
										setter.setAccessible(true);
										setter.invoke(oldLeg, o);
									} 
									catch (Exception ex)
									{
										logger.error(ex.getMessage());
										ex.printStackTrace();
									}
								}
							}
						}
						// oldLeg.setPrice(Double.valueOf(leg.getSize()));
					}
				} catch (Exception ex)
				{
					logger.error(ex.getMessage());
					ex.printStackTrace();
				}
			}

			String ulyCode = u.getProductUnderlyingCode();

			// String code = tag.getValue();
			// String name = u.getLabel();
			// tag = u.getMultiplier();
			// String sMultiplier = tag.getValue();
			// Double multiplier = null;
			// try
			// {
			// multiplier = Double.valueOf(sMultiplier);
			// } catch (Exception e)
			// {
			// }
			// EInstrumentType type = toInstrumentType(code);
			// return new com.celera.core.dm.Leg(IMarket.HK, code, type, name,
			// null, null, null, null, null, null, null,
			// null, multiplier);
		} catch (Exception ex)
		{
			logger.error(ex.getMessage());
			 ex.printStackTrace();
		}
		return null;
	}

	public static EInstrumentType toInstrumentType(String code)
	{
		switch (code)
		{
		case "S":
			return EInstrumentType.STOCK;
		case "I":
			return EInstrumentType.INDEX;
		}
		String newCode = code.replace("_", "").replace("%", "_PERCENT");
		EInstrumentType type = EInstrumentType.OPTION;
		try
		{
			type = EInstrumentType.valueOf(newCode);
		} catch (Exception e)
		{
			logger.error("type=[{}], {}", code, e.getMessage());
		}
		return type;
	}
}
