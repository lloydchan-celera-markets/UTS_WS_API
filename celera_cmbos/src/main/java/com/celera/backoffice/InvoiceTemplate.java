package com.celera.backoffice;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.celera.backoffice.BOFormatter;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.mongo.entity.Invoice;
import com.celera.mongo.entity.TradeDetail;
import com.celera.mongo.entity.TradeDetails;
import com.celera.tools.CSVUtils;
//import com.celera.word.IWordConst;
import com.celera.tools.Doc2Pdf;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;

public class InvoiceTemplate {

	private static final String INVOICE_EXPORT_PATH = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_INVOICE_EXPORT_PATH);
	private static final String TEMPLATE_PATH = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_TEMPLATE_PATH);
	
	static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			CTVMerge vmerge = CTVMerge.Factory.newInstance();
			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				vmerge.setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				vmerge.setVal(STMerge.CONTINUE);
			}
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			// Try getting the TcPr. Not simply setting an new one every time.
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr != null) {
				tcPr.setVMerge(vmerge);
			} else {
				// only set an new TcPr if there is not one already
				tcPr = CTTcPr.Factory.newInstance();
				tcPr.setVMerge(vmerge);
				cell.getCTTc().setTcPr(tcPr);
			}
		}
	}

	static void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
		for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
			CTHMerge hmerge = CTHMerge.Factory.newInstance();
			if (colIndex == fromCol) {
				// The first merged cell is set with RESTART merge value
				hmerge.setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				hmerge.setVal(STMerge.CONTINUE);
			}
			XWPFTableCell cell = table.getRow(row).getCell(colIndex);
			// Try getting the TcPr. Not simply setting an new one every time.
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr != null) {
				tcPr.setHMerge(hmerge);
			} else {
				// only set an new TcPr if there is not one already
				tcPr = CTTcPr.Factory.newInstance();
				tcPr.setHMerge(hmerge);
				cell.getCTTc().setTcPr(tcPr);
			}
		}
	}

	public static void setRun(XWPFRun run, String fontFamily, int fontSize, String colorRGB, String text, boolean bold,
			boolean addBreak) {
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		run.setColor(colorRGB);
		run.setText(text);
		run.setBold(bold);
		if (addBreak)
			run.addBreak();
	}

