package com.celera.backoffice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.TradeDetail;

public class SendAttachmentInEmail
{
	private static final Logger logger = LoggerFactory.getLogger(SendAttachmentInEmail.class);
	
	private static final String DEFAULT_EMAIL_SERVER_PROTO = "imap";
	private static final String DEFAULT_EMAIL_SERVER_IP = "outlook.office365.com";
	private static final String DEFAULT_EMAIL_SERVER_PORT = "993";
	private static final String DEFAULT_EMAIL_SERVER_USER = "Lloyd.Chan@celera-markets.com";
	private static final String DEFAULT_EMAIL_SERVER_PWD = "Ja9XuVDj";
	
	private static final SimpleDateFormat sdf_ddMMMMyyyy = new SimpleDateFormat("dd MMMM, yyyy");
	private static final SimpleDateFormat sdf_MMddyy = new SimpleDateFormat("MM/dd/yy");
	private static final SimpleDateFormat sdf_ddMMMyyyy = new SimpleDateFormat("dd-MMM-yy");
	private static final SimpleDateFormat sdf_MMMMyyyy = new SimpleDateFormat("MMMM yyyy");
	
	public static String buildHtmlContent(List<Invoice> l)
	{
		String msg = "<div style='font-family: Calibri'>"  
				+ "<p>Dear Sir or Madam,</p>"
				+ "<p>I hope this email finds you well,</p>"
				+ "<p>Find attached monthly invoice as well as single trade confirmations and summary below.</p>"
				+ "<p>Please kindly make necessary payments and let us know expected pay date.</p>"
				+ "<table border=0 cellspacing=0 cellpadding=0 width=557 style='width:418.1pt;border-collapse:collapse'>";
		for (Invoice inv: l) 
		{
			Date invDate;
			try
			{
				invDate = sdf_ddMMMMyyyy.parse(inv.getInvoice_date());
				String sInvDate = sdf_MMddyy.format(invDate);
				msg += "<tr style='height:16.5pt'><td width=84 nowrap valign=top style='width:63.0pt;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
						+ sInvDate + "</p></td>"
						+ "<td width=97 nowrap valign=top style='width:73.0pt;border:solid windowtext 1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
						+ inv.getInvoice_number() + "</p></td>"
						+ "<td width=184 nowrap valign=top style='width:138.1pt;border:solid windowtext 1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
						+ inv.getCompany() + "</p></td><td width=90 nowrap valign=top style='width:67.5pt;border:solid windowtext 1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
						+ inv.getAmount_due() + "</p></td>"
						+ "</tr>";
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msg += 	"</table>"
				+ "<p>If you require any extra information please don&#8217;t hesitate to contact us.</p>"
				+ "<p>Thank you very much in advance.<br></p>"
//				+ "<p style='font-size:11pt'>Guillaume Cunnington<br>"
//				+ "<span style='color:gray'>Head of Brokerage <br>Celera Markets Limited<br>Office +852 3746 3803 Mobile +852 9089 7796"
//				+ "</span></p>"
//				+ "<p><span lang=FR style='font-size:7.5pt;color:#848484'>PLEASE READ: The information contained in this e-mail " 
//				+ "is confidential and intended for the named recipient(s) only. If you are not an intended recipient of this e-mail you must not "
//				+ "copy, distribute or take any further action in reliance upon it and you should delete it and notify the sender immediately. "
//				+ "E-mail is not a secure method of communication. Celera Markets Limited cannot accept responsibility for the accuracy or "
//				+ "completeness of this message or any attachment(s). This transmission could contain viruses, be corrupted, destroyed, "
//				+ "incomplete, intercepted, lost or arrive late. If verification of this e-mail is sought then please request a hard copy. " 
//				+ "Unless otherwise stated any views or opinions presented are solely those of the author and do not represent those of "
//				+ "Celera Markets Limited. This e-mail is intended for information purposes only and is not a solicitation or offer to "
//				+ "buy or sell securities or related financial instruments.</span></p>"
				+ "</div>";
		return msg;
	}
//	
//	public static String buildHtmlContent(List<Invoice> l)
//	{
//		String msg = "<div class=WordSection1>"  
//				+ "<p class=MsoPlainText>Dear Sir, Madam,</p>"
//				+ "<p class=MsoPlainText>I hope this email finds you well,</p>"
//				+ "<p>Find attached monthly invoice as well as single trade confirmations and summary below.</p>"
//				+ "<p>Please kindly make necessary payments and let us know expected pay date.</p>"
//				+ "<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=557 style='width:418.1pt;border-collapse:collapse'>";
//		for (Invoice inv: l) 
//		{
//			Date invDate;
//			try
//			{
//				invDate = sdf_ddMMMMyyyy.parse(inv.getInvoice_date());
//				String sInvDate = sdf_MMddyy.format(invDate);
//				msg += "<tr style='height:16.5pt'><td width=84 nowrap valign=top style='width:63.0pt;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
//						+ sInvDate + "</p></td>"
//						+ "<td width=97 nowrap valign=top style='width:73.0pt;border:solid windowtext 1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
//						+ inv.getInvoice_number() + "</p></td>"
//						+ "<td width=184 nowrap valign=top style='width:138.1pt;border:solid windowtext 1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
//						+ inv.getCompany() + "</p></td><td width=90 nowrap valign=top style='width:67.5pt;border:solid windowtext 1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt;height:16.5pt'><p class=MsoNormal>"
//						+ inv.getAmount_due() + "</p></td>"
//						+ "</tr>";
//			} catch (ParseException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		msg += 	"</table>"
//				+ "<p class=MsoPlainText>If you require any extra information please don&#8217;t hesitate to contact us.</p>"
//				+ "<p class=MsoPlainText>Thank you very much in advance.<br><br></p>"
//				+ "<p class=MsoNormal>Guillaume Cunnington<br>"
//				+ "<span style='font-size:10pt;color:gray'>Head of Brokerage <br>Celera Markets Limited<br>Office +852 3746 3803 Mobile +852 9089 7796"
//				+ "</span></p>"
//				+ "<p class=MsoNormal><span lang=FR style='font-size:7.5pt;color:#848484'>PLEASE READ: The information contained in this e-mail " 
//				+ "is confidential and intended for the named recipient(s) only. If you are not an intended recipient of this e-mail you must not "
//				+ "copy, distribute or take any further action in reliance upon it and you should delete it and notify the sender immediately. "
//				+ "E-mail is not a secure method of communication. Celera Markets Limited cannot accept responsibility for the accuracy or "
//				+ "completeness of this message or any attachment(s). This transmission could contain viruses, be corrupted, destroyed, "
//				+ "incomplete, intercepted, lost or arrive late. If verification of this e-mail is sought then please request a hard copy. " 
//				+ "Unless otherwise stated any views or opinions presented are solely those of the author and do not represent those of "
//				+ "Celera Markets Limited. This e-mail is intended for information purposes only and is not a solicitation or offer to "
//				+ "buy or sell securities or related financial instruments.</span></p>"
//				+ "</div>";
//		return msg;
//	}
	
	public static void sendEmail(List<Invoice> invList) throws ParseException
	// public static void main(String[] args)
	{
		// Recipient's email ID needs to be mentioned.
//		String to_2 = "invoices@celera-markets.com";
//		String to_2 = "lloyd.chan@celera-markets.com,Guillaume.Cunnington@celera-markets.com";
		String to_2 = "lloyd.chan@celera-markets.com";

		// Address[] to = null;
		// try
		// {
		// to = new Address[]{new InternetAddress(to_1), new
		// InternetAddress(to_2)};
		// } catch (AddressException e)
		// {
		// e.printStackTrace();
		// }

		// Sender's email ID needs to be mentioned
		String from = "lloyd.chan@celera-markets.com";

		final String username = DEFAULT_EMAIL_SERVER_USER;// change accordingly
		final String password = DEFAULT_EMAIL_SERVER_PWD;// change accordingly

		// // Assuming you are sending email through relay.jangosmtp.net
		// String host = "relay.jangosmtp.net";

		Properties props = new Properties();
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".auth", "true");
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".starttls.enable", "true");
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".host", DEFAULT_EMAIL_SERVER_IP);
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".port", DEFAULT_EMAIL_SERVER_PORT);

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		Map<String, List<Invoice>> _map = new HashMap<String, List<Invoice>>();
		for (Invoice inv : invList)
		{
			String invDate = inv.getInvoice_date().substring(3).replaceAll(",", "");
			String key = inv.getAccount_number() + "_" + invDate;
			if (_map.containsKey(key))
			{
				List<Invoice> l = _map.get(key);
				l.add(inv);
			} else
			{
				List<Invoice> l = new ArrayList<Invoice>();
				l.add(inv);
				_map.put(key, l);
			}
		}

		for (Entry<String, List<Invoice>> e : _map.entrySet())
		{
			List<Invoice> l = e.getValue();
			String company = l.get(0).getCompany();
			String sTradeMonth = l.get(0).getTradeDetail().get(0).getDate();
			Date dTradeMonth = sdf_ddMMMyyyy.parse(sTradeMonth);
			String invMonth = sdf_MMMMyyyy.format(dTradeMonth);
			String text = buildHtmlContent(l);
			String invNum_List = "";

			try
			{
				// Create a default MimeMessage object.
				Message message = new MimeMessage(session);

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));

				// Set To: header field of the header.
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to_2));

				// Set Subject: header field
				message.setSubject(company + " -" + invMonth + " Invoices- Celera Markets Limited");

				// Create a multipar message
				Multipart multipart = new MimeMultipart();
				// Create the message part
				BodyPart messageBody = new MimeBodyPart();

				// String msg = "\n" + "I hope this email finds you well,\n\n"
				// + "Please Find attached the monthly invoice, as well as
				// single trade confirmations.\n"
				// + "Kindly make the necessary payment and let us know when we
				// can expect it .\n\n"
				// + "If you require any extra information please don’t hesitate
				// to contact us .\n\n"
				// + "Best regards,\n" + "Amine Larhrib\n" + "Celera Markets
				// Limited\n"
				// + "Office: (852) 3746-3800\n" + "Cell : ( 852) 6603 4121";

				// Now set the actual message
