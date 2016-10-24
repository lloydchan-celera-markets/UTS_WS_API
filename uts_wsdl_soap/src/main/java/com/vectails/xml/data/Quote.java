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
public class Quote extends GenericFactory implements IXmlNode
{
	private String Mode = null;
	
	private String ProductFinancialMarketCode = null;
	private String ProductUnderlyingTypeCode = null;
	private String ProductUnderlyingCode = null;
	private String ProductDerivativeTypeCode = null;
	private String ProductParameters = null;
	private String ProductReferenceSpot = null;
	private String ProductReferenceFutureExpiry = null;
	
	private String QuoteCreatorEntityCode = null;
	private String QuoteCreatorCode = null;
	private String QuoteId = null;
	
	private String TradedTime = null;
	private String TradedPrice = null;
	private String TradedSize = null;
	private String TradedComment = null;
	private String InterestLevel = null;
	private String Basis = null;
	private String Delta = null;
	private String InputParameterString = null;
	private String ListedOrOTC = null;
	List<IXmlNode> Legs = new ArrayList<IXmlNode>();
	private String BroadcastComment = null;
	private String PrivateComment = null;
	private String QuoteIsOn = null;
	private String AskTime = null;
	private String AskPrice = null;
	private String AskSize = null;
	private String BidTime = null;
	private String BidPrice = null;
	private String BidSize = null;
	private String QuotationType = null;
	private String PackageId = null;
	private String PackageMultiplier = null;
	private String IsPackageChoice = null;
	List<IXmlNode> Addressees = new ArrayList<IXmlNode>();
	List<IXmlNode> BidFrom = new ArrayList<IXmlNode>();
	List<IXmlNode> AskFrom = new ArrayList<IXmlNode>();
	List<IXmlNode> ProductMultiUnderlying = new ArrayList<IXmlNode>();
	
	public Quote()
	{
		super(Legs.class);
	}
	
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
//					field.setAccessible(true);
//					
//					if (Collection.class.isAssignableFrom(field.getType()))
//					{
////						IXmlNode o = null;
////						switch (nodeName) {
////						case "AllowedQuoteCreators":
////							o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$AllowedQuoteCreators");	
////							break;
////						case "IndexFutures":
////							o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$IndexFutures");	
////							break;
////						case "Underlyings":
////							o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$Underlyings");	
////							break;
////						case "Currencies":
////							o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$Currencies");	
////							break;
////						case "DerivativeTypes":
////							o = (IXmlNode)((IGenericFactory) this).build("com.vectails.data.IXmlParser$DerivativeTypes");	
////							break;
////						}
//						IXmlNode o = (IXmlNode)((IGenericFactory) this).build(IXmlTag.PACKAGE_XML_DATA_PREFIX + nodeName);	
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

	public String getMode()
	{
		return Mode;
	}

	public void setMode(String mode)
	{
		Mode = mode;
	}

	public String getProductFinancialMarketCode()
	{
		return ProductFinancialMarketCode;
	}

	public void setProductFinancialMarketCode(String productFinancialMarketCode)
	{
		ProductFinancialMarketCode = productFinancialMarketCode;
	}

	public String getProductUnderlyingTypeCode()
	{
		return ProductUnderlyingTypeCode;
	}

	public void setProductUnderlyingTypeCode(String productUnderlyingTypeCode)
	{
		ProductUnderlyingTypeCode = productUnderlyingTypeCode;
	}

	public String getProductUnderlyingCode()
	{
		return ProductUnderlyingCode;
	}

	public void setProductUnderlyingCode(String productUnderlyingCode)
	{
		ProductUnderlyingCode = productUnderlyingCode;
	}

	public String getProductDerivativeTypeCode()
	{
		return ProductDerivativeTypeCode;
	}

	public void setProductDerivativeTypeCode(String productDerivativeTypeCode)
	{
		ProductDerivativeTypeCode = productDerivativeTypeCode;
	}

	public String getProductParameters()
	{
		return ProductParameters;
	}

	public void setProductParameters(String productParameters)
	{
		ProductParameters = productParameters;
	}

	public String getProductReferenceSpot()
	{
		return ProductReferenceSpot;
	}

	public void setProductReferenceSpot(String productReferenceSpot)
	{
		ProductReferenceSpot = productReferenceSpot;
	}

	public String getProductReferenceFutureExpiry()
	{
		return ProductReferenceFutureExpiry;
	}

	public void setProductReferenceFutureExpiry(String productReferenceFutureExpiry)
	{
		ProductReferenceFutureExpiry = productReferenceFutureExpiry;
	}

	public String getQuoteCreatorEntityCode()
	{
		return QuoteCreatorEntityCode;
	}

	public void setQuoteCreatorEntityCode(String quoteCreatorEntityCode)
	{
		QuoteCreatorEntityCode = quoteCreatorEntityCode;
	}

	public String getQuoteCreatorCode()
	{
		return QuoteCreatorCode;
	}

