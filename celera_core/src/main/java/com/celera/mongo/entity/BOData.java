package com.celera.mongo.entity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class BOData
{
	public static Map<String, Account> map = new ConcurrentHashMap<String, Account>();

	static {



		
		Account o1 = new Account("CEL0001", "Nomura International Plc",
				"7th Floor, Winchester, Hiranandani Business Park, Powai, Mumbai, Maharashtra 400 076, India.",
				"santosh.dange@nomura.com", "Santosh Dange", "Nomura International Plc");
		
		Account o2a = new Account("CEL0002", "Deutsche Bank AG London Branch",
				"Velankani Tech Park, No 43, 3rd Floor, Block 5, Electronic City, Phase 1, Hosur Road, Bangalore, 560100, India",
				"futures.brokerage@db.com,singh.gagandeep@db.com,mohan.raj-mc@db.com", "Futures Brokerage", null);
		Account o4a = new Account("CEL0003", "Optiver Australia Pty Ltd", "39 Hunter St., Sydney NSW 2000, Australia.",
				"MiddleOfficeTeam@Optiver.com.au", "Brian Taylor", null);
		Account o5a = new Account("CEL0004", "Vivienne Court Trading Pty Ltd",
				"Suite 304, 24-30 Springfield Avenue Potts Point, Sydney, NSW 2011, Australia",
				"midoffice@vivcourt.com", "Helen Robertson", null);
		Account o6a = new Account("CEL0005", "Liquid Capital Australia Pty Ltd.", "United Kindom",
				"brokerage@liquidcapital.com", "Robert Wickham", null);
		Account o7 = new Account("CEL0006", "J.P. Morgan Securities PLC",
				"Futures & Options House Reconcillations, One@Changi City 7th Floor,  1 Changi Business Park Central 1, Singapore, 486036",
				"edg_asia_index_flow_trader_confos@jpmorgan.com , brokerage.control@jpmorgan.com", "F & O House team", null);
		Account o8a = new Account("CEL0007", "UBS AG London Branch", "1 Broadgate, London, ECEM 2BS, United Kingdom",
				"SH-UBS-invoices@ubs.com", "SH-UBS-invoices@ubs.com", null);
		Account o9 = new Account("CEL0008", "Eclipse Futures (HK) Limited",
				"Room 3001, 30/F Subi Okaza, 225-257 Gloucester Road, Causeway Bay, Hong Kong",
				"kechan@eclipseoptions.com, invoices@eclipseoptions.com, accounts@eclipseoptions.com", "Kelvin Chan", null);
		Account o10a = new Account("CEL0009", "Citigroup Global Markets Hong Kong Limited",
				"ICG Global Expense Management Team, Citigroup Centre 1, 5th Floor, 33 Canada Square, Canary Wharf, London, E14 5LB, UK",
				"Brokerage.ciplc@citi.com", "London Billing Email", null);
		Account o11 = new Account("CEL0010", "Royal Bank of Canada",
				"71 Queen Victoria Street, London, EC4V 4DE, United Kingdom", "", "Mark Nunns", null);
		Account o12a = new Account("CEL0011", "Korea Investment & Securities Co Ltd",
				"19/F, Trading Center, 27-1, Yeouido-dong, Yeongdeungpo-gu, Seoul, -150-747, Korea",
				"Trading_op@truefriend.com, onlyone@truefriend.com", "Hanna Kim", null);
		Account o13 = new Account("CEL0012", "Liquid Capital Hong Kong Limited",
				"21st Floor, The Center, 99 Queens Road, Central, Hong Kong", "brokerage@liquidcapital.com",
				"Anthony Webb", null);
		Account o14 = new Account("CEL0013", "Celera Financial Limited", "11G, 51 Man Yue Street, Hunghom, Hong Kong",
				"cflbrokerage@celera-group.com", "Account Payable", null);
		Account o15 = new Account("CEL0014", "BNP Paribas Arbitrage S.N.C.*", "",
				"bnparbitrage.brokerfees@bnpparibas.com,asia_edo_fees@asia.bnpparibas.com", "Account Payable", null);
		Account o16 = new Account("CEL0015", "Morgan Stanley & Co. International PLC",
				" 25 Cabot Square,  Canary Wharf, London, E14 4QA", "Brokerage.Payables@morganstanley.com;Attila.Pecz@morganstanley.com"
				, "Account Payable", null);
//		Account o17 = new Account("CEL0016", "BNP Paribas Paris", "", "MARINE.MALAGNOUX@UK.BNPPARIBAS.COM ",
//				"Account Payable");
		Account o18 = new Account("CEL0017", "BAFM", "", "derk.chan@bfam-partners.com,BFAM-MO@bfam-partners.com", "", "Thierry");
		byte[] bytes = {83, 111, 99, 105, -17, -65, -67, 116, -17, -65, -67, 32, 71, -17, -65, -67, 110, -17, -65, -67, 114, 97, 108, 101, 32, 40, 72, 75, 41, 32, 76, 105, 109, 105, 116, 101, 100};
		Account o19a = new Account("CEL0018", new String(bytes), "", "FREDERIC.OLIVE@SGCIB.COM ",
				"Account Payable", "Société Générale (HK) Limited");
		map.put(o19a.key(), o19a);
		Account o19 = new Account("CEL0018", "Société Générale (HK) Limited", "", "FREDERIC.OLIVE@SGCIB.COM ",
				"Account Payable", "Société Générale (HK) Limited");
		Account o20 = new Account("CEL0019", "Mirae Asset Securities Co. Ltd", "", "mirae-otc-settle@miraeasset.com ",
				"Account Payable", null);
		Account o21 = new Account("CEL0020", "Hyundai Securities", "", 
				"taehee.kim@hyundaisec.com;syshin09@stockmarket.co.kr;sh.ji@hyundaisec.com;rock79@hyundaisec.com;kdbghdru@youfirst.co.kr;juhyun.jeong@hyundaisec.com;jm.yang@hyundaisec.com;hjkim@stockmarket.co.kr;giantste@stockmarket.co.kr;chosj@stockmarket.co.kr;chcsuwon@hyundaisec.com;20694605@hyundai-securities.co.kr;chahn@hyundaisec.com;eqd@hyundaisec.com;"
				, "Account Payable", null);
		Account o22 = new Account("CEL0021", "Barclays Bank PLC", "", "gupdocs@barclayscapital.com ", "Account Payable", null);
		Account o23 = new Account("CEL0022", "Daishin Securities Co., Ltd.", "",
				"leejw81@daishin.com;songjy@daishin.com", "Account Payable", null);
		Account o24 = new Account("CEL0023", "HMC Investment", "",
				"0401176@hmcib.com;justisoo@hmcib.com;0800574@hmcib.com", "Account Payable", null);
		Account o25a = new Account("CEL0024", "NH Investment & Securities Co Ltd", "", "woorisec.opr@nhqv.com;inho@nhqv.com;hyejin@nhqv.com;chris84831@nhqv.com",
				"Account Payable", null);
		Account o26 = new Account("CEL0025", "Yuanta Securities Korea Co., Ltd", "", 
				"ahyoung.joo@yuantakorea.com;jaeyoung.cha@yuantakorea.com;jungkun.park@yuantakorea.com;jinwee.jeon@yuantakorea.com;jechan.oh@yuantakorea.com",
				"Account Payable", null);
		Account o27a = new Account("CEL0026", "Samsung Securities Co., Ltd", "", "els.ss@samsung.com",
				"Account Payable", null);
		Account o28 = new Account("CEL0027", "IBK Securities Co. Ltd", "", "shaun.s.chang@ibks.com", "Account Payable", null);
		Account o29 = new Account("CEL0028", "Shinhan Investment Corp.", "", "edflow@shinhan.com;boah@shinhan.com", "Account Payable", null);
		Account o33 = new Account("CEL0030", "Thierry", "", "", "Account Payable", "Thierry");
		Account o33a = new Account("CEL0030", "Celera Bank Private Test 1", "", "", "Account Payable", "Thierry");
		map.put(o33a.key(), o33a);
		Account o33b = new Account("CEL0030", "Emmanuel Slezack", "", "", "Account Payable", "Thierry");
		map.put(o33b.key(), o33b);
		Account o31a = new Account("CEL0031", "Haitong International Securities Company Limited", "", "settdep@htisec.com", "Account Payable", null);
		map.put(o31a.key(), o31a);
		Account o32 = new Account("CEL0032", "Eugene Investment & future ", "", "joonke@eugenefn.com ", "Account Payable", null);
		map.put(o32.key(), o32);
		Account o34 = new Account("CEL0034", "Dongbu Securities", "", "settlement@dongbuhappy.com", "Account Payable", null);
		map.put(o34.key(), o34);
		Account o35 = new Account("CEL0035", "Hanwha Investment & Securities", "",
				"201101141@hanwha.com;gidong.park@hanwha.com;shlee2016@hanwha.com", "Account Payable", null);
		map.put(o35.key(), o35);
		Account o36 = new Account("CEL0036", "Hana Financial Investment", "",
				"global@hanafn.com;chea@hanafn.com;haewook.kim@hanafn.com;equitytrading@hanafn.com;tjsgp02@hanafn.com;jsbang@hanafn.com;leemr@hanafn.com;lhc3@hanafn.com;cgmin@hanafn.com", 
				"Account Payable", null);
		Account o37 = new Account("CEL0037", "IMC Pacific Pty Ltd", "", "Hannah.Chan@imc.com", "Account Payable", null);
		map.put(o37.key(), o37);
		Account o38 = new Account("CEL0038", "Kiwoom Securities", "", "jckim@kiwoom.com;deri@kiwoom.com;gosmy@kiwoom.com", "Account Payable", null);
		map.put(o38.key(), o38);
		Account o39 = new Account("CEL0039", "Banco Bilbao Vizcaya Argentaria, S.A.", "", "brokerage.billing@bbva.com,fjvillanueva@grupobbva.com", "Account Payable", null);
		map.put(o39.key(), o39);
		Account o40 = new Account("CEL0040", "Kyobo Securities Co., Ltd", "", "30000329@iprovest.com;kyoboder@iprovest.com", "Account Payable", null);
		map.put(o40.key(), o40);
		Account o41 = new Account("CEL0041", "Woori Bank", "",
				"BO_EQD@wooribank.com;bs.min@wooribank.com;twjeon@wooribank.com;wspark2682@bloomberg.net",
				"Account Payable", null);
		map.put(o41.key(), o41);
		Account o42 = new Account("CEL0042", "Shinhan Bank", "",
				"shinhanfe@shinhan.com;settle@shinhan.com;jhyeon@shinhan.com;jjs2434@shinhan.com", "Account Payable",
				null);
		map.put(o42.key(), o42);
		Account o43 = new Account("CEL0043", "Merrill Lynch International*", "", "", "Account Payable", "Merrill Lynch International");
		map.put(o43.key(), o43);
		Account o44 = new Account("CEL0043", "Merrill Lynch International", "", "", "Account Payable", "Merrill Lynch International");
		map.put(o44.key(), o44);
		Account o45 = new Account("CEL0044", "KB Securities Co.", "", "", "Account Payable", null);
		map.put(o45.key(), o45);
		map.put(o36.key(), o36);





//		Account o30 = new Account("CEL0029", "", "", "", "");
//		map.put(o30.key(), o30);



//		Account o31 = new Account("CEL0031", "Haitong  International ", "", "settdep@htisec.com", "Account Payable");
//		map.put(o31.key(), o31);


		map.put(o1.key(), o1);
//		map.put(o2.key(), o2);
		map.put(o2a.key(), o2a);
//		map.put(o2b.key(), o2b);
//		map.put(o2c.key(), o2c);
//		map.put(o2d.key(), o2d);
//		map.put(o4.key(), o4);
		map.put(o4a.key(), o4a);
//		map.put(o5.key(), o5);
		map.put(o5a.key(), o5a);
//		map.put(o6.key(), o6);
		map.put(o6a.key(), o6a);
		map.put(o7.key(), o7);
//		map.put(o8.key(), o8);
		map.put(o8a.key(), o8a);
		map.put(o9.key(), o9);
//		map.put(o10.key(), o10);
		map.put(o10a.key(), o10a);
		map.put(o11.key(), o11);
//		map.put(o12.key(), o12);
		map.put(o12a.key(), o12a);
		map.put(o13.key(), o13);
		map.put(o14.key(), o14);
		map.put(o15.key(), o15);
		map.put(o16.key(), o16);
//		map.put(o17.key(), o17);
		map.put(o18.key(), o18);
		map.put(o19.key(), o19);
		map.put(o20.key(), o20);
		map.put(o21.key(), o21);
		map.put(o22.key(), o22);
		map.put(o23.key(), o23);
		map.put(o24.key(), o24);
//		map.put(o25.key(), o25);
		map.put(o25a.key(), o25a);
		map.put(o26.key(), o26);
//		map.put(o26a.key(), o26a);
//		map.put(o27.key(), o27);
		map.put(o27a.key(), o27a);
		map.put(o28.key(), o28);
		map.put(o29.key(), o29);
		map.put(o33.key(), o33);

	}

	public static Account get(String company) {
		return map.get(company.toUpperCase());
	}

	public static Account getId(String id) {
		for (Account acc: map.values()) {
			if (acc.getId().equals(id))
				return acc;
		}
		return null;
	}
	
	public static void main(String[] args) {
		for (byte b : "Société Générale (HK) Limited".getBytes()) {
			System.out.print(b + ",");
		}
		
		byte[] bytes = {83, 111, 99, 105, -17, -65, -67, 116, -17, -65, -67, 32, 71, -17, -65, -67, 110, -17, -65, -67, 114, 97, 108, 101, 32, 40, 72, 75, 41, 32, 76, 105, 109, 105, 116, 101, 100};
		System.out.println(new String(bytes));
		
		
		String file = "/home/idbs/cmbos/data/trade_confirmation_29112016a.csv";
		FileInputStream fstream;
		try
		{
			fstream = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	
			String strLine;
	
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
				for (byte b: strLine.getBytes()) {
					System.out.print(b + "," );
				}
			  System.out.println();
			  System.out.println(strLine);
			}
	
			//Close the input stream
			br.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