//				messageBodyPart.setText(text, "utf-8", "html");
				messageBody.setContent(text, "text/html; charset=utf-8");
				// Set text message part
				multipart.addBodyPart(messageBody);
				
				for (Invoice inv : l)
				{
					invNum_List += inv.getInvoice_number() + ", ";

					// Part two is attachment
					BodyPart messageBodyPart = new MimeBodyPart();
					String path = inv.getFile().replaceAll(".docx", ".pdf");
					int i = path.lastIndexOf('/');
					String fileName = path.substring(i);
					// String path =
					// "/home/idbs/workspace/uts/build/UTS_WS_API/celera_cmbos/temp/invoice_template_new.pdf";
					DataSource source = new FileDataSource(path);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileName);
					multipart.addBodyPart(messageBodyPart);

					for (TradeDetail td : inv.getTradeDetail())
					{
						MimeBodyPart part = new MimeBodyPart();
						path = td.getTradeConfoFile();
						i = path.lastIndexOf('/');
						fileName = path.substring(i);
						// String path =
						// "/home/idbs/workspace/uts/build/UTS_WS_API/celera_cmbos/temp/invoice_template_new.pdf";
						DataSource src = new FileDataSource(path);
						part.setDataHandler(new DataHandler(src));
						part.setFileName(fileName);
						multipart.addBodyPart(part);
					}
				}
				// Send the complete message parts
				message.setContent(multipart);
				
				// Send message
				Transport.send(message);

				logger.info("Sent email successfully {}", invNum_List);

			} catch (MessagingException ex)
			{
				logger.error("fail send email {}", invNum_List, ex);
				throw new RuntimeException(ex);
			}
		}
	}
	
	public static void sendEmail1(Invoice inv)