//	public static void header(XWPFDocument document) {
//		// create table
//		XWPFTable table = document.createTable(5, 2);
//		table.getCTTbl().getTblPr().unsetTblBorders(); // unset outside border
//
//		XWPFParagraph paragraph = table.getRow(0).getCell(1).addParagraph();
//		setRun(paragraph.createRun(), "Calibre LIght", 12, "000000", "CELERA MARKETS LIMITED", true, false);
//		// table.getRow(0).getCell(1).setText("CELERA MARKETS LIMITED");
//		table.getRow(1).getCell(1).setText("11G-1, 51 Man Yue Street,");
//		table.getRow(2).getCell(1).setText("Hunghom, Kowloon, Hong Kong");
//		table.getRow(3).getCell(1).setText("Tel: +852 3746 3801 / 3746 3898");
//		table.getRow(4).getCell(1).setText("Email: accounting@celera-markets.com");
//
//		// create and set column widths for all columns in all rows
//		// most examples don't set the type of the CTTblWidth but this
//		// is necessary for working in all office versions
//		CTTblWidth tblWidth0 = CTTblWidth.Factory.newInstance();
//		tblWidth0.setW(BigInteger.valueOf(6000));
//		tblWidth0.setType(STTblWidth.DXA);
//
//		CTTblWidth tblWidth1 = CTTblWidth.Factory.newInstance();
//		tblWidth1.setW(BigInteger.valueOf(4000));
//		tblWidth1.setType(STTblWidth.DXA);
//
//		for (int col = 0; col < 2; col++) {
//			for (int row = 0; row < 5; row++) {
//				CTTcPr tcPr = table.getRow(row).getCell(col).getCTTc().getTcPr();
//				// if (tcPr != null) {
//				// tcPr.setTcW(tblWidth);
//				// } else {
//				tcPr = CTTcPr.Factory.newInstance();
//				if (col == 0)
//					tcPr.setTcW(tblWidth0);
//				else
//					tcPr.setTcW(tblWidth1);
//				table.getRow(row).getCell(col).getCTTc().setTcPr(tcPr);
//				// }
//			}
//		}
//
//		// using the merge methods
//		mergeCellVertically(table, 0, 0, 4);
//		// mergeCellVertically(table, 0, 1, 2);
//		// mergeCellHorizontally(table, 0, 0, 4);
//		// mergeCellHorizontally(table, 2, 1, 4);
//
//		// FileInputStream fis;
//		// try {
//		// fis = new FileInputStream("F://Temp/word/logo.png");
//		//
//		// // index = student.getImagePath().lastIndexOf('\\') + 1;
//		// // imageName = student.getImagePath().substring(index);
//		// XWPFParagraph cellParagraph =
//		// table.getRow(0).getCell(0).addParagraph();
//		// XWPFRun run = cellParagraph.createRun();
//		// run.addPicture(fis, XWPFDocument.PICTURE_TYPE_PNG, "logo.png",
//		// Units.toEMU(100), Units.toEMU(100));
//		// } catch (FileNotFoundException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// } catch (InvalidFormatException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// } catch (IOException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//	}
//
//	public static void billToHead(XWPFDocument document) {
//		// create table
//		XWPFTable table = document.createTable(1, 3);
//		table.getCTTbl().getTblPr().unsetTblBorders(); // unset outside border
//		// table.getRow(0).getCell(0).setText("Bill To");
//
//		XWPFParagraph paragraph = table.getRow(0).getCell(0).addParagraph();
//		setRun(paragraph.createRun(), "Times New Roman", 12, "000000", "Bill To", true, false);
//		// String hexColor = String.format("#%06X", Color.BLACK);
//		// System.out.println(hexColor);
//		table.getRow(0).getCell(0).setColor("001111");
//
//		CTTblWidth tblWidth0 = CTTblWidth.Factory.newInstance();
//		tblWidth0.setW(BigInteger.valueOf(6000));
//		tblWidth0.setType(STTblWidth.DXA);
//
//		CTTcPr tcPr = table.getRow(0).getCell(0).getCTTc().getTcPr();
//		tcPr = CTTcPr.Factory.newInstance();
//		tcPr.setTcW(tblWidth0);
//		table.getRow(0).getCell(0).getCTTc().setTcPr(tcPr);
//	}
//
//	public static void billToBody(XWPFDocument document) {
//		// create table
//		XWPFTable table = document.createTable(6, 2);
//		table.getCTTbl().getTblPr().unsetTblBorders(); // unset outside border
//
//		// XWPFParagraph paragraph = table.getRow(0).getCell(1).addParagraph();
//		// setRun(paragraph.createRun() , "Calibre LIght" , 12, "000000" ,
//		// "CELERA MARKETS LIMITED" , true, false);
//		table.getRow(0).getCell(0).setText("Nomura International Plc");
//		table.getRow(1).getCell(0).setText(
//				"7th Floor, Winchester, Hiranandani Business Park, Powai, Mumbai, Maharashtra 400 076, India.");
//		table.getRow(4).getCell(0).setText("Attn: Santosh Dange");
//
//		table.getRow(0).getCell(1).setText("Invoice #");
//		table.getRow(1).getCell(1).setText("Invoice Date: 01 Nov 2016");
//		table.getRow(2).getCell(1).setText("Account number: CEL0001");
//		table.getRow(4).getCell(1).setText("Due date: 28 October, 2016");
//		table.getRow(5).getCell(1).setText("Amount Due: 269971");
//
//		// create and set column widths for all columns in all rows
//		// most examples don't set the type of the CTTblWidth but this
//		// is necessary for working in all office versions
//		CTTblWidth tblWidth0 = CTTblWidth.Factory.newInstance();
//		tblWidth0.setW(BigInteger.valueOf(6000));
//		tblWidth0.setType(STTblWidth.DXA);
//
//		CTTblWidth tblWidth1 = CTTblWidth.Factory.newInstance();
//		tblWidth1.setW(BigInteger.valueOf(4000));
//		tblWidth1.setType(STTblWidth.DXA);
//
//		for (int col = 0; col < 2; col++) {
//			for (int row = 0; row < 5; row++) {
//				CTTcPr tcPr = table.getRow(row).getCell(col).getCTTc().getTcPr();
//				// if (tcPr != null) {
//				// tcPr.setTcW(tblWidth);
//				// } else {
//				tcPr = CTTcPr.Factory.newInstance();
//				if (col == 0)
//					tcPr.setTcW(tblWidth0);
//				else
//					tcPr.setTcW(tblWidth1);
//				table.getRow(row).getCell(col).getCTTc().setTcPr(tcPr);
//				// }
//			}
//		}
//
//		// using the merge methods
//		mergeCellVertically(table, 0, 1, 3);
//		// mergeCellVertically(table, 0, 1, 2);
//		// mergeCellHorizontally(table, 1, 0, 1);
//		// mergeCellHorizontally(table, 5, 0, 1);
//		// mergeCellHorizontally(table, 6, 0, 1);
//
//	}
//
//	public static void remit(XWPFDocument document) {
//		// create table
//		XWPFTable table = document.createTable(8, 1);
//		table.getCTTbl().getTblPr().unsetTblBorders(); // unset outside border
//
//		XWPFParagraph paragraph = table.getRow(0).getCell(0).addParagraph();
//		setRun(paragraph.createRun(), "Time News Roman", 12, "000000", "Please remit to", true, false);
//		table.getRow(0).getCell(0).setColor("000000");
//		table.getRow(1).getCell(0).setText("Bank Name: DBS Bank (Hong Kong) Limited");
//		table.getRow(2).getCell(0)
//				.setText("Bank Address: Floor 16, The Center, 99 Queen's Road Central, Central, Hong Kong");
//		table.getRow(3).getCell(0).setText("Bank Code: 016");
//		table.getRow(4).getCell(0).setText("Branch Code: 478");
//		table.getRow(5).getCell(0).setText("Account Number: 7883658530");
//		table.getRow(6).getCell(0).setText("Account Beneficiary: Celera Markets Limited");
//		table.getRow(7).getCell(0).setText("SWIFT: DHBKHKHH");
//
//		// create and set column widths for all columns in all rows
//		// most examples don't set the type of the CTTblWidth but this
//		// is necessary for working in all office versions
//		CTTblWidth tblWidth0 = CTTblWidth.Factory.newInstance();
//		tblWidth0.setW(BigInteger.valueOf(15000));
//		tblWidth0.setType(STTblWidth.DXA);
//
//		// for (int col = 0; col < 2; col++) {
//		for (int row = 0; row < 8; row++) {
//			CTTcPr tcPr = table.getRow(row).getCell(0).getCTTc().getTcPr();
//			// if (tcPr != null) {
//			// tcPr.setTcW(tblWidth);
//			// } else {
//			tcPr = CTTcPr.Factory.newInstance();
//			tcPr.setTcW(tblWidth0);
//			table.getRow(row).getCell(0).getCTTc().setTcPr(tcPr);
//			// }
//		}
//		// }
//		// using the merge methods
//		// mergeCellVertically(table, 0, 1, 3);
//		// mergeCellHorizontally(table, 6, 0, 1);
//
//	}

	public static void setCell(XWPFTableCell cell, String text) {
		for (XWPFParagraph p : cell.getParagraphs()) {
			for (XWPFRun r : p.getRuns()) {
				r.setText(text, 0);
				return;
			}
		}
	}

	public static void clearCell(XWPFTableCell cell) {
		boolean start = true;
		for (XWPFParagraph p : cell.getParagraphs()) {
			for (XWPFRun r : p.getRuns()) {
				r.setText("", 0);
			}
		}
	}

	public static void fillCell(XWPFTableCell c, String token, TradeDetail td, int id) {
		if (token.equals("Xxxxref" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getReference());
		} else if (token.equals("Xxxxdate" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getDate());
		} else if (token.equals("Xxxxid" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getTradeId());
		} else if (token.equals("Xxxxdesc" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getDescription());
		} else if (token.equals("Xxxxsize" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getSize());
		} else if (token.equals("Xxxxhedge" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getHedge());
		} else if (token.equals("Xxxxfee" + id)) {
			System.out.println("equals " + token);
			clearCell(c);
			setCell(c, td.getFee());
		}
	}

