package com.celera.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Doc2Pdf
{

	public static final String PATH = "F://Temp/word/invoices";

	public static void listFilesForFolder(final File folder)
	{
		for (final File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				listFilesForFolder(fileEntry);
			} else
			{
				try
				{
					doGenerate(fileEntry);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				//// System.out.println(fileEntry.getName());
				// System.out.println(fileEntry.getAbsolutePath());
				//// System.out.println(fileEntry.getPath());
				// String fileOutName = fileEntry.getAbsolutePath();
				// fileOutName = fileOutName.replaceAll("docx", "pdf");
				// System.out.println(fileOutName);
			}
		}
	}

	public static void doGenerate(File fileEntry) throws IOException
	{
		String root = "target";
		// String fileOutName = root + "/" + fileInName + ".pdf";
		// String fileOutName = PATH + "/" + fileEntry.getName();
		String fileOutName = fileEntry.getAbsolutePath();
		fileOutName = fileOutName.replaceAll("docx", "pdf");
		long startTime = System.currentTimeMillis();

		InputStream is = new FileInputStream(fileEntry);
		XWPFDocument document = new XWPFDocument(is);
		// XWPFDocument document = new
		// XWPFDocument(AbstractXWPFPOIConverterTest.class.getResourceAsStream(fileInName));
		OutputStream out = new FileOutputStream(new File(fileOutName));
		PdfOptions options = PdfOptions.create().fontEncoding("windows-1250");
		PdfConverter.getInstance().convert(document, out, options);
		System.out.println("Generate " + fileOutName + " with " + (System.currentTimeMillis() - startTime) + " ms.");
	}

//	public static void main(String[] args)
//	{
//		final File folder = new File("F://Temp/word/invoices");
//		listFilesForFolder(folder);
//	}

	public static void doGenerate(String file)
	{
		File fileEntry = new File(file);
		String root = "target";
		// String fileOutName = root + "/" + fileInName + ".pdf";
		// String fileOutName = PATH + "/" + fileEntry.getName();
		String fileOutName = fileEntry.getAbsolutePath();
		fileOutName = fileOutName.replaceAll("docx", "pdf");
		long startTime = System.currentTimeMillis();

		InputStream is;
		try
		{
			is = new FileInputStream(fileEntry);

			XWPFDocument document = new XWPFDocument(is);
			// XWPFDocument document = new
			// XWPFDocument(AbstractXWPFPOIConverterTest.class.getResourceAsStream(fileInName));
			OutputStream out = new FileOutputStream(new File(fileOutName));
			PdfOptions options = PdfOptions.create().fontEncoding("windows-1250");
			PdfConverter.getInstance().convert(document, out, options);
			System.out
					.println("Generate " + fileOutName + " with " + (System.currentTimeMillis() - startTime) + " ms.");

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (XWPFConverterException e)
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