//	public static void main(String[] args)
	{
		// Recipient's email ID needs to be mentioned.
//		String to = "Amine.Larhrib@celera-markets.com";
		String to = "lloyd.chan@celera-markets.com";

		// Sender's email ID needs to be mentioned
		String from = "lloyd.chan@celera-markets.com";

		final String username = DEFAULT_EMAIL_SERVER_USER;// change accordingly
		final String password = DEFAULT_EMAIL_SERVER_PWD;// change accordingly

//		// Assuming you are sending email through relay.jangosmtp.net
//		String host = "relay.jangosmtp.net";

		Properties props = new Properties();
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".auth", "true");
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".starttls.enable", "true");
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".host", DEFAULT_EMAIL_SERVER_IP);
		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".port", DEFAULT_EMAIL_SERVER_PORT);

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try
		{
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			String company = inv.getCompany();
			
			// Set Subject: header field
			message.setSubject(company + " -Due Invoices- Celera Markets limited");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			String msg = "\n" +
"Dear Sir, Madam,\n\nI hope this email finds you well,\n\n" +  
"Find attached monthly invoice as well as single trade confirmations and summary below\n" +  
"Please kindly make the necessary payment and let us know expected pay date.\n\n" +
"If you require any extra information, please don’t hesitate to contact us.\n\n" +
"Thank you very much in advance.\n\n" +

"Guillaume Cunnington\n" +
"Head of Brokerage\nCelera Markets Limited\nOffice: +852 3746 3803 Mobile +852 9090 7796\n";

			// Now set the actual message
			messageBodyPart.setText(msg);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String path = inv.getFile().replaceAll(".docx", ".pdf");
			int i = path.lastIndexOf('/');
			String fileName = path.substring(i);
//			String path = "/home/idbs/workspace/uts/build/UTS_WS_API/celera_cmbos/temp/invoice_template_new.pdf";
			DataSource source = new FileDataSource(path);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
			
			for (TradeDetail td : inv.getTradeDetail()) {
				MimeBodyPart part = new MimeBodyPart();
				path = td.getTradeConfoFile();
				i = path.lastIndexOf('/');
				fileName = path.substring(i);
//				String path = "/home/idbs/workspace/uts/build/UTS_WS_API/celera_cmbos/temp/invoice_template_new.pdf";
				DataSource src = new FileDataSource(path);
				part.setDataHandler(new DataHandler(src));
				part.setFileName(fileName);
				multipart.addBodyPart(part);
			}

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			logger.info("Sent email successfully");

		} catch (MessagingException e)
		{
			logger.error("fail send email", e);
			throw new RuntimeException(e);
		}
	}
}