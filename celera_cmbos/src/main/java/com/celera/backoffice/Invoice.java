package com.celera.backoffice;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Invoice")
public class Invoice
{
	String company;
	String address;
	String attn;
	String invoice_number;
	String invoice_date;
	String account_number;
	String due_date;
	String amount_due;
	String description;
	String amount;
	
	// static
	String payment_bank_name = "DBS Bank (Hong Kong) Limited";
	String payment_bank_address = "Floor 16, The Center, 99 Queen's Road Central, Central, Hong Kong";
	String payment_bank_code = "016";
	String payment_branch_code = "478";
	String payment_account_number = "7883658530";
	String payment_account_beneficiary = "Celera Markets Limited";
	String payment_swift = "DHBKHKHH";

	@XmlElement
	public void setCompany(String company)
	{
		this.company = company;
	}
	@XmlElement
	public void setAddress(String address)
	{
		this.address = address;
	}
	@XmlElement
	public void setAttn(String attn)
	{
		this.attn = attn;
	}
	@XmlElement
	public void setInvoice_number(String invoice_number)
	{
		this.invoice_number = invoice_number;
	}
	@XmlElement
	public void setInvoice_date(String invoice_date)
	{
		this.invoice_date = invoice_date;
	}
	@XmlElement
	public void setAccount_number(String account_number)
	{
		this.account_number = account_number;
	}
	@XmlElement
	public void setDue_date(String due_date)
	{
		this.due_date = due_date;
	}
	@XmlElement
	public void setAmount_due(String amount_due)
	{
		this.amount_due = amount_due;
	}
	@XmlElement
	public void setDescription(String description)
	{
		this.description = description;
	}
	@XmlElement
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	@XmlElement
	public void setPayment_bank_name(String payment_bank_name)
	{
		this.payment_bank_name = payment_bank_name;
	}
	@XmlElement
	public void setPayment_bank_address(String payment_bank_address)
	{
		this.payment_bank_address = payment_bank_address;
	}
	@XmlElement
	public void setPayment_bank_code(String payment_bank_code)
	{
		this.payment_bank_code = payment_bank_code;
	}
	@XmlElement
	public void setPayment_branch_code(String payment_branch_code)
	{
		this.payment_branch_code = payment_branch_code;
	}
	@XmlElement
	public void setPayment_account_number(String payment_account_number)
	{
		this.payment_account_number = payment_account_number;
	}
	@XmlElement
	public void setPayment_account_beneficiary(String payment_account_beneficiary)
	{
		this.payment_account_beneficiary = payment_account_beneficiary;
	}
	@XmlElement
	public void setPayment_swift(String payment_swift)
	{
		this.payment_swift = payment_swift;
	}
	public String getCompany()
	{
		return company;
	}
	public String getAddress()
	{
		return address;
	}
	public String getAttn()
	{
		return attn;
	}
	public String getInvoice_number()
	{
		return invoice_number;
	}
	public String getInvoice_date()
	{
		return invoice_date;
	}
	public String getAccount_number()
	{
		return account_number;
	}
	public String getDue_date()
	{
		return due_date;
	}
	public String getAmount_due()
	{
		return amount_due;
	}
	public String getDescription()
	{
		return description;
	}
	public String getAmount()
	{
		return amount;
	}
	public String getPayment_bank_name()
	{
		return payment_bank_name;
	}
	public String getPayment_bank_address()
	{
		return payment_bank_address;
	}
	public String getPayment_bank_code()
	{
		return payment_bank_code;
	}
	public String getPayment_branch_code()
	{
		return payment_branch_code;
	}
	public String getPayment_account_number()
	{
		return payment_account_number;
	}
	public String getPayment_account_beneficiary()
	{
		return payment_account_beneficiary;
	}
	public String getPayment_swift()
	{
		return payment_swift;
	}
	@Override
	public String toString()
	{
		return "Invoice [company=" + company + ", address=" + address + ", attn=" + attn + ", invoice_number="
				+ invoice_number + ", invoice_date=" + invoice_date + ", account_number=" + account_number
				+ ", due_date=" + due_date + ", amount_due=" + amount_due + ", description=" + description + ", amount="
				+ amount + ", payment_bank_name=" + payment_bank_name + ", payment_bank_address=" + payment_bank_address
				+ ", payment_bank_code=" + payment_bank_code + ", payment_branch_code=" + payment_branch_code
				+ ", payment_account_number=" + payment_account_number + ", payment_account_beneficiary="
				+ payment_account_beneficiary + ", payment_swift=" + payment_swift + "]";
	}
}
