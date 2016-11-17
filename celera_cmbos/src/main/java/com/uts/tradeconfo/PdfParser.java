package com.uts.tradeconfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PdfParser {
	
	private static final String CHARSET_UTF8 = "UTF-8";
	
	private static final String DEFAULT_EMAIL_SERVER_PROTO = "imap";
	private static final String DEFAULT_EMAIL_SERVER_IP = "outlook.office365.com";
	private static final String DEFAULT_EMAIL_SERVER_PORT = "993";
	private static final String DEFAULT_EMAIL_SERVER_USER = "Lloyd.Chan@celera-markets.com";
	private static final String DEFAULT_EMAIL_SERVER_PWD = "Ja9XuVDj";
	
	private static final String protocol = DEFAULT_EMAIL_SERVER_PROTO;
	private static final String host = DEFAULT_EMAIL_SERVER_IP;
	private static final String port = DEFAULT_EMAIL_SERVER_PORT;
	private static final String userName = DEFAULT_EMAIL_SERVER_USER;
	private static final String password = DEFAULT_EMAIL_SERVER_PWD;
	private static final String PREFIX_EMAIL_SUBJECT = "Trade Confirmation";
	
	public static final List<UtsTradeConfoDetail> _list = new ArrayList<UtsTradeConfoDetail>();
	
	public void readFile(String path)
	{
		final File folder = new File(path);
		listFilesForFolder(folder);
	}
	
	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				String fileName = fileEntry.getName();
				System.out.println(fileName);
				try {
					if (fileName.endsWith("pdf"))
						parsePdf(fileEntry);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void parsePdf(File file) throws IOException, MessagingException
	{
		InputStream is = new FileInputStream(file);
		PdfReader reader = new PdfReader(is);

		for (int page = 1; page <= 1; page++)
		{
			try 
			{
				byte[] b = PdfTextExtractor.getTextFromPage(reader, page, new MyTextExtractionStrategy())
						.getBytes(CHARSET_UTF8);
				// byte[] b1 = reader.getPageContent(0);
				// for (int i =0; i<b.length; i++) {
				// System.out.print(b[i] + ",");
				// }
	
				String sPdf = new String(b);
				// System.out.println(sPdf);
				UtsTradeConfoDetail t = new UtsTradeConfoDetail();
				t.parsePdf(sPdf);
				t.setFile(file.getAbsolutePath());
				_list.add(t);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
//	public static void main(String[] arg)
//	{
//		PdfParser t = new PdfParser();
////		t.getAllFromInbox();
//		t.readFile();
//	}
	
}
