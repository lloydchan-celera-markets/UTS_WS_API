package com.celera.backoffice;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.mongo.entity.Account;
import com.celera.mongo.entity.BOData;
import com.celera.mongo.entity.Hedge;
import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.Leg;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.entity.TradeDetail;
import com.celera.mongo.entity.TradeDetails;
import com.uts.tools.Uts2Dm;
import com.celera.mongo.entity.InvoiceRegister;
import com.uts.tradeconfo.UtsTradeConfoDetail;
import com.uts.tradeconfo.UtsTradeConfoSummary;

public class DbCreateInvoice implements Runnable
{
	private static final Logger logger = LoggerFactory.getLogger(DbCreateInvoice.class);
	
    private static final NumberFormat nf = new DecimalFormat("##.#");
    private static final String PREFIX_BUYER = "Buyer - ";
    private static final String PREFIX_SELLER = "Seller - ";
	private static final String PRE_INVOICE_NUMBER = "CEL-";
	private static final String PATTERN = " - ";
	private static final NumberFormat format = NumberFormat.getInstance();
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	private static final SimpleDateFormat sdf_mmm_yy = new SimpleDateFormat("MMMyy");
	private static final SimpleDateFormat sdf_mmmm_yyyyyy = new SimpleDateFormat("MMMM yyyy");
	private static final SimpleDateFormat sdf_dd_MMMM_yy = new SimpleDateFormat("dd MMMM, yyyy");
	private static final SimpleDateFormat sdf_mm_yy = new SimpleDateFormat("MMyy");
	
	private final String todayStr = sdf.format(new Date());
	
//	private static Long invoice_Num = 0l;

	private Date invMonth;
	private Date tradeDateStart;
	private Date tradeDateEnd;

	private String currency;
	private String company;
	
	public static Long getNextInvNum() 
	{
		String invNumber = DatabaseAdapter.getMaxInvoiceNumber();
		logger.info("Max invoice number {} ", invNumber);
		Long invoice_Num = Uts2Dm.toLong(invNumber.replaceAll(PRE_INVOICE_NUMBER, ""));
		return invoice_Num + 1;
	}

//	public static String key(String participant, String currency, Date d)
//	{
//		String[] token = participant.split(" - ");
//		return new String(token[0] + "_" + currency + "_" + sdf_mmm_yy.format(d)).toUpperCase();
//	}
	
	private String key() 
	{
		// similar company return the same key
		return Invoice.key(this.company, this.currency, this.invMonth);
	}
	
//	public DbInvoiceGenerator(Date start, Date end)
//	{
//		this.start = start;
//		this.end = end;
//	}

	public DbCreateInvoice(){}
//	public DbCreateInvoice(String key)
//	{
//		String[] token = key.split("_");
//		this.company = token[0];
//		this.currency = token[1];
//		
//		String MMyy = token[2];
//		Date dMonth;
//		try
//		{
//			dMonth = sdf_mm_yy.parse(MMyy);
//			Calendar start = Calendar.getInstance();
//			start.setTime(dMonth);
//			start.set(Calendar.DAY_OF_MONTH, 1);
//			start.set(Calendar.HOUR_OF_DAY, 0);
//			start.set(Calendar.MINUTE, 0);
//			start.set(Calendar.SECOND, 0);
//			start.set(Calendar.MILLISECOND, 0);
//			
//			int lastDay = start.getActualMaximum(Calendar.DAY_OF_MONTH);
//			Calendar lastDayCal = Calendar.getInstance();
//			lastDayCal.setTime(start.getTime());
//			lastDayCal.set(Calendar.DAY_OF_MONTH, lastDay);
//			lastDayCal.set(Calendar.HOUR_OF_DAY, 23);
//			lastDayCal.set(Calendar.MINUTE, 59);
//			lastDayCal.set(Calendar.SECOND, 59);
//			lastDayCal.set(Calendar.MILLISECOND, 999);
//			
//			this.start = start.getTime();
//			this.end = lastDayCal.getTime();
//		} catch (ParseException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	public void setDate(Date invMonth)
	{
		this.invMonth = invMonth;
		
		Calendar start = Calendar.getInstance();
		start.setTime(invMonth);
		
		start.add(Calendar.MONTH, -1);	// trade date month
		
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);

