package com.celera.backoffice;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.backoffice.BOFormatter;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.mongo.MongoDbAdapter;
import com.celera.mongo.entity.Account;
import com.celera.mongo.entity.BOData;
import com.celera.mongo.entity.Hedge;
import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.Leg;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.entity.TradeDetail;
import com.celera.mongo.entity.TradeDetails;
import com.celera.tools.CSVReader;
import com.celera.backoffice.InvoiceTemplate;
import com.celera.mongo.entity.InvoiceRegister;
import com.uts.tradeconfo.PdfParser;
import com.uts.tradeconfo.UtsTradeConfoDetail;
import com.uts.tradeconfo.UtsTradeConfoSummary;

public class PdfCreateInvoice
{
	private static final Logger logger = LoggerFactory.getLogger(PdfCreateInvoice.class);
	
	private static final NumberFormat format = NumberFormat.getInstance();

	
	private static final String PRE_INVOICE_NUMBER = "CEL";
	private static final String PATTERN = " - ";
	private static int invoice_Num = 160228;
//	private static final String invoice_Num = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_INVOICE_NUMBER);
    private static final NumberFormat nf = new DecimalFormat("##.#");
    private static final String PREFIX_BUYER = "Buyer - ";
    private static final String PREFIX_SELLER = "Seller - ";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private static final String TRADECONFO_PATH = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_TRADECONFO_PATH);
	
	private final String todayStr = sdf.format(new Date());
	
	private final PdfParser pdfParser = new PdfParser();
	
	public static final Set<String> tempRecon = new HashSet<String>();
	
	public PdfCreateInvoice()
	{
		pdfParser.readFile(TRADECONFO_PATH);
	}
	
	public static void main(String[] args)
	{
		UtsTradeConfoSummary.load();
		
		PdfCreateInvoice run = new PdfCreateInvoice();
		run.createInvoice();
		
		run.recon();
		
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void recon() {
		for (Entry<String, InvoiceRegister> e: CSVReader.map.entrySet()) {
			String key = e.getKey().replace("IR_", "");
			if (!tempRecon.contains(key))
				logger.error("=============recon error============ csv_key = {}", key);
		}
	}
	
	public void createInvoice()
	{
		Map<String, List<UtsTradeConfoDetail>> client2TradeConfo = new HashMap<String, List<UtsTradeConfoDetail>>();

		// (firm + currency) -> trade confo
		List<UtsTradeConfoDetail> dbList = this.pdfParser._list;
		
		String key;
		for (UtsTradeConfoDetail e : dbList)
		{
			
			String curncy = e.getCurncy();
			curncy = curncy.replace("KRW", "USD").replace("JPY", "USD");
			String buyer = e.getBuyer();
			String seller = e.getSeller();
			String firm;
			String tradeDate = e.getTradeDate();


			if (buyer == null && seller == null || tradeDate == null)
			{
				continue;
			}
			else if (buyer == null)
			{
				int pos = seller.lastIndexOf(PATTERN);
				firm = (pos > 0) ? seller.substring(0,  pos) : seller;
			} else
			{
				int pos = buyer.lastIndexOf(PATTERN);
				firm = (pos > 0) ? buyer.substring(0,  pos) : buyer;
			}
			
			Date dTradeDate = null;
			try
			{
				dTradeDate = sdf_dd_mmm_yy.parse(tradeDate);
			} catch (ParseException e1)
			{
				e1.printStackTrace();
				System.exit(-1);
			} 
//			key = Invoice.key(firm, curncy, dTradeDate);
			key = firm + "_" + curncy + "_" + sdf_mmm_yy.format(dTradeDate);
			if (key.toUpperCase().endsWith("OCT16")) {
				logger.debug("{}", e);
			}
			List temp = (List) client2TradeConfo.get(key);
			if (temp == null)
			{
				temp = new ArrayList<UtsTradeConfoDetail>();
				client2TradeConfo.put(key, temp);
			}
			temp.add(e);
			
			TradeConfo tc = e.convert();
			tc.setHasInvoiceCreated(true);
			
//DatabaseAdapter.create(tc);
		}

//System.exit(-1);
		
//		String.format("CEL%04d", invoice_Num);

		// for each client
		for (Map.Entry<String, List<UtsTradeConfoDetail>> e: client2TradeConfo.entrySet())
		{
			Double totalSize = 0d;
			Double totalFee = 0d;
			Double totalHedge = 0d;
			
			key = e.getKey();
			
			// find client info
			List<TradeDetail> lTd = new ArrayList<TradeDetail>();
			
			for (UtsTradeConfoDetail tc : e.getValue())
			{
				TradeDetail tradeDetail = new TradeDetail();
				tradeDetail.setDate(tc.getTradeDate());	// same format
				tradeDetail.setTradeId(tc.getId());
				tradeDetail.setDescription(tc.getSummary());
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
					try {
						hedge += h.getQty();
					}
					catch (Exception e1) {
						logger.error("hedge invalid [{}]", h);
					}
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
					Number number = format.parse(tc.getBrokerageFee());
					totalFee += number.doubleValue();
					tradeDetail.setFee(BOFormatter.displayFee(number.doubleValue(), tc.getCurncy()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				lTd.add(tradeDetail);
				
//DatabaseAdapter.create(tradeDetail);
			}
			
			String[] tokens = key.split("_");
			String company = tokens[0];
			String curncy = tokens[1];
			String mmmyy = tokens[2];
			
//			TradeDetails td = new TradeDetails();
//			td.setTradeDetail(lTd);
//			td.setSize(BOFormatter.displayNumber(totalSize));
//			td.setHedge(BOFormatter.displayNumber(totalHedge));
//			td.setTotal_fee(BOFormatter.displayFee(totalFee, curncy));
//DatabaseAdapter.save(td);
			
			Account account = BOData.get(company);
			if (account == null) {
				logger.error("company not found: {}", company);
				System.exit(-1);
//System.out.println(key);
//				continue;
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
				inv.setDue_date("28 November, 2016");
				inv.setAmount_due(BOFormatter.displayFee(totalFee, curncy));
				inv.setDescription("October 2016 Brokerage Fee");
				inv.setAmount(BOFormatter.displayFee(totalFee, curncy));
				inv.setCurrency(curncy);
				Date tradeDate = sdf_mmm_yy.parse(mmmyy); 
				setInvoiceDate(tradeDate, inv);
				
//				inv.setTradeDetails(td);
				inv.setSize(BOFormatter.displayNumber(totalSize));
				inv.setHedge(BOFormatter.displayNumber(totalHedge));
				inv.setTotal_fee(BOFormatter.displayFee(totalFee, curncy));
				inv.setTradeDetail(lTd);
				
String invNumber = checkInvNumber(key, totalFee, inv);
if (invNumber != null)
{
//	System.out.println("=========invNum============" + invNumber);
	inv.setInvoice_number(invNumber);
}
				
				try {
					InvoiceTemplate.wordDocProcessor(inv, curncy, tradeDate);
DatabaseAdapter.create(inv);
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

	private Date nextMonth(Date d) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_MONTH, 9);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}
	
	private void setInvoiceDate(Date tradeDate, Invoice inv)
	{
//		try
//		{
//			Date d = sdf_mmm_yy.parse(mmmyy);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tradeDate.getTime());
			cal.set(Calendar.DAY_OF_MONTH, 9);
			cal.add(Calendar.MONTH, 1);
			String invdate = sdf_dd_MMMM_yy.format(cal.getTime());
			inv.setInvoice_date(invdate);
			
//			cal.add(Calendar.MONTH, 1);
//			String invduedate = sdf_dd_MMMM_yy.format(cal.getTime());
			inv.setDue_date(invdate);
//		} catch (ParseException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static SimpleDateFormat sdf_dd_mmm_yy = new SimpleDateFormat("dd-MMM-yy");
	private static SimpleDateFormat sdf_mmm_yy = new SimpleDateFormat("MMMyy");
	private static SimpleDateFormat sdf_mmmm_yyyyyy = new SimpleDateFormat("MMMM yyyy");
	private static SimpleDateFormat sdf_dd_MMMM_yy = new SimpleDateFormat("dd MMMM, yyyy");
	private static SimpleDateFormat sdf_mm_yy = new SimpleDateFormat("MMyy");
	
	public static String checkInvNumber(String key, Double totalFee, Invoice inv) {
		String[] tokens = key.split("_");
		String mmmyy = tokens[2];
		String invNumber = null;
		Date d;
		try {
			d = sdf_mmm_yy.parse(mmmyy);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_MONTH, 9);
			String fileMonth = sdf_mm_yy.format(cal.getTime());
			
			cal.add(Calendar.MONTH, 1);


//			String keyUsd = tokens[0] + "_" + tokens[1].replace("KRW", "USD").replace("JPY", "USD") + "_" + fileMonth;
			String keyUsd = inv.getAccount_number() + "_" + tokens[1].replace("KRW", "USD").replace("JPY", "USD") + "_" + fileMonth;
			keyUsd = keyUsd.toUpperCase();
tempRecon.add(keyUsd);			
			InvoiceRegister register = UtsTradeConfoSummary.map.get(keyUsd);
//			InvoiceRegister register = CSVReader.map.get(keyUsd);
			if (register == null)
			{
				// dont fxxking care recon
logger.error("=============key============== {}", keyUsd);
//System.exit(-1);
			}
			else {
				if (register.getAmount().equals(totalFee)){
//					invNumber = register.getInvoice();
					invNumber = PRE_INVOICE_NUMBER + "-" + invoice_Num++;
//System.out.println("=============amount the same==============" + invNumber);
//logger.info("=============same amount============== {}, {}, {}", tokens[0], register.getAmount(), totalFee);
					return invNumber;
				}
				else {
					if (register.getAmount().equals(totalFee/2))
					{
System.out.println("=============double amount==============" + tokens[0] + "," + register.getAmount() +"," + totalFee );
inv.showDetailsList();
					}
					else {
logger.error("=============incorrect amount============== {}, {}, {}, {}", keyUsd, register.getCustomer(), register.getAmount(), totalFee );
						inv.showDetailsList();
					}
//					System.out.println(keyUsd + "=" + register.getAmount() + "," + totalFee);
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return invNumber;
	}
}