//	public static void fillTradeDetails(XWPFDocument doc, Invoice inv) {
//		List<TradeDetail> list_td = inv.getTradeDetails().getTradeDetail();
//		// int a = 97; char count;
//System.out.println("==========size==========="+list_td.size());		
//		int count = 0;
//		for (int i = 0; i < list_td.size(); i++) {
//			TradeDetail td = list_td.get(i);
//			count = i + 1;
//			// count = (char)a;
//			// a++;
//
//			boolean isTargetTable = false;
//			for (XWPFTable tbl : doc.getTables()) {
//
//				for (XWPFTableRow row : tbl.getRows()) {
//					for (XWPFTableCell cell : row.getTableCells()) {
//						for (XWPFParagraph p : cell.getParagraphs()) {
//							// if (cell.getParagraphs().size() > 1)
//							// System.out.println(">1");
//							String token = "";
//							for (XWPFRun r : p.getRuns()) {
//								String text = r.getText(0);
//								token += text;
//							}
//							if (token.equals("Xxxxdate1"))
//								isTargetTable = true;
//
//							fillCell(cell, token, td, count);
////							System.out.println("============token========" + token);
//						}
//					}
//				}
//				// there expect only one row
//			}
//		}
//	}

	public static void fillTradeDetails2(XWPFDocument doc, Invoice inv) {
		List<TradeDetail> list_td = inv.getTradeDetails().getTradeDetail();
		// int a = 97; char count;
		int count = 0;
		TradeDetail td = list_td.get(0);
		// count = i + 1;
		// count = (char)a;
		// a++;
		int len = list_td.size();

		boolean isTargetTable = false;
		for (XWPFTable tbl : doc.getTables()) {
			if (!isTargetTable) {
				for (XWPFTableRow row : tbl.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph p : cell.getParagraphs()) {
							// if (cell.getParagraphs().size() > 1)
							// System.out.println(">1");
							String token = "";
							for (XWPFRun r : p.getRuns()) {
								String text = r.getText(0);
								token += text;
							}
							if (token.equals("Trade Date"))
							{
								isTargetTable = true;
//								System.out.println("============token========" + token);
							}
	
	//						fillCell(cell, token, td, count);
	
						}
					}
				}
				if (isTargetTable) {
					// there expect only one row
					for (int i = 0; i < len; i++) {
						td = list_td.get(i);
						XWPFTableRow tableOneRowTwo = tbl.createRow();
						tableOneRowTwo.getCell(0).setText(td.getDate());
						tableOneRowTwo.getCell(1).setText(td.getTradeId());
						tableOneRowTwo.getCell(2).setText(td.getDescription());
						tableOneRowTwo.getCell(3).setText(td.getSize());
						tableOneRowTwo.getCell(4).setText(td.getHedge());
						tableOneRowTwo.getCell(5).setText(td.getReference());
						tableOneRowTwo.getCell(6).setText(td.getFee());
					}
				}
			}
		}
	}

	// public void wordDocProcessor(AnotherVO anotherData, ArrayList<String>
	// list, String sourse, String destination)
	public static void wordDocProcessor(Invoice inv, String curncy, String mmmyy)
			throws IOException, InvalidFormatException, ParseException {
		Date d = sdf_mmm_yy.parse(mmmyy);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_MONTH, 9);
		cal.add(Calendar.MONTH, 1);
