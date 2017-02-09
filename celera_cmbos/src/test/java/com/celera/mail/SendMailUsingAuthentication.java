package com.celera.mail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.mail.Authenticator;
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

public class SendMailUsingAuthentication
{


//	private static final String DEFAULT_EMAIL_SERVER_PROTO = "imap";
//	private static final String DEFAULT_EMAIL_SERVER_IP = "outlook.office365.com";
//	private static final String DEFAULT_EMAIL_SERVER_PORT = "993";
//	private static final String DEFAULT_EMAIL_SERVER_USER = "lloyd.chan@celera-markets.com";
//	private static final String DEFAULT_EMAIL_SERVER_PWD = "Ja9XuVDj";
    
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_AUTH_USER = "lloyd.khc.chan@gmail.com";
	private static final String SMTP_AUTH_PWD  = "Khc051405";
	
  public static void main(String args[]) throws Exception
  {
	  SendMailUsingAuthentication mail = new SendMailUsingAuthentication();
	  String subject = "testing";
	  String message = "testing";
	  String[] recipients = {"lloyd.chan@celera-markets.com"};
	  String from = "lloyd.chan@celera-markets.com";
	  mail.postMail(recipients, subject, message, from);
  }

  public void postMail( String recipients[ ], String subject,String message , String from) throws MessagingException
  {
    try {
        boolean debug = false;

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
//		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".auth", "true");
//		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".starttls.enable", "true");
//		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".host", DEFAULT_EMAIL_SERVER_IP);
//		props.put("mail." + DEFAULT_EMAIL_SERVER_PROTO + ".port", DEFAULT_EMAIL_SERVER_PORT);
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug);

        Message msg = new MimeMessage(session);

        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        msg.setSubject(subject);

        msg.setContent(message, "text/plain");

        Transport.send(msg);

    } 
    catch (Throwable e) 
    {
        e.printStackTrace();
    }
 }
/**
* SimpleAuthenticator is used to do simple authentication
* when the SMTP server requires it.
*/
private class SMTPAuthenticator extends javax.mail.Authenticator
{
    public PasswordAuthentication getPasswordAuthentication()
    {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
//        String username = DEFAULT_EMAIL_SERVER_USER;
//        String password = DEFAULT_EMAIL_SERVER_PWD;
        return new PasswordAuthentication(username, password);
    }
}

}