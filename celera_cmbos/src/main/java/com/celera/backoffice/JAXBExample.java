//package com.celera.backoffice;
//
//import java.io.File;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//
//import com.celera.mongo.entity.Invoice;
//
//public class JAXBExample
//{
//	public static void main(String[] args)
//	{
//		String company = "Vivienne Court Ptrading Pty Limited";
//		String address = "Suite 304, 24-30 Springfield Avenue Potts Point, Sydney, NSW 2011, Australia";
//		String attn = "midoffice@vivcourt.com";
//		String invoice_number = "CEL-160055";
//		String invoice_date = "11 Jul 2016";
//		String account_number = "CEL0004";
//		String due_date = "28 July, 2016";
//		String amount_due = "HK$7,308.00";
//		String description = "June 2016 Brokerage Fee";
//		String amount = "HK$7,308.00";
//
//		Invoice inv = new Invoice();
//		inv.setCompany(company);
//		inv.setAddress(address);
//		inv.setAttn(attn);
//		inv.setInvoice_number(invoice_number);
//		inv.setInvoice_date(invoice_date);
//		inv.setAccount_number(account_number);
//		inv.setDue_date(due_date);
//		inv.setAmount_due(amount_due);
//		inv.setDescription(description);
//		inv.setAmount(amount);
//
//		try
//		{
//
//			File file = new File("/home/idbs/workspace/uts/build/UTS_WS_API/celera_cmbos/temp/invoice.xml");
//			JAXBContext jaxbContext = JAXBContext.newInstance(Invoice.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			// output pretty printed
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//			jaxbMarshaller.marshal(inv, file);
//			jaxbMarshaller.marshal(inv, System.out);
//
//		} catch (JAXBException e)
//		{
//			e.printStackTrace();
//		}
//
//	}
//}