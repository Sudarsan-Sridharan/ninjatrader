package com.bn.ninjatrader.model.guice;

import com.bn.ninjatrader.model.annotation.*;
import com.bn.ninjatrader.model.mongo.DbClient;
import com.google.inject.AbstractModule;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Created by Brad on 4/30/16.
 */
public class NtModelModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(NtModelModule.class);

  public static final String SETTINGS_COLLECTION = "settings";
  public static final String ICHIMOKU_COLLECTION = "ichimoku";
  public static final String MEAN_COLLECTION = "mean";
  public static final String PRICE_COLLECTION = "price";
  public static final String SMA_COLLECTION = "sma";
  public static final String EMA_COLLECTION = "ema";
  public static final String RSI_COLLECTION = "rsi";
  public static final String STOCK_COLLECTION = "stock";
  public static final String REPORT_COLLECTION = "report";

  private final DbClient dbClient;

  public NtModelModule() {
    this(DbClient.createDefault());
  }

  public NtModelModule(final DbClient dbClient) {
    this.dbClient = dbClient;
  }

  @Override
  protected void configure() {
    bindAnnotatedToCollection(SettingsCollection.class, SETTINGS_COLLECTION);
    bindAnnotatedToCollection(ReportCollection.class, REPORT_COLLECTION);
    bindAnnotatedToCollection(PriceCollection.class, PRICE_COLLECTION);
    bindAnnotatedToCollection(IchimokuCollection.class, ICHIMOKU_COLLECTION);
    bindAnnotatedToCollection(MeanCollection.class, MEAN_COLLECTION);
    bindAnnotatedToCollection(SMACollection.class, SMA_COLLECTION);
    bindAnnotatedToCollection(EMACollection.class, EMA_COLLECTION);
    bindAnnotatedToCollection(RSICollection.class, RSI_COLLECTION);
    bindAnnotatedToCollection(StockCollection.class, STOCK_COLLECTION);
  }

  private void bindAnnotatedToCollection(final Class<? extends Annotation> annotation, final String collectionName) {
    final MongoCollection collection = dbClient.getMongoCollection(collectionName);
    bind(MongoCollection.class).annotatedWith(annotation).toInstance(collection);
  }
}
