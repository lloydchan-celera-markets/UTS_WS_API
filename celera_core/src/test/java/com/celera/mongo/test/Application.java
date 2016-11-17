package com.celera.mongo.test;


import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.core.io.ClassPathResource;

import com.celera.mongo.entity.Address;
import com.celera.mongo.entity.Person;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.repo.AddressRepo;
import com.celera.mongo.repo.PersonRepo;
import com.celera.mongo.repo.TradeConfoRepo;

public class Application
{
	static void demoTradeConfo() 
	{
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new ClassPathResource("spring-config.xml").getPath());
		ApplicationContext context = new GenericXmlApplicationContext("spring-config.xml");
		
		TradeConfoRepo tradeConfoRepo = context.getBean(TradeConfoRepo.class);
		TradeConfo tradeConfo = new TradeConfo();
//		tradeConfo.setId(1l);
		tradeConfo.setSummary("Listed KS200 DEC17 200/240 -2/1 European Put Ratio REF 259.50 (DEC16)");
		tradeConfo.setBuyer("Optiver Australia Pty Ltd - Lucy Goulopoulos");
		tradeConfo.setSeller("Morgan Stanley & Co. International PLC");
		tradeConfo.setPrice(3.45d);
		tradeConfo.setCurncy("KRW");
//		tradeConfo.setTradeDate("04-Oct-16");
		tradeConfo.setRefPrice(259.5d);
		tradeConfo.setTradeConfoId("CELERAEQ-2016-13151");
		tradeConfo.setDelta("-10.00 %");
		tradeConfo.setBuyQty(200d);
		tradeConfo.setSellQty(100d);
		tradeConfo.setPtValue(500000d);
		tradeConfo.setPtCny("KRW");
		tradeConfo.setPremiumPmt("As per exchange rules");
		tradeConfo.setNotational(12975000000d);
		tradeConfo.setNotationalCny("KRW");
		tradeConfo.setRate(1103d);
		tradeConfo.setPremium(172500000d);
		tradeConfo.setPremiumCny("KRW");
//		tradeConfo.setHedge(Arrays.asList({"Buy 50 MINI FUTURES (KS200 DEC16)"}));
//		tradeConfo.setHedgeFutRef(259.5d);
		tradeConfo.setBrokerageFee(0d);
		tradeConfo.setBrokerageCny("USD");
//		List<String> legs = tradeConfo.getLegs();
//		legs.add(new Leg("Buy", 100, new Date(28-Dec-17 10),800 EuropeanCall 523.00 +2,615,000.00");
//		legs.add("Leg2 Sell 200 29-Dec-16 10,800 EuropeanCall 100.00 -1,000,000.00");

		tradeConfoRepo.save(tradeConfo);

		Iterable<TradeConfo> tradeConfoList = tradeConfoRepo.findAll();
		System.out.println("TradeConfo List : ");
		for (TradeConfo person : tradeConfoList)
		{
			System.out.println(person);
		}

		System.out.println("TradeConfo with tradeId CELERAEQ-2016-13155 is " + tradeConfoRepo.searchByTradeConfoId("CELERAEQ-2016-13155"));

//		context.close();
	}
	
	public static void main(String[] args)
	{
		demoTradeConfo();
	}
}
