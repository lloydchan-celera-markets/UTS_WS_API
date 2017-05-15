package com.celera.mongo.entity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.celera.mongo.repo.InvoiceRepo;


//@XmlRootElement(name="Invoice")	// generate xml -> pdf
@Document(collection = "invoice")
public class Invoice implements ICustomizeMongoDocument<InvoiceRepo>
{
	private static final Logger logger = LoggerFactory.getLogger(Invoice.class);

	private static final SimpleDateFormat sdf_mm_yy = new SimpleDateFormat("MMyy");
	
	@Id
	private String id;
	
	private String company;
	private String address;
	private String attn;
	private String sentTo;
	private String invoice_number;
	private String invoice_date;
	private String account_number;
	private String due_date;
	private String amount_due;
	private String description;
	private String amount;
	private String key;
	private Boolean isPaid = false;
	private Boolean hasSent = false;
	private String currency;
	
	// static
	private String paymentBankName = "DBS Bank (Hong Kong) Limited";
	private String paymentBankAddress = "Floor 16, The Center, 99 Queen's Road Central, Central, Hong Kong";
	private String paymentBankCode = "016";
	private String paymentBranchCode = "478";
	private String paymentAccountNumber = "7883658530";
	private String paymentAccountBeneficiary = "Celera Markets Limited";
	private String paymentSwift = "DHBKHKHH";
	
	private String file;

	private String size;
	private String hedge;
	private String total_fee;
	
	private String fileSizeInBytes;
	
//	@DBRef(db = "tradedetail")
	List<TradeDetail> tradeDetail = new ArrayList<TradeDetail>();
	
//	@DBRef(db = "tradedetails")
//    private TradeDetails tradeDetails;

	private Date lastModified;

	public Invoice() {}

    public void showDetailsList() {
    	for( TradeDetail td : tradeDetail) {
    		System.out.println(td.getTradeId() + "," + td.getFee());
    	}
	}
	
	@PersistenceConstructor
	public Invoice(String id, String company, String address, String attn, String sentTo, String invoice_number,
			String invoice_date, String account_number, String due_date, String amount_due, String description,
			String amount, String key, Boolean isPaid, Boolean hasSent, String currency, String paymentBankName,
			String paymentBankAddress, String paymentBankCode, String paymentBranchCode, String paymentAccountNumber,
			String paymentAccountBeneficiary, String paymentSwift, String file, String size, String hedge,
			String total_fee, List<TradeDetail> tradeDetail, String fileSizeInBytes, Date lastModified)
	{
		super();
		this.id = id;
		this.company = company;
		this.address = address;
		this.attn = attn;
		this.sentTo = sentTo;
		this.invoice_number = invoice_number;
		this.invoice_date = invoice_date;
		this.account_number = account_number;
		this.due_date = due_date;
		this.amount_due = amount_due;
		this.description = description;
		this.amount = amount;
		this.isPaid = isPaid;
		this.hasSent = hasSent;
		this.currency = currency;
		this.paymentBankName = paymentBankName;
		this.paymentBankAddress = paymentBankAddress;
		this.paymentBankCode = paymentBankCode;
		this.paymentBranchCode = paymentBranchCode;
		this.paymentAccountNumber = paymentAccountNumber;
		this.paymentAccountBeneficiary = paymentAccountBeneficiary;
		this.paymentSwift = paymentSwift;
		this.file = file;
		this.size = size;
		this.hedge = hedge;
		this.total_fee = total_fee;
		this.tradeDetail = tradeDetail;
		this.fileSizeInBytes = fileSizeInBytes;
		this.lastModified = lastModified;
		
		this.key = key;
	}

	public String getFileSizeInBytes()
	{
		return fileSizeInBytes;
	}

	public void setFileSizeInBytes(String fileSizeInBytes)
	{
		this.fileSizeInBytes = fileSizeInBytes;
	}

