package com.celera.mongo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.CrudRepository;

import com.celera.mongo.entity.Address;
import com.celera.mongo.entity.IMongoDocument;
import com.celera.mongo.entity.Person;
import com.celera.mongo.entity.TradeConfo;
import com.celera.mongo.repo.AddressRepo;
import com.celera.mongo.repo.PersonRepo;
import com.celera.mongo.repo.TradeConfoRepo;

public class MongoDbAdapter
{
	Logger logger = LoggerFactory.getLogger(MongoDbAdapter.class);
	
	private ApplicationContext ctx;
	private static MongoDbAdapter _instance;
	
	private MongoDbAdapter()
	{
		ctx = new GenericXmlApplicationContext("spring-config.xml");
	}
	
	public static MongoDbAdapter instance()
	{
		if (_instance == null)
		{
			synchronized (MongoDbAdapter.class)
			{
				if (_instance == null)
				{
					_instance = new MongoDbAdapter();		
				}
			}
		}
		return _instance;
	}
	
	public void save(IMongoDocument doc) 
	{
		try
		{
			doc.setLastModified(new Date(System.currentTimeMillis()));
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new ClassPathResource("spring-config.xml").getPath());
			Class clazz = doc.getRepo();
			CrudRepository repo = (CrudRepository) ctx.getBean(clazz);
			repo.save(doc);

//		Iterable<TradeConfo> tradeConfoList = tradeConfoRepo.findAll();
//		System.out.println("TradeConfo List : ");
//		for (TradeConfo person : tradeConfoList)
//		{
//			System.out.println(person);
//		}
//		System.out.println("TradeConfo with tradeId CELERAEQ-2016-13155 is " + tradeConfoRepo.searchByTradeConfoId("CELERAEQ-2016-13155"));
//		logger.info("insert: {}" , doc.toString());
//		context.close();
		}
		catch (Exception e)
		{
			logger.error("", e);
			throw e;
		}
	}
	
	public void delete(IMongoDocument doc) 
	{
		try
		{
//			doc.setLastModified(new Date(System.currentTimeMillis()));
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new ClassPathResource("spring-config.xml").getPath());
			Class clazz = doc.getRepo();
			CrudRepository repo = (CrudRepository) ctx.getBean(clazz);
			repo.delete(doc);
			
//		Iterable<TradeConfo> tradeConfoList = tradeConfoRepo.findAll();
//		System.out.println("TradeConfo List : ");
//		for (TradeConfo person : tradeConfoList)
//		{
//			System.out.println(person);
//		}
//		System.out.println("TradeConfo with tradeId CELERAEQ-2016-13155 is " + tradeConfoRepo.searchByTradeConfoId("CELERAEQ-2016-13155"));
//		logger.info("insert: {}" , doc.toString());
//		context.close();
		}
		catch (Exception e)
		{
			logger.error("", e);
			throw e;
		}
	}
	
	public void deleteAll(IMongoDocument doc) 
	{
		try
		{
			Class clazz = doc.getRepo();
			CrudRepository repo = (CrudRepository) ctx.getBean(clazz);
			repo.deleteAll();
		}
		catch (Exception e)
		{
			logger.error("", e);
			throw e;
		}
	}
	
	public CrudRepository get(Class clazz) {
		return (CrudRepository) ctx.getBean(clazz);
	}
	
	static public void main(String[] a)
	{
		TradeConfo tradeConfo = new TradeConfo();
		
	}
}
