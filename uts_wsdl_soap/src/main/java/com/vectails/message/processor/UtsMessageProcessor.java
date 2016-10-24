package com.vectails.message.processor;

import java.io.StringReader;
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

import com.vectails.oms.OMS;
import com.vectails.sds.UtsStaticDataService;
import com.vectails.session.IUtsLastTimeUpdateListener;
import com.vectails.xml.IUtsLastTimeUpdater;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.Addressee;
import com.vectails.xml.data.Addressees;
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
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IMarket;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.IQuote;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.Instrument;
import com.celera.core.dm.Trade;
import com.uts.tools.Uts2Dm;

public class UtsMessageProcessor
{
	final static Logger logger = LoggerFactory.getLogger(UtsMessageProcessor.class);

	private static UtsMessageProcessor _instance;
	private IUtsLastTimeUpdateListener utsSessListener;
	
	protected UtsMessageProcessor()
	{
	}
	
	public void setUtsSessionListener(IUtsLastTimeUpdateListener cb)
	{
		utsSessListener = cb;
	}

	public static UtsMessageProcessor instance()
	{
		if (_instance == null)
		{
			synchronized (UtsMessageProcessor.class) 
			{
				if (_instance == null)	
					_instance = new UtsMessageProcessor();
			}
		}
		return _instance;
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * @param o
	 * description: dispatch parsed node object into OMS and process 
	 */
	public void dispatch(IXmlNode o)
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
			// List derivTypes = resp.getDerivativeTypes();
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
			if (resp.getTimeOfLastRecoveredQuotes() != null)	// login Quote messages (skip) 
			{
				List<IXmlNode> quotesList = resp.getQuotes();
				for (IXmlNode node : quotesList)
				{
					dispatch(node);
				}
			}
		}
		else if (o instanceof Underlyings)
		{
			List<IXmlNode> underlyingList = ((Underlyings) o).getUnderlying();
			for (IXmlNode node : underlyingList)
			{
				dispatch(node);
			}
		}
		else if (o instanceof IndexFutures)
		{
			List<IXmlNode> list = ((IndexFutures) o).getIndexFuture();
			for (IXmlNode node : list)
			{
				dispatch(node);
			}
		}
		else if (o instanceof DerivativeTypes)
		{
			List<IXmlNode> list = ((DerivativeTypes) o).getDerivativeType();
			for (IXmlNode node : list)
			{
				dispatch(node);
			}
		}
		else if (o instanceof Quotes)
		{
			List<IXmlNode> list = ((Quotes) o).getQuote();
			for (IXmlNode node : list)
			{
				dispatch(node);
			}
		}
		else if (o instanceof Underlying)
		{
			instr = onInstrument((Underlying) o);
		}
		else if (o instanceof IndexFuture)
		{
			instr = onInstrument((IndexFuture) o);
		}
		else if (o instanceof DerivativeType)
		{
			instr = onInstrument((DerivativeType) o);
			List<IXmlNode> l1 = ((DerivativeType) o).getLegs();
			for (IXmlNode o1 : l1)
			{
				if (o1 instanceof Legs)
				{
					List<IXmlNode> l2 = ((Legs) o1).getLeg();
					for (IXmlNode o2 : l2)
					{
						if (o2 instanceof Leg)
						{
							IInstrument leg;
							leg = onInstrument((Leg) o2);
							((Derivative) instr).addLeg(leg.getName(), leg);
						}
					}
				}
			}
		}
		else if (o instanceof Quote)
		{
			doQuote((Quote) o);
		}
		
		if (instr != null)
		{
//			EInstrumentType type = instr.getType();
			UtsStaticDataService.instance().onInstrumentUpdate(instr);
		}
		
