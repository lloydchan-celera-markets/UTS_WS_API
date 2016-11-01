package com.celera.backoffice.service;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendAttachmentInEmail
{
	private static final String DEFAULT_EMAIL_SERVER_PROTO = "imap";
	private static final String DEFAULT_EMAIL_SERVER_IP = "outlook.office365.com";
	private static final String DEFAULT_EMAIL_SERVER_PORT = "993";
	private static final String DEFAULT_EMAIL_SERVER_USER = "Lloyd.Chan@celera-markets.com";
	private static final String DEFAULT_EMAIL_SERVER_PWD = "Ja9XuVDj";
	
	public static void main(String[] args)
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

			// Set Subject: header field
			message.setSubject("Testing Invoice");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("This is testing invoice message");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String path = "/home/idbs/workspace/uts/build/UTS_WS_API/celera_cmbos/temp/invoice_template_new.pdf";
			DataSource source = new FileDataSource(path);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("invoice_template_new.pdf");
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}
}