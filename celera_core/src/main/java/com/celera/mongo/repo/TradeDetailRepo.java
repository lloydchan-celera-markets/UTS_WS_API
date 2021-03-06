package com.celera.mongo.repo;

import org.springframework.data.repository.CrudRepository;

import com.celera.mongo.entity.TradeDetail;

public interface TradeDetailRepo extends CrudRepository<TradeDetail, String>
{
//	@Query("UPDATE TradeConfo t set t.summary = ?1, t.buyer = ?2, t.seller = ?3, t.price = ?4, t.curncy = ?5, "
//			+ "t.tradeDate = ?6, t.refPrice = ?7, t.tradeConfoId = ?8, t.delta = ?9, t.buyQty = ?10, t.sellQty = ?11"
//			+ ", t.ptValue = ?12, t.ptCny = ?13, t.premiumPmt = ?14, t.notational = ?15, t.notationalCny = ?16, "
//			+ "t.rate = ?17, t.premium = ?18, t.premiumCny= ?19, t.hedge = ?20, t.hedgeFutRef = ?21, "
//			+ "t.brokerageFee = ?22, t.brokerageCny = ?23, where t.buyer = ?24 and t.seller = ?25 and t.tradeConfoId = ?26}")
//	public void updateTradeConfo(String tradeConfoId);
}