	public void setQuoteCreatorCode(String quoteCreatorCode)
	{
		QuoteCreatorCode = quoteCreatorCode;
	}

	public String getQuoteId()
	{
		return QuoteId;
	}

	public void setQuoteId(String quoteId)
	{
		QuoteId = quoteId;
	}

	public String getTradedTime()
	{
		return TradedTime;
	}

	public void setTradedTime(String tradedTime)
	{
		TradedTime = tradedTime;
	}

	public String getTradedPrice()
	{
		return TradedPrice;
	}

	public void setTradedPrice(String tradedPrice)
	{
		TradedPrice = tradedPrice;
	}

	public String getTradedSize()
	{
		return TradedSize;
	}

	public void setTradedSize(String tradedSize)
	{
		TradedSize = tradedSize;
	}

	public String getTradedComment()
	{
		return TradedComment;
	}

	public void setTradedComment(String tradedComment)
	{
		TradedComment = tradedComment;
	}

	public String getInterestLevel()
	{
		return InterestLevel;
	}

	public void setInterestLevel(String interestLevel)
	{
		InterestLevel = interestLevel;
	}

	public String getBasis()
	{
		return Basis;
	}

	public void setBasis(String basis)
	{
		Basis = basis;
	}

	public String getDelta()
	{
		return Delta;
	}

	public void setDelta(String delta)
	{
		Delta = delta;
	}

	public String getInputParameterString()
	{
		return InputParameterString;
	}

	public void setInputParameterString(String inputParameterString)
	{
		InputParameterString = inputParameterString;
	}

	public String getListedOrOTC()
	{
		return ListedOrOTC;
	}

	public void setListedOrOTC(String listedOrOTC)
	{
		ListedOrOTC = listedOrOTC;
	}

	public List<IXmlNode> getLegs()
	{
		return Legs;
	}

	public void setLegs(List<IXmlNode> legs)
	{
		Legs = legs;
	}

	public String getBroadcastComment()
	{
		return BroadcastComment;
	}

	public void setBroadcastComment(String broadcastComment)
	{
		BroadcastComment = broadcastComment;
	}

	public String getPrivateComment()
	{
		return PrivateComment;
	}

	public void setPrivateComment(String privateComment)
	{
		PrivateComment = privateComment;
	}

	public String getQuoteIsOn()
	{
		return QuoteIsOn;
	}

	public void setQuoteIsOn(String quoteIsOn)
	{
		QuoteIsOn = quoteIsOn;
	}

	public String getAskTime()
	{
		return AskTime;
	}

	public void setAskTime(String askTime)
	{
		AskTime = askTime;
	}

	public String getAskPrice()
	{
		return AskPrice;
	}

	public void setAskPrice(String askPrice)
	{
		AskPrice = askPrice;
	}

	public String getAskSize()
	{
		return AskSize;
	}

	public void setAskSize(String askSize)
	{
		AskSize = askSize;
	}

	public String getBidTime()
	{
		return BidTime;
	}

	public void setBidTime(String bidTime)
	{
		BidTime = bidTime;
	}

	public String getBidPrice()
	{
		return BidPrice;
	}

	public void setBidPrice(String bidPrice)
	{
		BidPrice = bidPrice;
	}

	public String getBidSize()
	{
		return BidSize;
	}

	public void setBidSize(String bidSize)
	{
		BidSize = bidSize;
	}

	public String getQuotationType()
	{
		return QuotationType;
	}

	public void setQuotationType(String quotationType)
	{
		QuotationType = quotationType;
	}

	public String getPackageId()
	{
		return PackageId;
	}

	public void setPackageId(String packageId)
	{
		PackageId = packageId;
	}

	public String getPackageMultiplier()
	{
		return PackageMultiplier;
	}

	public void setPackageMultiplier(String packageMultiplier)
	{
		PackageMultiplier = packageMultiplier;
	}

	public String getIsPackageChoice()
	{
		return IsPackageChoice;
	}

	public void setIsPackageChoice(String isPackageChoice)
	{
		IsPackageChoice = isPackageChoice;
	}

	public List<IXmlNode> getAddressees()
	{
		return Addressees;
	}

	public void setAddressees(List<IXmlNode> addressees)
	{
		Addressees = addressees;
	}

	public List<IXmlNode> getBidFrom()
	{
		return BidFrom;
	}

	public void setBidFrom(List<IXmlNode> bidFrom)
	{
		BidFrom = bidFrom;
	}

	public List<IXmlNode> getAskFrom()
	{
		return AskFrom;
	}

	public void setAskFrom(List<IXmlNode> askFrom)
	{
		AskFrom = askFrom;
	}

	public List<IXmlNode> getProductMultiUnderlying()
	{
		return ProductMultiUnderlying;
	}

	public void setProductMultiUnderlying(List<IXmlNode> productMultiUnderlying)
	{
		ProductMultiUnderlying = productMultiUnderlying;
	}
}