package com.celera.backoffice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.celera.mongo.entity.Account;

public class BOData
{
	public static Map<String, Account> map = new ConcurrentHashMap<String, Account>();

	static {

		Account o1 = new Account("CEL0001", "Nomura International PLC",
				"7th Floor, Winchester, Hiranandani Business Park, Powai, Mumbai, Maharashtra 400 076, India.",
				"santosh.dange@nomura.com", "Santosh Dange");

		Account o2 = new Account("CEL0002", "Deutsche Bank AG, Deutsche Bank Group, Global Market Listed Derivatives",
				"Velankani Tech Park, No 43, 3rd Floor, Block 5, Electronic City, Phase 1, Hosur Road, Bangalore, 560100, India",
				"futures.brokerage@db.com,singh.gagandeep@db.com,mohan.raj-mc@db.com", "Futures Brokerage");

		Account o2a = new Account("CEL0002", "Deutsche Bank AG London Branch",
				"Velankani Tech Park, No 43, 3rd Floor, Block 5, Electronic City, Phase 1, Hosur Road, Bangalore, 560100, India",
				"futures.brokerage@db.com,singh.gagandeep@db.com,mohan.raj-mc@db.com", "Futures Brokerage");

		Account o2b = new Account("CEL0002", "Deutsche Bank Group, Global Market Listed Derivatives",
				"Velankani Tech Park, No 43, 3rd Floor, Block 5, Electronic City, Phase 1, Hosur Road, Bangalore, 560100, India",
				"futures.brokerage@db.com,singh.gagandeep@db.com,mohan.raj-mc@db.com", "Futures Brokerage");

		Account o2c = new Account("CEL0002", "Global Market Listed Derivatives",
				"Velankani Tech Park, No 43, 3rd Floor, Block 5, Electronic City, Phase 1, Hosur Road, Bangalore, 560100, India",
				"futures.brokerage@db.com,singh.gagandeep@db.com,mohan.raj-mc@db.com", "Futures Brokerage");

		Account o2d = new Account("CEL0002", "Deutsche Bank AG",
				"Velankani Tech Park, No 43, 3rd Floor, Block 5, Electronic City, Phase 1, Hosur Road, Bangalore, 560100, India",
				"futures.brokerage@db.com,singh.gagandeep@db.com,mohan.raj-mc@db.com", "Futures Brokerage");

		Account o4 = new Account("CEL0003", "Optiver Australia Pty Limited",
				"39 Hunter St., Sydney NSW 2000, Australia.", "MiddleOfficeTeam@Optiver.com.au", "Brian Taylor");

		Account o4a = new Account("CEL0003", "Optiver Australia Pty Ltd", "39 Hunter St., Sydney NSW 2000, Australia.",
				"MiddleOfficeTeam@Optiver.com.au", "Brian Taylor");

		Account o5 = new Account("CEL0004", "Vivienne Court Trading Pty Limited",
				"Suite 304, 24-30 Springfield Avenue Potts Point, Sydney, NSW 2011, Australia",
				"midoffice@vivcourt.com", "Helen Robertson");

		Account o5a = new Account("CEL0004", "Vivienne Court Trading Pty Ltd",
				"Suite 304, 24-30 Springfield Avenue Potts Point, Sydney, NSW 2011, Australia",
				"midoffice@vivcourt.com", "Helen Robertson");

		Account o6 = new Account("CEL0005", "Liquid Capital Australia Pty Limited", "United Kindom",
				"brokerage@liquidcapital.com", "Robert Wickham");

		Account o6a = new Account("CEL0005", "Liquid Capital Australia Pty Ltd.", "United Kindom",
				"brokerage@liquidcapital.com", "Robert Wickham");

		Account o7 = new Account("CEL0006", "J.P. Morgan Securities PLC",
				"Futures & Options House Reconcillations, One@Changi City 7th Floor,  1 Changi Business Park Central 1, Singapore, 486036",
				"edg_asia_index_flow_trader_confos@jpmorgan.com , brokerage.control@jpmorgan.com", "F & O House team");

		Account o8 = new Account("CEL0007", "UBS Limited", "1 Broadgate, London, ECEM 2BS, United Kingdom",
				"SH-UBS-invoices@ubs.com", "SH-UBS-invoices@ubs.com");

		Account o8a = new Account("CEL0007", "UBS AG London Branch", "1 Broadgate, London, ECEM 2BS, United Kingdom",
				"SH-UBS-invoices@ubs.com", "SH-UBS-invoices@ubs.com");

		Account o9 = new Account("CEL0008", "Eclipse Futures (HK) Limited",
				"Room 3001, 30/F Subi Okaza, 225-257 Gloucester Road, Causeway Bay, Hong Kong",
				"kechan@eclipseoptions.com, invoices@eclipseoptions.com, accounts@eclipseoptions.com", "Kelvin Chan");

		Account o10 = new Account("CEL0009", "Citigroup Global Markets Limited",
				"ICG Global Expense Management Team, Citigroup Centre 1, 5th Floor, 33 Canada Square, Canary Wharf, London, E14 5LB, UK",
				"Brokerage.ciplc@citi.com", "London Billing Email");

		Account o10a = new Account("CEL0009", "Citigroup Global Markets Hong Kong Limited",
				"ICG Global Expense Management Team, Citigroup Centre 1, 5th Floor, 33 Canada Square, Canary Wharf, London, E14 5LB, UK",
				"Brokerage.ciplc@citi.com", "London Billing Email");

		Account o11 = new Account("CEL0010", "Royal Bank of Canada",
				"71 Queen Victoria Street, London, EC4V 4DE, United Kingdom", "", "Mark Nunns");

		Account o12 = new Account("CEL0011", "Korea Investment & Securities Co. Ltd.",
				"19/F, Trading Center, 27-1, Yeouido-dong, Yeongdeungpo-gu, Seoul, -150-747, Korea",
				"Trading_op@truefriend.com, onlyone@truefriend.com", "Hanna Kim");
		Account o12a = new Account("CEL0011", "Korea Investment & Securities Co Ltd",
				"19/F, Trading Center, 27-1, Yeouido-dong, Yeongdeungpo-gu, Seoul, -150-747, Korea",
				"Trading_op@truefriend.com, onlyone@truefriend.com", "Hanna Kim");

		Account o13 = new Account("CEL0012", "Liquid Capital Hong Kong Limited",
				"21st Floor, The Center, 99 Queens Road, Central, Hong Kong", "brokerage@liquidcapital.com",
				"Anthony Webb");

		Account o14 = new Account("CEL0013", "Celera Financial Limited", "11G, 51 Man Yue Street, Hunghom, Hong Kong",
				"cflbrokerage@celera-group.com", "");

		Account o15 = new Account("CEL0014", "BNP Paribas Arbitrage S.N.C.*", "",
				"bnparbitrage.brokerfees@bnpparibas.com,asia_edo_fees@asia.bnpparibas.com", "");

		Account o16 = new Account("CEL0015", "Morgan Stanley & Co. International PLC",
				" 25 Cabot Square,  Canary Wharf, London, E14 4QA", "brokerage.payables@morganstanley.com", "");

		Account o17 = new Account("CEL0016", "BNP Paribas Paris", "", "MARINE.MALAGNOUX@UK.BNPPARIBAS.COM ",
				"442075 959700");

		Account o18 = new Account("CEL0017", "BAFM", "", "derk.chan@bfam-partners.com,BFAM-MO@bfam-partners.com", "");

		Account o19 = new Account("CEL0018", "Société Générale (HK) Limited", "", "FREDERIC.OLIVE@SGCIB.COM ",
				"85221665027");

		Account o20 = new Account("CEL0019", "Mirae Asset Securities Co. Ltd", "", "wonjun.hwand@miraeasset.com ",
				"822 3774 6986");

		Account o21 = new Account("CEL0020", "Hyundai Securities", "", "syshin09@stockmarket.co.kr", "82261141866");

		Account o22 = new Account("CEL0021", "Barclays Bank PLC", "", "gupdocs@barclayscapital.com ", "");

		Account o23 = new Account("CEL0022", "Daishin Securities Co., Ltd.", "",
				"leejw81@daishin.com;songjy@daishin.com", "");

		Account o24 = new Account("CEL0023", "HMC Investment", "",
				"0401176@hmcib.com;justisoo@hmcib.com;0800574@hmcib.com", "");

		Account o25 = new Account("CEL0024", "NH Investment & Securities Co. Ltd.", "", "jackie5@nhwm.com ",
				"8227687681");

		Account o25a = new Account("CEL0024", "NH Investment & Securities Co Ltd", "", "jackie5@nhwm.com ",
				"8227687681");

		Account o26 = new Account("CEL0025", "Yuanta Securities Korea Co., Ltd", "", "kyojin.ku@yuantakorea.com ",
				"82237705984");

		Account o26a = new Account("CEL0025", "Yuanta Securities Korea Co., Ltd.", "", "kyojin.ku@yuantakorea.com ",
				"82237705984");

		Account o27 = new Account("CEL0026", "Samsung Securities Co., Ltd.", "", "youngja.yoon@samsung.com",
				"82220208426");
		Account o27a = new Account("CEL0026", "Samsung Securities Co., Ltd", "", "youngja.yoon@samsung.com",
				"82220208426");

		Account o28 = new Account("CEL0027", "IBK Securities Co. Ltd", "", "twklem@ibks.com ", "82269155823");
		Account o29 = new Account("CEL0028", "Shinhan Investment Corp.", "", "global_fo@shinhan.com", "82837722249");
		Account o30 = new Account("CEL0029", "", "", "", "");
		Account o31 = new Account("", "Haitong  International ", "", "settdep@htisec.com", "");
		Account o32 = new Account("", "Eugene Investment & future ", "", "joonke@eugenefn.com ", "");
		Account o33 = new Account("CEL0030", "Thierry", "", "", "");

		map.put(o1.key(), o1);
		map.put(o2.key(), o2);
		map.put(o2a.key(), o2a);
		map.put(o2b.key(), o2b);
		map.put(o2c.key(), o2c);
		map.put(o2d.key(), o2d);
		map.put(o4.key(), o4);
		map.put(o4a.key(), o4a);
		map.put(o5.key(), o5);
		map.put(o5a.key(), o5a);
		map.put(o6.key(), o6);
		map.put(o6a.key(), o6a);
		map.put(o7.key(), o7);
		map.put(o8.key(), o8);
		map.put(o8a.key(), o8a);
		map.put(o9.key(), o9);
		map.put(o10.key(), o10);
		map.put(o10a.key(), o10a);
		map.put(o11.key(), o11);
		map.put(o12.key(), o12);
		map.put(o12a.key(), o12a);
		map.put(o13.key(), o13);
		map.put(o14.key(), o14);
		map.put(o15.key(), o15);
		map.put(o16.key(), o16);
		map.put(o17.key(), o17);
		map.put(o18.key(), o18);
		map.put(o19.key(), o19);
		map.put(o20.key(), o20);
		map.put(o21.key(), o21);
		map.put(o22.key(), o22);
		map.put(o23.key(), o23);
		map.put(o24.key(), o24);
		map.put(o25.key(), o25);
		map.put(o25a.key(), o25a);
		map.put(o26.key(), o26);
		map.put(o26a.key(), o26a);
		map.put(o27.key(), o27);
		map.put(o27a.key(), o27a);
		map.put(o28.key(), o28);
		map.put(o29.key(), o29);
		map.put(o33.key(), o33);

	}

	public static Account get(String company) {
		return map.get(company.toUpperCase());
	}
}
