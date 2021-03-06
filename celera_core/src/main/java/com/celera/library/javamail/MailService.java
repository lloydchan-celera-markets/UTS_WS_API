package com.celera.library.javamail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.mongo.entity.IMongoDocument;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.SortTerm;

public class MailService implements IMailService
{
	final static Logger logger = LoggerFactory.getLogger(MailService.class);
	
	private final String protocol;
	private final String host;
	private final String port;
	private final String userName;
	private final String password;

	private final Session session;

	private IMailListener cb;

	public MailService(String protocol, String host, String port, String userName, String password)
	{
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;

		Properties properties = getServerProperties(protocol, host, port);
		session = Session.getDefaultInstance(properties);
	}

	@Override
	public String toString()
	{
		return "MailService [protocol=" + protocol + ", host=" + host + ", port=" + port + ", userName=" + userName
				+ ", password=" + password + ", session=" + session + "]";
	}

	private Properties getServerProperties(String protocol, String host, String port)
	{
		Properties properties = new Properties();
		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);
		properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

		return properties;
	}

	/* 
	 * only work for POP3
	 */
	public List<IMongoDocument> getBetween(Date somePastDate, Date someFutureDate)
	{
		List<IMongoDocument>  list = new ArrayList<IMongoDocument>();
		
		SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT, someFutureDate);
		SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, somePastDate);
		SearchTerm andTerm = new AndTerm(olderThan, newerThan);

		Store store;
		try
		{
			store = session.getStore(protocol);

			store.connect(userName, password);

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			inbox.search(andTerm);

			int count = inbox.getMessageCount();
			Message[] messages = inbox.getMessages(1, count);
			System.out.println("number of email in inbox: " + count);

			for (Message message : messages)
			{
				cb.onEmail(message, list);
			}

			inbox.close(false);
			store.close();
		}
		catch (NoSuchProviderException ex)
		{
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		}
		catch (MessagingException ex)
		{
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
		
		return list;
	}

	public List<IMongoDocument> getAllFromInbox()
	{
		List<IMongoDocument> list = new ArrayList<IMongoDocument>();
		try
		{
			Store store = session.getStore(protocol);
			store.connect(userName, password);

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			int count = inbox.getMessageCount();
			logger.info("Inbox emails {}", count);
			Message[] messages = inbox.getMessages();
			Arrays.sort(messages, new Comparator<Message>() {
			    @Override
			    public int compare(Message o1, Message o2) {
			        try
					{
						return o1.getSentDate().compareTo(o2.getSentDate());
					}
					catch (MessagingException e)
					{
						e.printStackTrace();
					}
			        return 0;
			    }
			});
			
			for (int i=0; i<messages.length; i++)
			{
				Message message = messages[i];
//logger.info("{}", i);
				cb.onEmail(message, list);
			}

			inbox.close(false);
			store.close();
		}
		catch (NoSuchProviderException ex)
		{
			logger.error("No provider for protocol {}", protocol, ex);
		}
		catch (MessagingException ex)
		{
			logger.error("Could not connect to the message store {}", protocol, ex);
		}
		
		return list;
	}

	@Override
	public void setListener(IMailListener cb)
	{
		this.cb = cb;
	}
}
