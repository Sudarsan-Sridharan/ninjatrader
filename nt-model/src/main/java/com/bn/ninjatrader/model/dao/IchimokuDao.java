package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.model.annotation.DailyIchimokuCollection;
import com.bn.ninjatrader.model.data.AbstractStockData;
import com.bn.ninjatrader.model.data.IchimokuData;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class IchimokuDao {

  private static final Logger log = LoggerFactory.getLogger(IchimokuDao.class);

  private static final String QUERY_FIND_BY_YEAR = String.format("{%s : #, %s: #}",
      AbstractStockData.SYMBOL, AbstractStockData.YEAR);

  private final MongoCollection mongoCollection;

  @Inject
  public IchimokuDao(@DailyIchimokuCollection MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;

    getMongoCollection().ensureIndex(
            String.format(" { %s : 1 , %s : 1 } ", AbstractStockData.SYMBOL, AbstractStockData.YEAR), "{ unique: true }");
  }

  public Optional<IchimokuData> findPriceByYear(String symbol, int year) {
    IchimokuData data = mongoCollection.findOne(QUERY_FIND_BY_YEAR, symbol, year).as(IchimokuData.class);
    return Optional.ofNullable(data);
  }

  public void save(IchimokuData t) {
    mongoCollection.save(t);
  }


  public MongoCollection getMongoCollection() {
    return mongoCollection;
  }

  public void upsert(String symbol, Ichimoku ichimoku) {
    mongoCollection.update(QUERY_FIND_BY_YEAR, symbol, ichimoku.getDate().getYear())
            .upsert()
            .with("{$addToSet:{data:#}}", ichimoku);
  }
}