		if (o instanceof IUtsLastTimeUpdater)
		{
			// update UTS session
			((IUtsLastTimeUpdater)o).updateLastTime(utsSessListener);
		}
	}

	public IInstrument onInstrument(IndexFuture u)
	{
		try
		{
			String code = u.getIndexCode();
			String expiry = u.getExpiryDate();
			String typeCode = u.getIndexUnderlyingTypeCode();
			// EStatus sts = EStatus.ACTIVE;
			String lastUpdTime = u.getLastUpdateDateTime();
			LocalDate lastUpdate = Uts2Dm.toLocalDate(lastUpdTime);
			EInstrumentType type = Uts2Dm.toInstrumentType(typeCode);

			return new Derivative(IMarket.HK, code, type, null, null, null, null, lastUpdate, null, expiry, null, null);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IInstrument onInstrument(Underlying u)
	{
		try
		{
			String code = u.getCode();
			String typeCode = u.getUnderlyingTypeCode();
			String name = u.getName();
			String ric = u.getReuters();
			String bbg = u.getBloomberg();
			String isin = u.getISIN();
			// String isDead = u.getIsObsolete();
			String lastUpdTime = u.getLastUpdateDateTime();
			LocalDate lastUpdate = Uts2Dm.toLocalDate(lastUpdTime);
			EInstrumentType type = Uts2Dm.toInstrumentType(typeCode);

			return new Instrument(IMarket.HK, code, type, name, isin, bbg, ric, lastUpdate);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IInstrument onInstrument(DerivativeType u)
	{
		try
		{
			String code = u.getCode();
			String name = u.getName();
			String sIsPriceInPercent = u.getIsPriceInPercent();
			Boolean isPriceInPercent = Boolean.valueOf(sIsPriceInPercent);
			String lastUpdTime = u.getLastUpdateDateTime();

//			String paramStr = u.getParameterString();
//			UtsStaticDataService.add(code, paramStr);

			LocalDate lastUpdate = Uts2Dm.toLocalDate(lastUpdTime);
			EInstrumentType type = Uts2Dm.toInstrumentType(code);

			return new Derivative(IMarket.HK, code, type, name, null, null, null, lastUpdate, null, null, null,
					isPriceInPercent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IInstrument onInstrument(Leg u)
	{
		try
		{
			ParameterTag tag = u.getDerivativeType();
			String code = tag.getValue();
			String name = u.getLabel(); // Derivative.Legs.Leg.Label <->
										// Quote.Legs.Leg.Name
			tag = u.getMultiplier();
			String sMultiplier = tag.getValue();
			Double multiplier = null;
			try
			{
				multiplier = Double.valueOf(sMultiplier);
			}
			catch (Exception e)
			{
			}
			EInstrumentType type = Uts2Dm.toInstrumentType(code);
			return new com.celera.core.dm.Leg(IMarket.HK, code, type, name, null, null, null, null, null, null, null,
					null, multiplier);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.error(u.toString());
			e.printStackTrace();
		}
		return null;
	}

	public void doTrade(String id , String price, String size, String time, String comment)
	{
		if (id == null || price == null || size == null || time == null)
			return;

		if (Uts2Dm.DB_NULL.equals(id) || Uts2Dm.DB_NULL.equals(price) || Uts2Dm.DB_NULL.equals(size)
				|| Uts2Dm.DB_NULL.equals(time))
			return;
		
		Trade trade = new Trade();
		trade.setOrderId(Uts2Dm.toLong(id));
		trade.setPrice(Uts2Dm.toDouble(price));
		trade.setQty(Uts2Dm.toLong(size));
		trade.setTime(Uts2Dm.toLocalDate(time));
		trade.setComment(comment);

		OMS.instance().onTrade(trade);
	}

	public void doQuote(Quote u)
	{
		Derivative instr = null;

		Long id = Long.valueOf(u.getQuoteId());
		com.celera.core.dm.Quote quote = (com.celera.core.dm.Quote) OMS.instance().get(id);
		if (quote == null)
		{
			quote = new com.celera.core.dm.Quote();
			quote.setId(id);

			// set instrument and legs
			String sDerivTypeCode = u.getProductDerivativeTypeCode();
			if (sDerivTypeCode == null)
			{
				logger.error("Derivate Type not found {}", sDerivTypeCode);
				return;
			}
			instr = (Derivative) UtsStaticDataService.instance().get(sDerivTypeCode);
			if (instr == null)
			{
				logger.error("Instrument not found {}", sDerivTypeCode);
				return;
			}
			quote.setInstr(instr);

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
								oldLeg.setPrice(Uts2Dm.toDouble(spot));
							}
						}
						for (IXmlNode e2 : leg.getLegDerivative()) // LegDerivative
						{
							for (IXmlNode e3 : ((LegDerivative) e2).getLegDerivativeItems()) // LegDerivativeItems
							{
								for (IXmlNode e4 : ((LegDerivativeItems) e3).getLegDerivativeItem()) // LegDerivativeItem
								{
									String itemName = null, value = null;
									try
									{
										itemName = ((LegDerivativeItem) e4).getName();
										value = ((LegDerivativeItem) e4).getValue();

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
										logger.error("field[{}], annotation[{}]", itemName, value, ex);
									}
								}
							}
						}
						// oldLeg.setPrice(Double.valueOf(leg.getSize()));
					}
				}
				catch (Exception ex)
				{
					logger.error(ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		
		// addressees
		List<com.celera.core.dm.Addressee> list = new ArrayList<com.celera.core.dm.Addressee>();
		try
		{
			for (IXmlNode o1: u.getAddressees())
			{
				if (o1 instanceof Addressees)
				{
					for (IXmlNode o2: ((Addressees)o1).getAddressee())
					{
						Addressee o3 = (Addressee)o2;
						
						com.celera.core.dm.Addressee addr = new com.celera.core.dm.Addressee();
						addr.setEntityCode(o3.getEntityCode());
						addr.setAddrCode(o3.getAddresseeCode());
						addr.setIsBroadcast(Uts2Dm.toBoolean(o3.getBroadcast()));
						list.add(addr);
					}	
				}
			}
		}
		catch (Exception e)
		{
			logger.error("parse Quote addressee error", e);
		}
		
		// modifiable fields
		quote.setStatus(Uts2Dm.toStatus(u.getMode()));
		quote.setEntity(u.getQuoteCreatorEntityCode());
		quote.setAskPrice(Uts2Dm.toDouble(u.getAskPrice()));
		quote.setAskQty(Uts2Dm.toLong(u.getAskSize()));
		quote.setAskTime(Uts2Dm.toLocalDate(u.getAskTime()));
		quote.setBidPrice(Uts2Dm.toDouble(u.getBidPrice()));
		quote.setBidQty(Uts2Dm.toLong(u.getBidSize()));
		quote.setBidTime(Uts2Dm.toLocalDate(u.getBidTime()));
		quote.setAddressees(list);
		
		OMS.instance().onQuote(quote);
		
		doTrade(u.getQuoteId(), u.getTradedPrice(), u.getTradedSize(), u.getTradedTime(), u.getTradedComment());
	}
}
