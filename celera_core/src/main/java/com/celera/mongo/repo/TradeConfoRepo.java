package com.celera.mongo.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.celera.mongo.entity.Person;
import com.celera.mongo.entity.TradeConfo;

public interface TradeConfoRepo extends CrudRepository<TradeConfo, Long>
{
	@Query("{'tradeConfoId' : ?0}")
	public Iterable<TradeConfo> searchByTradeConfoId(String tradeConfoId);
}