	public String getCurrency()
	{
		return currency;
	}
	
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	@Override
	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key.toUpperCase();
	}

	public Boolean getIsPaid()
	{
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid)
	{
		this.isPaid = isPaid;
		this.lastModified = new Date();
	}

	public Boolean getHasSent()
	{
		return hasSent;
	}

	public void setHasSent(Boolean hasSent)
	{
		this.hasSent = hasSent;
		this.lastModified = new Date();
	}

	public String getFile()
	{
		return file;
	}

	public void setFile(String file)
	{
		this.file = file;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getAttn()
	{
		return attn;
	}

	public void setAttn(String attn)
	{
		this.attn = attn;
	}

	public String getSentTo()
	{
		return sentTo;
	}

	public void setSentTo(String sentTo)
	{
		this.sentTo = sentTo;
	}

	public String getInvoice_number()
	{
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number)
	{
		this.invoice_number = invoice_number;
	}

	public String getInvoice_date()
	{
		return invoice_date;
	}

	public void setInvoice_date(String invoice_date)
	{
		this.invoice_date = invoice_date;
	}

	public String getAccount_number()
	{
		return account_number;
	}

	public void setAccount_number(String account_number)
	{
		this.account_number = account_number;
	}

	public String getDue_date()
	{
		return due_date;
	}

	public void setDue_date(String due_date)
	{
		this.due_date = due_date;
	}

	public String getAmount_due()
	{
		return amount_due;
	}

	public void setAmount_due(String amount_due)
	{
		this.amount_due = amount_due;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAmount()
	{
		return amount;
	}

	public void setAmount(String amount)
	{
		this.amount = amount;
	}

	public String getPaymentBankName()
	{
		return paymentBankName;
	}

	public void setPaymentBankName(String paymentBankName)
	{
		this.paymentBankName = paymentBankName;
	}

	public String getPaymentBankAddress()
	{
		return paymentBankAddress;
	}

	public void setPaymentBankAddress(String paymentBankAddress)
	{
		this.paymentBankAddress = paymentBankAddress;
	}

	public String getPaymentBankCode()
	{
		return paymentBankCode;
	}

	public void setPaymentBankCode(String paymentBankCode)
	{
		this.paymentBankCode = paymentBankCode;
	}

	public String getPaymentBranchCode()
	{
		return paymentBranchCode;
	}

	public void setPaymentBranchCode(String paymentBranchCode)
	{
		this.paymentBranchCode = paymentBranchCode;
	}

	public String getPaymentAccountNumber()
	{
		return paymentAccountNumber;
	}

	public void setPaymentAccountNumber(String paymentAccountNumber)
	{
		this.paymentAccountNumber = paymentAccountNumber;
	}

	public String getPaymentAccountBeneficiary()
	{
		return paymentAccountBeneficiary;
	}

	public void setPaymentAccountBeneficiary(String paymentAccountBeneficiary)
	{
		this.paymentAccountBeneficiary = paymentAccountBeneficiary;
	}

	public String getPaymentSwift()
	{
		return paymentSwift;
	}

	public void setPaymentSwift(String paymentSwift)
	{
		this.paymentSwift = paymentSwift;
	}

	public Date getLastModified()
	{
		return lastModified;
	}
	
	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public String getHedge()
	{
		return hedge;
	}

	public void setHedge(String hedge)
	{
		this.hedge = hedge;
	}

	public String getTotal_fee()
	{
		return total_fee;
	}

	public void setTotal_fee(String total_fee)
	{
		this.total_fee = total_fee;
	}

	public List<TradeDetail> getTradeDetail()
	{
		return tradeDetail;
	}

	public void setTradeDetail(List<TradeDetail> tradeDetail)
	{
		this.tradeDetail = tradeDetail;
	}

	@Override
	public String toString()
	{
		return "Invoice [id=" + id + ", company=" + company + ", address=" + address + ", attn=" + attn + ", sentTo="
				+ sentTo + ", invoice_number=" + invoice_number + ", invoice_date=" + invoice_date + ", account_number="
				+ account_number + ", due_date=" + due_date + ", amount_due=" + amount_due + ", description="
				+ description + ", amount=" + amount + ", key=" + key + ", isPaid=" + isPaid + ", hasSent=" + hasSent
				+ ", currency=" + currency + ", paymentBankName=" + paymentBankName + ", paymentBankAddress="
				+ paymentBankAddress + ", paymentBankCode=" + paymentBankCode + ", paymentBranchCode="
				+ paymentBranchCode + ", paymentAccountNumber=" + paymentAccountNumber + ", paymentAccountBeneficiary="
				+ paymentAccountBeneficiary + ", paymentSwift=" + paymentSwift + ", file=" + file + ", size=" + size
				+ ", hedge=" + hedge + ", total_fee=" + total_fee + ", fileSizeInBytes=" + fileSizeInBytes
				+ ", tradeDetail=" + tradeDetail + ", lastModified=" + lastModified + "]";
	}

	@Override
	public void setLastModified(Date d)
	{
		this.lastModified = d;
	}

	@Override
	public Class getRepo()
	{
		return InvoiceRepo.class;
	}
	
	public static String key(String participant, String currency, Date invDate)
	{
		try {
			String company = participant.split(" - ")[0];
			String accNumber = BOData.get(company).getId();
			return new String(accNumber + "_" + currency + "_" + sdf_mm_yy.format(invDate)).toUpperCase();
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(participant + "," + currency + "," + invDate);
			System.exit(-1);
		}
		return null;
	}
	
//	public static String keyLowerCase(String participant, String currency, Date d)
//	{
//		String[] token = participant.split(" - ");
//		return new String(token[0] + "_" + currency + "_" + sdf_mm_yy.format(d));
//	}
	
	public JsonObject json()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			try
			{
				Object o = f.get(this);
				if (o != null)
				{
//					if (o instanceof TradeDetails)
//					{
//						String name = f.getName();
//						builder.add(name, ((TradeDetails)o).json());
//					}
//					else {
						String name = f.getName();
						builder.add(name, o.toString());
//					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e)
			{
				logger.error("", e);
			}
		}
		
		JsonArrayBuilder tdBuilder = Json.createArrayBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			try
			{
				Object o = f.get(this);
				if (o != null)
				{
					String name = f.getName();
					builder.add(name, o.toString());
				}
			} catch (IllegalArgumentException | IllegalAccessException e)
			{
				logger.error("", e);
			}
		}
		
		for (TradeDetail h : this.tradeDetail)
		{
			tdBuilder.add(h.json());
		}
		builder.add("tradedetail", tdBuilder);
		
		JsonObject empJsonObject = builder.build();

		logger.debug("Invoice JSON {}", empJsonObject);

		return empJsonObject;
	}
}