//		String invdate = sdf_dd_MMMM_yy.format(cal.getTime());
		String fileMonth = sdf_mm_yy.format(cal.getTime());

		cal.add(Calendar.MONTH, 1);
//		String invduedate = sdf_dd_MMMM_yy.format(cal.getTime());

		String mmmm_yyyy = sdf_mmmm_yyyyyy.format(d);

		TradeDetails td = inv.getTradeDetails();

		String attn = "Account Payable";
		if (inv.getAttn() != null && inv.getAttn().length() > 0) {
			attn = inv.getAttn();
		}

		// XWPFDocument doc = new XWPFDocument(OPCPackage.open(sourse +
		// "XXXXX_TestReport_URL_Document.doc"));
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(TEMPLATE_PATH + File.separator + "template.docx"));
		// XWPFDocument doc = new XWPFDocument("F://Temp/word/template.docx");
		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
// System.out.println("text=" + text);

							if (text != null && text.contains("$company")) {
								// System.out.println("$company found");
								text = text.replace("$company", inv.getCompany());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$address")) {
								// System.out.println("$address found");
								text = text.replace("$address", inv.getAddress());
								r.setText(text, 0);
							} else if (text != null && text.contains("$address")) {
								// System.out.println("$address found");
								text = text.replace("$address", inv.getAddress());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$attentionto")) {
								// System.out.println("$attentionto found");
								text = text.replace("$attentionto", "Attn: " + attn);
								r.setText(text, 0);
							} else if (text != null && text.contains("attentionto")) {
								// System.out.println("$attentionto found");
								text = text.replace("attentionto", "Attn: " + attn);
								r.setText(text, 0);
							}
							if (text != null && text.contains("$emails")) {
								// System.out.println("$emails found");
								text = text.replace("$emails", inv.getSentTo());
								r.setText(text, 0);
							}
							else if (text != null && text.contains("emails")) {
								// System.out.println("$emails found");
								text = text.replace("emails", inv.getSentTo());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$myaccount")) {
								// System.out.println("$myaccount found");
								text = text.replace("$myaccount", "Account number: " + inv.getAccount_number());
								r.setText(text, 0);
							} else if (text != null && text.contains("myaccount")) {
								// System.out.println("$myaccount found");
								text = text.replace("myaccount", "Account number: " + inv.getAccount_number());
								r.setText(text, 0);
							}
							if (text != null && text.startsWith("$dueamount")) {
								// System.out.println("$dueamount found");
								text = text.replace("$dueamount", inv.getAmount_due());
								r.setText(text, 0);
							}
							else if (text != null && text.startsWith("dueamount")) {
								// System.out.println("$dueamount found");
								text = text.replace("dueamount", inv.getAmount_due());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$amountdue")) {
								// System.out.println("$amountdue found");
								text = text.replace("$amountdue", "Amount Due: " + inv.getAmount_due());
								r.setText(text, 0);
							} else if (text != null && text.contains("amountdue")) {
								// System.out.println("$amountdue found");
								text = text.replace("amountdue", "Amount Due: " + inv.getAmount_due());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$mmmmyyyy")) {
								// System.out.println("$mmmmyyyy found");
								text = text.replace("$mmmmyyyy", mmmm_yyyy);
								r.setText(text, 0);
							} else if (text != null && text.contains("mmmmyyyy")) {
								// System.out.println("mmmmyyyy found");
								text = text.replace("mmmmyyyy", mmmm_yyyy);
								r.setText(text, 0);
							}
							if (text != null && text.contains("$invdate")) {
								// System.out.println("$invdate found");
								text = text.replace("$invdate", inv.getInvoice_date());
								r.setText(text, 0);
							} else if (text != null && text.contains("invdate")) {
								// System.out.println("$invdate found");
								text = text.replace("invdate", inv.getInvoice_date());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$invduedate")) {
								// System.out.println("$invduedate found");
								text = text.replace("$invduedate", inv.getDue_date());
								r.setText(text, 0);
							} else if (text != null && text.contains("invduedate")) {
								// System.out.println("$invduedate found");
								text = text.replace("invduedate", inv.getDue_date());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$invnum")) {
//								 System.out.println("invnum found");
								text = text.replace("$invnum", inv.getInvoice_number());
								r.setText(text, 0);
							} else if (text != null && text.contains("invnum")) {
//								 System.out.println("invnum found");
								text = text.replace("invnum", inv.getInvoice_number());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$tdmonth")) {
								// System.out.println("$tdmonth found");
								text = text.replace("$tdmonth", mmmm_yyyy);
								r.setText(text, 0);
							}
							else if (text != null && text.contains("tdmonth")) {
								// System.out.println("$tdmonth found");
								text = text.replace("tdmonth", mmmm_yyyy);
								r.setText(text, 0);
							}
							if (text != null && text.contains("$totfee")) {
								// System.out.println("$totfee found");
								text = text.replace("$totfee", td.getTotal_fee());
								r.setText(text, 0);
							} else if (text != null && text.contains("totfee")) {
								// System.out.println("$totfee found");
								text = text.replace("totfee", td.getTotal_fee());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$tothedge")) {
								// System.out.println("$tothedge found");
								text = text.replace("$tothedge", td.getHedge());
								r.setText(text, 0);
							}
							else if (text != null && text.contains("tothedge")) {
								// System.out.println("$tothedge found");
								text = text.replace("tothedge", td.getHedge());
								r.setText(text, 0);
							}
							if (text != null && text.contains("$totsize")) {
								// System.out.println("$totsize found");
								text = text.replace("$totsize", td.getSize());
								r.setText(text, 0);
							}
							else if (text != null && text.contains("totsize")) {
								// System.out.println("$totsize found");
								text = text.replace("totsize", td.getSize());
								r.setText(text, 0);
							}
							if (text != null && text.equals("$")) {
								// System.out.println("$ found");
								text = text.replace("$", "");
								r.setText(text, 0);
							}
						}
					}
				}
			}
		}

		fillTradeDetails2(doc, inv);

		// doc.write(new FileOutputStream(destination + list.get(0) +
		// "_TestReport_URL_Document.doc"));
		String company = inv.getCompany();
		company = company.replaceAll(".?\\*+.?", "");
		FileOutputStream os = null;
		String file = INVOICE_EXPORT_PATH + File.separator + company + "_" + curncy + "_" + fileMonth + ".docx";
		inv.setFile(file);
		
		try {
			os = new FileOutputStream(file);
			doc.write(os);
		} catch (Exception e) {
			System.out.println("file=" + file);
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.flush();
				} catch (Exception e) {
				}
				;
				try {
					os.close();
				} catch (Exception e) {
				}
				;
			}
		}
		
		
		if (inv.getInvoice_number() == null || inv.getInvoice_number().length() == 0) {}
		else {
//			System.out.println("=============Doc2Pdf.doGenerate=============="+inv.getInvoice_number());
			Doc2Pdf.doGenerate(file);
		}
	}

	private static SimpleDateFormat sdf_mmm_yy = new SimpleDateFormat("MMMyy");
	private static SimpleDateFormat sdf_mm_yy = new SimpleDateFormat("MMyy");
	private static SimpleDateFormat sdf_mmmm_yyyyyy = new SimpleDateFormat("MMMM yyyy");
	private static SimpleDateFormat sdf_dd_MMMM_yy = new SimpleDateFormat("dd MMMM, yyyy");

	public static void csvProcessor(TradeDetails tds, String company, String curncy, String mmmyy)
			throws IOException, InvalidFormatException {

		FileWriter writer = null;

		try {
			Date d = sdf_mmm_yy.parse(mmmyy);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_MONTH, 9);
			cal.add(Calendar.MONTH, 1);
			String invdate = sdf_dd_MMMM_yy.format(cal.getTime());
			String fileMonth = sdf_mmm_yy.format(cal.getTime());

			String mmmm_yyyy = sdf_mmmm_yyyyyy.format(d);

			company = company.replaceAll(".?\\*+.?", "");
			String csvFile = "F://Temp/word/invoices/" + company + "_" + curncy + "_" + fileMonth + ".csv";

			writer = new FileWriter(csvFile);
			CSVUtils.writeLine(writer, Arrays.asList("", "", "MONTHLY DETAILS - " + mmmm_yyyy, "", "", "", ""));
			CSVUtils.writeLine(writer, Arrays.asList("", "Client Name:", company, "", "", "", ""));
			CSVUtils.writeLine(writer, Arrays.asList("Trade Date", "Trade ID", "Description", "SIZE", "Hedge",
					"Reference", "Brokerage Fee"));

			for (TradeDetail td : tds.getTradeDetail()) {
				System.out.print(td);
				CSVUtils.writeLine(writer, td.toCsv());
			}

			CSVUtils.writeLine(writer, Arrays.asList("", "", "", "", "", "", ""));
			CSVUtils.writeLine(writer, Arrays.asList("", "", "", "", "", "", ""));
			CSVUtils.writeLine(writer,
					Arrays.asList("", "", mmmm_yyyy + " Total", tds.getSize(), tds.getHedge(), "", tds.getTotal_fee()));

			// double-quotes
			// CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc\"c"));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.flush();
				} catch (Exception e) {
				}
				;
				try {
					writer.close();
				} catch (Exception e) {
				}
				;

			}
		}
	}

	public static void main1(String[] args) throws Exception {
//
//		XWPFDocument document = new XWPFDocument();
//
//		XWPFParagraph paragraph = document.createParagraph();
//		XWPFRun run = paragraph.createRun();
//		// run.setText("The table:");
//
//		header(document);
//		document.createParagraph().createRun().addBreak();
//
//		billToHead(document);
//		document.createParagraph().createRun().addBreak();
//		billToBody(document);
//		document.createParagraph().createRun().addBreak();
//		remit(document);
//
//		// paragraph = document.createParagraph();
//
//		FileOutputStream out = new FileOutputStream(IWordConst.PATH + "create_table1.docx");
//		document.write(out);
//
//		System.out.println("create_table1.docx written successully");
	}
}