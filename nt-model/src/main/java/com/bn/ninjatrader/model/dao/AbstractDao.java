package com.bn.ninjatrader.model.dao;

import com.google.inject.Singleton;
import org.jongo.MongoCollection;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public abstract class AbstractDao<T> {

  private final MongoCollection mongoCollection;

  public AbstractDao(MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  public void save(T t) {
    mongoCollection.save(t);
  }

  public MongoCollection getMongoCollection() {
    return mongoCollection;
  }
}
