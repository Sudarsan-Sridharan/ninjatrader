package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.data.AbstractStockData;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.jongo.Oid;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public abstract class AbstractDao<T> {
  public static final String QUERY_FIND_BY_SYMBOL = String.format("{%s : #}", AbstractStockData.SYMBOL);

  public static final String QUERY_FIND_BY_YEAR = String.format("{%s : #, %s: #}",
          AbstractStockData.SYMBOL, AbstractStockData.YEAR);

  public static final String QUERY_FIND_BY_YEAR_RANGE = String.format("{%s : #, %s: {$gte: #, $lte: #}}",
          AbstractStockData.SYMBOL, AbstractStockData.YEAR);

  public static final String QUERY_FIND_ALL_FOR_YEAR = String.format("{%s : #}", AbstractStockData.YEAR);

  private final MongoCollection mongoCollection;

  public AbstractDao(MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  public void save(T t) {
    mongoCollection.save(t);
  }

  public void remove(String id) {
    mongoCollection.remove(Oid.withOid(id));
  }

  public void insert(T t) {
    mongoCollection.insert(t);
  }

  public MongoCollection getMongoCollection() {
    return mongoCollection;
  }
}
