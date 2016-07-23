package com.bn.ninjatrader.model.guice;

import com.bn.ninjatrader.model.annotation.DailyIchimokuCollection;
import com.bn.ninjatrader.model.annotation.DailyMeanCollection;
import com.bn.ninjatrader.model.annotation.DailyPriceCollection;
import com.bn.ninjatrader.model.annotation.StockCollection;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

import java.lang.annotation.Annotation;

/**
 * Created by Brad on 4/30/16.
 */
public class NtModelTestModule extends AbstractModule {

  public static final String DAILY_PRICE_COLLECTION = "daily_price";
  public static final String DAILY_ICHIMOKU_COLLECTION = "daily_ichimoku";
  public static final String DAILY_MEAN_COLLECTION = "daily_mean";
  public static final String STOCK_COLLECTION = "stock";
  public static final String MONGODB_NAME = "test_ninja_trader";

  private Jongo jongo;

  @Override
  protected void configure() {
    MongoClient mongoClient = new MongoClient("192.168.99.100:32768");
    MongoDatabase db = mongoClient.getDatabase(MONGODB_NAME);

    jongo = new Jongo(new DB(mongoClient, MONGODB_NAME),
        new JacksonMapper.Builder()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .build());

    bind(MongoDatabase.class).toInstance(db);
    bindAnnotatedToCollection(DailyPriceCollection.class, DAILY_PRICE_COLLECTION);
    bindAnnotatedToCollection(DailyIchimokuCollection.class, DAILY_ICHIMOKU_COLLECTION);
    bindAnnotatedToCollection(DailyMeanCollection.class, DAILY_MEAN_COLLECTION);
    bindAnnotatedToCollection(StockCollection.class, STOCK_COLLECTION);
  }

  private void bindAnnotatedToCollection(Class<? extends Annotation> annotation, String collectionName) {
    MongoCollection collection = jongo.getCollection(collectionName);
    bind(MongoCollection.class).annotatedWith(annotation).toInstance(collection);
  }
}
