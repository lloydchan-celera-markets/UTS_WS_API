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
import com.celera.backoffice.Invoice;
import com.celera.backoffice.TradeDetail;
import com.celera.backoffice.TradeDetails;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.mongo.entity.Hedge;
import com.celera.mongo.entity.Leg;
import com.celera.tools.CSVReader;
import com.celera.tools.InvoiceRegister;
import com.celera.backoffice.InvoiceTemplate;
import com.uts.tradeconfo.PdfParser;
import com.uts.tradeconfo.UtsTradeConfoDetail;

public class PdfCreateInvoice implements Runnable
{
	Logger logger = LoggerFactory.getLogger(PdfCreateInvoice.class);
	
	private static final NumberFormat format = NumberFormat.getInstance();

	
	private static final String PRE_INVOICE_NUMBER = "CEL";
	private static final String PATTERN = " - ";
	private static int invoice_Num = 1;
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
		PdfCreateInvoice run = new PdfCreateInvoice();
		run.run();
		
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
			String key = e.getKey();
			if (!tempRecon.contains(key))
				System.out.println("=============recon error============" + key);
		}
	}
	
	public void run()
	{
		Map<String, List<UtsTradeConfoDetail>> client2TradeConfo = new HashMap<String, List<UtsTradeConfoDetail>>();

		// (firm + currency) -> trade confo
		List<UtsTradeConfoDetail> dbList = this.pdfParser._list;
		
		String key;
		for (UtsTradeConfoDetail e : dbList)
		{
//if (e.getId().equals("CELERAEQ-2016-13031"))
//	System.out.println("CELERAEQ-2016-13031");
			
			String curncy = e.getCurncy();
			String buyer = e.getBuyer();
			String seller = e.getSeller();
			String firm;
			String tradeDate = e.getTradeDate();


//System.out.println("tradeDate -> " + tradeDate);			
			if (buyer == null && seller == null || tradeDate == null)
			{
//System.out.println("buyer == null && seller == null && tradeDate == null");
//System.out.println(e);
				continue;
			}
			else if (buyer == null)
			{
				String[] tokens = tradeDate.split("-");
				String mmm = tokens[1];
				String yy = tokens[2];
				seller = e.getSeller();
				int pos = seller.lastIndexOf(PATTERN);
				firm = (pos > 0) ? seller.substring(0,  pos) : seller;
				key = firm + "_" + curncy + "_" + mmm+yy;
			} else
			{
				String[] tokens = tradeDate.split("-");
				String mmm = tokens[1];
				String yy = tokens[2];
				seller = e.getSeller();
				
				int pos = buyer.lastIndexOf(PATTERN);
				firm = (pos > 0) ? buyer.substring(0,  pos) : buyer;
				key = firm + "_" + curncy + "_" + mmm+yy;
			}
			List temp = (List) client2TradeConfo.get(key);
			if (temp == null)
			{
				temp = new ArrayList<UtsTradeConfoDetail>();
				client2TradeConfo.put(key, temp);
				
				
			}
			temp.add(e);
		}

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
				tradeDetail.setId(tc.getId());
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
					Number number = format.parse(tc.getBrokerageFee());
					totalFee += number.doubleValue();
					tradeDetail.setFee(BOFormatter.displayFee(number.doubleValue(), tc.getCurncy()));
				} catch (Exception e1) {
//System.out.println("Exception borager=" + tc.getBrokerageFee() + "," + tc.getCurncy());
					e1.printStackTrace();
				}
				lTd.add(tradeDetail);
			}
			
			TradeDetails td = new TradeDetails();
			td.setTradeDetail(lTd);
			td.setPeriod("OCTOBER, 2016");
			String[] tokens = key.split("_");
			String company = tokens[0];
			String curncy = tokens[1];
			String mmmyy = tokens[2];
			td.setCompany(company);
			td.setDescription("October 2016 Total");
			td.setSize(BOFormatter.displayNumber(totalSize));
			td.setHedge(BOFormatter.displayNumber(totalHedge));
			td.setTotal_fee(BOFormatter.displayFee(totalFee, curncy));
			
			com.celera.backoffice.Account account = com.celera.backoffice.Account.get(company.toUpperCase());
			if (account == null) {
				logger.error("company not found: {}", company);
System.out.println(key);
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
				inv.setDue_date("28 November, 2016");
				inv.setAmount_due(BOFormatter.displayFee(totalFee, curncy));
				inv.setDescription("October 2016 Brokerage Fee");
				inv.setAmount(BOFormatter.displayFee(totalFee, curncy));
				inv.setTradeDetails(td);

String invNumber = checkInvNumber(key, totalFee, inv);
if (invNumber != null)
{
//	System.out.println("=========invNum============" + invNumber);
	inv.setInvoice_number(invNumber);
}
				
				try {
					InvoiceTemplate.wordDocProcessor(inv, curncy, mmmyy);
				} catch (InvalidFormatException ex) {
					// TODO Auto-generated catch block
					logger.error("", ex);
//					ex.printStackTrace();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
//					ex.printStackTrace();
					logger.error("", ex);
				}

//				try {
//					InvoiceTemplate.csvProcessor(td, inv.getCompany(), curncy, mmmyy);
//				} catch (InvalidFormatException ex) {
//					// TODO Auto-generated catch block
//					ex.printStackTrace();
//				} catch (IOException ex) {
//					// TODO Auto-generated catch block
//					ex.printStackTrace();
//				}
				
			}
			catch (Exception ex) 
			{
			}
		}
	}

	private static SimpleDateFormat sdf_mmm_yy = new SimpleDateFormat("MMMyy");
	private static SimpleDateFormat sdf_mmmm_yyyyyy = new SimpleDateFormat("MMMM yyyy");
	private static SimpleDateFormat sdf_dd_MMMM_yy = new SimpleDateFormat("dd MMMM, yyyy");
	
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
			cal.add(Calendar.MONTH, 1);
			String fileMonth = sdf_mmm_yy.format(cal.getTime());

			String keyUsd = tokens[0] + "_" + tokens[1].replace("KRW", "USD").replace("JPY", "USD") + "_" + fileMonth;
			keyUsd = keyUsd.toUpperCase();
tempRecon.add(keyUsd);			
			InvoiceRegister register = CSVReader.map.get(keyUsd);
			if (register == null)
			{
				// dont fxxking care recon
//				System.out.println("=============key==============" + keyUsd);
			}
			else {
				if (register.getAmount().equals(totalFee)){
					invNumber = register.getInvoice();
//System.out.println("=============amount the same==============" + invNumber);
System.out.println("=============same amount==============" + tokens[0] + "," + register.getAmount() +"," + totalFee );
					return invNumber;
				}
				else {
					if (register.getAmount().equals(totalFee/2))
					{
//System.out.println("=============double amount==============" + tokens[0] + "," + register.getAmount() +"," + totalFee );
inv.showDetailsList();
					}
					else {
System.out.println("=============incorrect amount==============" + keyUsd + "," + register.getAmount() +"," + totalFee );
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