		int lastDay = start.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar lastDayCal = Calendar.getInstance();
		lastDayCal.setTime(start.getTime());
		lastDayCal.set(Calendar.DAY_OF_MONTH, lastDay);
		lastDayCal.set(Calendar.HOUR_OF_DAY, 23);
		lastDayCal.set(Calendar.MINUTE, 59);
		lastDayCal.set(Calendar.SECOND, 59);
		lastDayCal.set(Calendar.MILLISECOND, 999);

		this.tradeDateStart = start.getTime();
		this.tradeDateEnd = lastDayCal.getTime();
	}
	
	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public void run1()
	{
		Map<String, List<TradeConfo>> client2TradeConfo = new HashMap<String, List<TradeConfo>>();

		// (firm + currency) -> trade confo
		List<TradeConfo> dbList = DatabaseAdapter.getHistTradeConfo(tradeDateStart, tradeDateEnd);
		String key;
		for (TradeConfo e : dbList)
		{
			String curncy = e.getCurncy();
			String buyer = e.getBuyer();
			Date tradeDate = e.getTradeDate();
			
			String firm;
			if (buyer == null)
			{
				String seller = e.getSeller();
				int pos = seller.lastIndexOf(PATTERN);
				firm = (pos > 0) ? seller.substring(0, pos) : seller;
				key = firm + curncy;
			} else
			{
				int pos = buyer.lastIndexOf(PATTERN);
				firm = (pos > 0) ? buyer.substring(0, pos) : buyer;
				key = firm + curncy;
			}
			List temp = (List) client2TradeConfo.get(key);
			if (temp == null)
			{
				temp = new ArrayList<TradeConfo>();
				client2TradeConfo.put(key, temp);
			}
			temp.add(e);
		}

//		String.format("CEL%04d", invoice_Num);
		// for each client
		for (Map.Entry<String, List<TradeConfo>> e : client2TradeConfo.entrySet())
		{
			// find client info
			List<TradeConfo> tradeConfo = e.getValue();
			TradeDetail tradeDetail = new TradeDetail();
			for (TradeConfo tc : e.getValue())
			{
				tradeDetail.setDate(Uts2Dm.toDateString(tc.getTradeDate()));
			}
		}
	}
	
	private Date nextMonth(Date d) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_MONTH, 9);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}
	
	public Invoice createInvoice()
	{
		List<TradeConfo> client2TradeConfo = new ArrayList<TradeConfo>();
		
		// (firm + currency) -> trade confo
		List<TradeConfo> dbList = DatabaseAdapter.getHistTradeConfo(tradeDateStart, tradeDateEnd);
		
		String myKey = this.key();
		String tmpKey = null/*, tdKeyLow = null*/;
		String curncy = null;
		Date tradeDate = null;
		for (TradeConfo e : dbList)
		{
			String tmpCurncy = e.getCurncy();
			String buyer = e.getBuyer();
			String seller = e.getSeller();
			Date tmpTradeDate = e.getTradeDate();
			Date invDate = nextMonth(tmpTradeDate);
			String tmpParticipant = null;
			if (buyer == null && seller == null || tmpTradeDate == null)
			{
				logger.error("{}", e);
				System.exit(-1);
				continue;
			}
			tmpParticipant = buyer == null? seller: buyer;
			tmpKey = Invoice.key(tmpParticipant, tmpCurncy, invDate);
			
			if (myKey.equals(tmpKey)) 
			{ 
				client2TradeConfo.add(e);
				company = tmpParticipant.split(" - ")[0];
				curncy = tmpCurncy;
				tradeDate = tmpTradeDate;
			}
		}
		
		Double totalSize = 0d;
		Double totalFee = 0d;
		Double totalHedge = 0d;
		// find client info
		List<TradeDetail> lTd = new ArrayList<TradeDetail>();
		
		// for each client
		for (TradeConfo tc: client2TradeConfo)
		{
			// update hasSent
			tc.setHasInvoiceCreated(true);
			DatabaseAdapter.update(tc);
			
			TradeDetail tradeDetail = new TradeDetail();
			tradeDetail.setDate(Uts2Dm.toDateString(tc.getTradeDate()));	// same format
			tradeDetail.setTradeId(tc.getTradeConfoId());
			tradeDetail.setDescription(tc.getSummary());
			tradeDetail.setDescription(tc.getSummary());
			tradeDetail.setTradeConfoFile(tc.getSummary());
			tradeDetail.setTradeConfoFile(tc.getFile());
			
			Double size = 0d;
			for (Leg leg: tc.getLegs())
			{
				size += leg.getQty();
			}
			totalSize += size;
			
			Double hedge = 0d;
			for (Hedge h: tc.getHedges())
			{
				hedge += h.getQty();
			}
			totalHedge += hedge;
//				
			tradeDetail.setSize(nf.format(size));
			tradeDetail.setHedge(nf.format(hedge));
			
			String buyer = tc.getBuyer();
			if (buyer == null)
			{
				tradeDetail.setReference(PREFIX_SELLER + tc.getSeller());
			} else
			{
				tradeDetail.setReference(PREFIX_BUYER + buyer);
			}
			try {
				totalFee += tc.getBrokerageFee();
				tradeDetail.setFee(BOFormatter.displayFee(tc.getBrokerageFee(), tc.getCurncy()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			lTd.add(tradeDetail);
			
			logger.info("save database {}", tradeDetail);	
		}
			
//		String[] tokens = tdKeyLow.split("_");
//		String company = tokens[0];
//		String curncy = tokens[1];
//		String mmyy = tokens[2];
		Invoice inv = null;
		
		try {
			String inv_mmyy = sdf_mm_yy.format(this.invMonth);
			
			Account account = BOData.get(company);
			if (account == null) {
				logger.error("company not found: {}", company);
			}

			inv = new Invoice();
			inv.setCompany(company);
			inv.setSentTo(account.getEmails());
			inv.setAddress(account.getAddress());
			inv.setAttn(account.getAttn());
			inv.setInvoice_number("");
			inv.setInvoice_date(todayStr);
			inv.setAccount_number(account.getId());
			inv.setAmount_due(BOFormatter.displayFee(totalFee, curncy));
			inv.setDescription("October 2016 Brokerage Fee");
			inv.setAmount(BOFormatter.displayFee(totalFee, curncy));
			inv.setCurrency(curncy);
			updateInvoiceDate(this.tradeDateStart, inv);
			
//				inv.setTradeDetails(td);
			inv.setSize(BOFormatter.displayNumber(totalSize));
			inv.setHedge(BOFormatter.displayNumber(totalHedge));
			inv.setTotal_fee(BOFormatter.displayFee(totalFee, curncy));
			inv.setTradeDetail(lTd);
			
			String invNumber = createInvNumber(company, curncy, tradeDate, totalFee, inv);
			if (invNumber != null)
			{
				inv.setInvoice_number(invNumber);
			}
			inv.setKey(Invoice.key(company, curncy, this.invMonth));
			
			try {
				InvoiceTemplate.dbWordDocProcessor(inv, curncy, tradeDate);
				
				DatabaseAdapter.create(inv);
//				logger.info("save database {}", inv);	
			}
			catch (Exception ex) {
				logger.error("", ex);
			}
			finally {
			}
		}
		catch (Exception ex) 
		{
			logger.error("", ex);
			System.exit(-1);
		}
		
		return inv;
	}
	
	public void run()
	{
//		UtsTradeConfoSummary.load();
		
		Map<String, List<TradeConfo>> client2TradeConfo = new HashMap<String, List<TradeConfo>>();

		// (firm + currency) -> trade confo
//		List<UtsTradeConfoDetail> dbList = this.pdfParser._list;
		List<TradeConfo> dbList = DatabaseAdapter.getHistTradeConfo(tradeDateStart, tradeDateEnd);
		
		String tdKey = null, tdKeyLow = null;
		for (TradeConfo e : dbList)
		{
			String curncy = e.getCurncy();
			String buyer = e.getBuyer();
			String seller = e.getSeller();
			Date tradeDate = e.getTradeDate();

			if (buyer == null && seller == null || tradeDate == null)
			{
				continue;
			}
			else if (buyer == null)
			{
				tdKey = Invoice.key(seller, curncy, tradeDate);
//				tdKeyLow = Invoice.keyLowerCase(seller, curncy, tradeDate);
				
			} else
			{
				tdKey = Invoice.key(buyer, curncy, tradeDate);
//				tdKeyLow = Invoice.keyLowerCase(buyer, curncy, tradeDate);
			}
			
			if (this.key().equals(tdKey)) 
			{ 
				List temp = (List) client2TradeConfo.get(tdKeyLow);
				if (temp == null)
				{
					temp = new ArrayList<TradeConfo>();
					client2TradeConfo.put(tdKeyLow, temp);
				}
				temp.add(e);
				
				// update hasSent
				e.setHasInvoiceCreated(true);
				DatabaseAdapter.create(e);
			}
//			else {
//				System.out.println("================skip key================" + key);
//			}
		}

//		String.format("CEL%04d", invoice_Num);

		// for each client
		for (Map.Entry<String, List<TradeConfo>> e: client2TradeConfo.entrySet())
		{
			Double totalSize = 0d;
			Double totalFee = 0d;
			Double totalHedge = 0d;
			
			// find client info
			List<TradeDetail> lTd = new ArrayList<TradeDetail>();
			
			for (TradeConfo tc : e.getValue())
			{
				TradeDetail tradeDetail = new TradeDetail();
				tradeDetail.setDate(Uts2Dm.toDateString(tc.getTradeDate()));	// same format
				tradeDetail.setTradeId(tc.getId());
				tradeDetail.setDescription(tc.getSummary());
				Double size = 0d;
				for (Leg leg: tc.getLegs())
				{
					size += leg.getQty();
				}
				totalSize += size;
				
				Double hedge = 0d;
				for (Hedge h: tc.getHedges())
				{
					hedge += h.getQty();
				}
				totalHedge += hedge;
//				
				tradeDetail.setSize(nf.format(size));
				tradeDetail.setHedge(nf.format(hedge));
				
				String buyer = tc.getBuyer();
				if (buyer == null)
				{
					tradeDetail.setReference(PREFIX_SELLER + tc.getSeller());
				} else
				{
					tradeDetail.setReference(PREFIX_BUYER + buyer);
				}
				try {
//if (tc.getBrokerageFee() == null)
//	System.out.println("borager null =" + tc.getBrokerageFee());
					totalFee += tc.getBrokerageFee();
					tradeDetail.setFee(BOFormatter.displayFee(tc.getBrokerageFee(), tc.getCurncy()));
				} catch (Exception e1) {
//System.out.println("Exception borager=" + tc.getBrokerageFee() + "," + tc.getCurncy());
					e1.printStackTrace();
				}
				lTd.add(tradeDetail);
				
//DatabaseAdapter.save(tradeDetail);
logger.info("save database {}", tradeDetail);	
			}

			String[] tokens = tdKey.split("_");
			String company = tokens[0];
			String curncy = tokens[1];
			String mmmyy = tokens[2];
			
//			TradeDetails td = new TradeDetails();
//			td.setTradeDetail(lTd);
//			td.setSize(BOFormatter.displayNumber(totalSize));
//			td.setHedge(BOFormatter.displayNumber(totalHedge));
//			td.setTotal_fee(BOFormatter.displayFee(totalFee, curncy));
			
//DatabaseAdapter.save(td);
//logger.info("save database {}", td);			
			Account account = BOData.get(company.toUpperCase());
			if (account == null) {
				logger.error("company not found: {}", company);

				continue;
			}
			
			try {
				Invoice inv = new Invoice();
				inv.setCompany(company);
				inv.setSentTo(account.getEmails());
				inv.setAddress(account.getAddress());
				inv.setAttn(account.getAttn());
				inv.setInvoice_number("");
				inv.setInvoice_date(todayStr);
				
				inv.setAccount_number(account.getId());
//				inv.setDue_date("28 November, 2016");
				inv.setAmount_due(BOFormatter.displayFee(totalFee, curncy));
				inv.setDescription("October 2016 Brokerage Fee");
				inv.setAmount(BOFormatter.displayFee(totalFee, curncy));
				inv.setCurrency(curncy);
				Date invDate = sdf_mmm_yy.parse(mmmyy);
				setInvoiceDate1(invDate, inv);

//				inv.setTradeDetails(td);
				inv.setSize(BOFormatter.displayNumber(totalSize));
				inv.setHedge(BOFormatter.displayNumber(totalHedge));
				inv.setTotal_fee(BOFormatter.displayFee(totalFee, curncy));
				inv.setTradeDetail(lTd);
				
String invNumber = checkInvNumber(tdKey, totalFee, inv);
if (invNumber != null)
{
	inv.setInvoice_number(invNumber);
}
				
				try {
					InvoiceTemplate.wordDocProcessor(inv, curncy, invDate);
//DatabaseAdapter.save(inv);
logger.info("save database {}", inv);	
				}
				catch (Exception ex) {
					logger.error("", ex);
				}
				finally {
//					System.out.println("other exception");
				}
			}
			catch (Exception ex) 
			{
				logger.error("", e);
			}
		}
	}
	
	private void updateInvoiceDate(Date tradeDate, Invoice inv)
	{
//		try
//		{
//			Date d = sdf_mm_yy.parse(mmyy);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(tradeDate);
			cal.set(Calendar.DAY_OF_MONTH, 9);
			cal.add(Calendar.MONTH, 1);
			String invdate = sdf_dd_MMMM_yy.format(cal.getTime());
			
			cal.add(Calendar.MONTH, 1);
			String invduedate = sdf_dd_MMMM_yy.format(cal.getTime());
			inv.setInvoice_date(invdate);
			inv.setDue_date(invduedate);
//		} catch (ParseException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void setInvoiceDate1(Date d, Invoice inv)
	{
//		try
//		{
//			Date d = sdf_mmm_yy.parse(mmmyy);

			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_MONTH, 9);
			cal.add(Calendar.MONTH, 1);
			String invdate = sdf_dd_MMMM_yy.format(cal.getTime());

			cal.add(Calendar.MONTH, 1);
			String invduedate = sdf_dd_MMMM_yy.format(cal.getTime());
			inv.setInvoice_date(invdate);
			inv.setDue_date(invduedate);
//		} catch (ParseException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static String createInvNumber(String company, String currency, Date tradeDate, Double totalFee, Invoice inv) 
	{
		String tradeDate_mmyy = sdf_mm_yy.format(tradeDate);
//			String keyUsd = tokens[0] + "_" + tokens[1].replace("KRW", "USD").replace("JPY", "USD") + "_" + fileMonth;
		String keyUsd = company + "_" + currency.replace("KRW", "USD").replace("JPY", "USD") + "_" + tradeDate_mmyy;
		keyUsd = keyUsd.toUpperCase();
		
		InvoiceRegister register = UtsTradeConfoSummary.map.get(keyUsd);
		if (register == null)
		{
			// dont fxxking care recon
			logger.error("Invoice register not found in UTS {}", keyUsd);
		}
		else {
			if (register.getAmount().equals(totalFee)){
				logger.info("=============same amount============== {}, {}, {}", company, register.getAmount(), totalFee);
				String invNum = padInvNum(getNextInvNum());
//				return (PRE_INVOICE_NUMBER + getInvoiceNumber());
				return invNum;
			}
			else {
				if (register.getAmount().equals(totalFee/2))
				{
					logger.error("=============double amount==============" + company + "," + register.getAmount() +"," + totalFee );
					inv.showDetailsList();
				}
				else {
					logger.error("=============incorrect amount============== {}, {}, {}", keyUsd, register.getAmount(), totalFee );
					inv.showDetailsList();
				}
			}
		}
		return null;
	}
	
//	public static String createInvNumber(String company, String currency, String inv_mmyy, Double totalFee, Invoice inv) {
//			
////			String keyUsd = tokens[0] + "_" + tokens[1].replace("KRW", "USD").replace("JPY", "USD") + "_" + fileMonth;
//		String keyUsd = company + "_" + currency.replace("KRW", "USD").replace("JPY", "USD") + "_" + inv_mmyy;
//		keyUsd = keyUsd.toUpperCase();
//
//		InvoiceRegister register = UtsTradeConfoSummary.map.get(keyUsd);
//		if (register == null)
//		{
//			// dont fxxking care recon
//			logger.error("Invoice register not found in UTS {}", keyUsd);
//		}
//		else {
//			if (register.getAmount().equals(totalFee)){
//				logger.info("=============same amount============== {}, {}, {}", company, register.getAmount(), totalFee);
//				String invNum = padInvNum(getNextInvNum());
////				return (PRE_INVOICE_NUMBER + getInvoiceNumber());
//				return invNum;
//			}
//			else {
//				if (register.getAmount().equals(totalFee/2))
//				{
//					logger.error("=============double amount==============" + company + "," + register.getAmount() +"," + totalFee );
//					inv.showDetailsList();
//				}
//				else {
//					logger.error("=============incorrect amount============== {}, {}, {}", keyUsd, register.getAmount(), totalFee );
//					inv.showDetailsList();
//				}
//			}
//		}
//		return null;
//	}
	
	public static String padInvNum(Long invNum) {
		return String.format("CEL-%04d", invNum);
	}
	
	public static String checkInvNumber(String key, Double totalFee, Invoice inv) {
		String[] tokens = key.split("_");
		String mmmyy = tokens[2];
		Date d;
		try {
			d = sdf_mmm_yy.parse(mmmyy);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_MONTH, 9);
			cal.add(Calendar.MONTH, 1);
			String fileMonth = sdf_mmm_yy.format(cal.getTime());

			String keyUsd = tokens[0] + "_" + tokens[1].replace("KRW", "USD").replace("JPY", "USD") + "_" + fileMonth;
			keyUsd = keyUsd.toUpperCase();
//tempRecon.add(keyUsd);			
//			InvoiceRegister register = CSVReader.map.get(keyUsd);
			InvoiceRegister register = UtsTradeConfoSummary.map.get(keyUsd);
			if (register == null)
			{
				// dont fxxking care recon
logger.error("Invoice not found in UTS {}" + keyUsd);
			}
			else {
				if (register.getAmount().equals(totalFee)){
logger.info("=============same amount============== {}, {}, {}", tokens[0], register.getAmount(), totalFee);
					return register.getInvoice();
//System.out.println("=============amount the same==============" + invNumber);

				}
				else {
					if (register.getAmount().equals(totalFee/2))
					{
logger.error("=============double amount==============" + tokens[0] + "," + register.getAmount() +"," + totalFee );
inv.showDetailsList();
					}
					else {
logger.error("=============incorrect amount============== {}, {}, {}", keyUsd, register.getAmount(), totalFee );
						inv.showDetailsList();
					}
				}
			}
			
		} catch (ParseException e) {
			logger.error("{}", e);
			
		}
		return null;
	}
	
	public static void main(String[] args) 
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date start = new Date(cal.getTimeInMillis());
		Date end = new Date();
//		DbInvoiceGenerator gen = new DbInvoiceGenerator(start, end);
//		gen.run1();
	}
}
