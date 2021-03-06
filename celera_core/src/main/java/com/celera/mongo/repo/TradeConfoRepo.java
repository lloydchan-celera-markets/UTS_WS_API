package com.celera.mongo.repo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.celera.mongo.entity.Person;
import com.celera.mongo.entity.TradeConfo;

public interface TradeConfoRepo extends CrudRepository<TradeConfo, String>
{
	@Query("{'tradeConfoId' : ?0}")
	public Iterable<TradeConfo> searchByTradeConfoId(String tradeConfoId);
	
//	@Query("UPDATE TradeConfo t set t.summary = ?1, t.buyer = ?2, t.seller = ?3, t.price = ?4, t.curncy = ?5, "
//			+ "t.tradeDate = ?6, t.refPrice = ?7, t.tradeConfoId = ?8, t.delta = ?9, t.buyQty = ?10, t.sellQty = ?11"
//			+ ", t.ptValue = ?12, t.ptCny = ?13, t.premiumPmt = ?14, t.notational = ?15, t.notationalCny = ?16, "
//			+ "t.rate = ?17, t.premium = ?18, t.premiumCny= ?19, t.hedge = ?20, t.hedgeFutRef = ?21, "
//			+ "t.brokerageFee = ?22, t.brokerageCny = ?23, where t.buyer = ?24 and t.seller = ?25 and t.tradeConfoId = ?26}")
//	public void updateTradeConfo(String tradeConfoId);
	
	
	@Query("{'tradeDate' : { '$gt' : ?0, '$lt' : ?1 }}")
	public Collection<TradeConfo> findBetween(Date startDay, Date endDay);
//	public default Collection<TradeConfo> findBetween(Date startDay, Date endDay) {
//		Criteria c = Criteria.where("tradeDate").gte(startDay).and("tradeDate").lte(endDay);
//		List<TradeConfo> users = TradeConfoRepo.find(query(c), TradeConfo.class);
//	}

	@Query("{'lastModified' : { '$gt' : ?0 }}")
	public Collection<TradeConfo> findAfter(Date lastModified);
}
