package com.uts.tradeconfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.celera.backoffice.BOData;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.uts.tradeconfo.InvoiceRegister;
import com.uts.tools.Uts2Dm;

import au.com.bytecode.opencsv.CSVReader;

public class UtsTradeConfoSummary {

	private static final String TRADECONFO_FILE = ResourceManager.getProperties(IResourceProperties.PROP_CMBOS_UTSTRADECONFO_CSV);
	
	public static Map<String, com.uts.tradeconfo.InvoiceRegister> map = new HashMap<String, InvoiceRegister>();
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");

	public static void loadSummary() {

	}

	public static void main(String[] args) throws IOException {

		// public static void main(String[] args) {
////		String csvFile = "Z://Celera Markets/Invoice/trade_confirmation(full)_v1.csv";
//		BufferedReader br = null;
//		String line = "";
//		String cvsSplitBy = ",";
		boolean isHeader = true;

		try {

			CSVReader reader = new CSVReader(new FileReader(TRADECONFO_FILE));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {

				if (nextLine.length == 0)
					continue;
				if (isHeader) {
					isHeader = false;
					continue;
				}
				
//				for (String s : nextLine) {
//					System.out.print(s + "," );
//				}
//				System.out.println("");
				String date = nextLine[2];
				String client = nextLine[3];
				String fees = nextLine[9];
				String fc = nextLine[10];
//				System.out.println(date + "," + baclient," + fees + "," + fc);
				// nextLine[] is an array of values from the line

				com.celera.mongo.entity.Account acc = BOData.get(client);
				if (acc == null) {
					System.out.println("=========no such client===========" + client);
				}
				else {
//					System.out.println(client + ": " + acc);
					InvoiceRegister ir = new InvoiceRegister(date, "CEL-160001", acc.getId(),fc, client, Uts2Dm.toDouble(fees));
					InvoiceRegister old = map.get(ir.key());
					if (old == null) {
						map.put(ir.key(), ir);
						System.out.println("=============new==========="+ir);
					}
					else {
						old.setAmount(ir.getAmount() + old.getAmount());
						System.out.println("=============update==========="+old);
					}
				}
			}
			
			for (InvoiceRegister l: map.values()) {
				System.out.println(l.toString());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
}
