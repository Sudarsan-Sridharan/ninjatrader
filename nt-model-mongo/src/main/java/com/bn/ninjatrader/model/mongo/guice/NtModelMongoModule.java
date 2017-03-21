package com.bn.ninjatrader.model.mongo.guice;

import com.bn.ninjatrader.common.guice.NtClockModule;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.mongo.annotation.PriceCollection;
import com.bn.ninjatrader.model.mongo.annotation.TradeAlgorithmCollection;
import com.bn.ninjatrader.model.mongo.client.DbClient;
import com.bn.ninjatrader.model.mongo.dao.MongoPriceDao;
import com.bn.ninjatrader.model.mongo.dao.MongoTradeAlgorithmDao;
import com.bn.ninjatrader.model.mongo.factory.PriceBuilderFactoryMongo;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.guice.ArchaiusModule;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 4/30/16.
 */
public class NtModelMongoModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(NtModelMongoModule.class);

  public static final String SETTINGS_COLLECTION = "settings";
  public static final String ICHIMOKU_COLLECTION = "ichimoku";
  public static final String MEAN_COLLECTION = "mean";
  public static final String PRICE_COLLECTION = "price";
  public static final String TRADE_ALGO_COLLECTION = "trade_algo";
  public static final String SMA_COLLECTION = "sma";
  public static final String EMA_COLLECTION = "ema";
  public static final String RSI_COLLECTION = "rsi";
  public static final String STOCK_COLLECTION = "stock";
  public static final String REPORT_COLLECTION = "report";

  private DbClient dbClient;

  public NtModelMongoModule() {}

  public NtModelMongoModule(final DbClient dbClient) {
    this.dbClient = dbClient;
  }

  @Override
  protected void configure() {
    install(new NtClockModule());
    install(new ArchaiusModule());

    bind(PriceDao.class).to(MongoPriceDao.class);
    bind(TradeAlgorithmDao.class).to(MongoTradeAlgorithmDao.class);
    bind(PriceBuilderFactory.class).to(PriceBuilderFactoryMongo.class);

//    bindAnnotatedToCollection(SettingsCollection.class, SETTINGS_COLLECTION);
//    bindAnnotatedToCollection(ReportCollection.class, REPORT_COLLECTION);
//    bindAnnotatedToCollection(PriceCollection.class, PRICE_COLLECTION);
//    bindAnnotatedToCollection(TradeAlgorithmCollection.class, TRADE_ALGO_COLLECTION);
//    bindAnnotatedToCollection(IchimokuCollection.class, ICHIMOKU_COLLECTION);
//    bindAnnotatedToCollection(MeanCollection.class, MEAN_COLLECTION);
//    bindAnnotatedToCollection(SMACollection.class, SMA_COLLECTION);
//    bindAnnotatedToCollection(EMACollection.class, EMA_COLLECTION);
//    bindAnnotatedToCollection(RSICollection.class, RSI_COLLECTION);
//    bindAnnotatedToCollection(StockCollection.class, STOCK_COLLECTION);
  }

//  private void bindAnnotatedToCollection(final Class<? extends Annotation> annotation, final String collectionName) {
//    final MongoCollection collection = dbClient.getMongoCollection(collectionName);
//    bind(MongoCollection.class).annotatedWith(annotation).toInstance(collection);
//  }

  @Provides
  private DbClient provideDbClient(final Config config) {
    if (dbClient == null) {
      final String host = config.getString("mongo.host");
      final String databaseName = config.getString("mongo.database.name");
      LOG.info("Connecting to {}/{}", host, databaseName);
      dbClient = DbClient.create().host(host).databaseName(databaseName).connect();
    }
    return dbClient;
  }

  @Provides
  @PriceCollection
  private MongoCollection providePriceCollection(final DbClient dbClient) {
    final MongoCollection collection = dbClient.getMongoCollection(PRICE_COLLECTION);
    return collection;
  }

  @Provides
  @TradeAlgorithmCollection
  private MongoCollection provideTradeAlgoCollection(final DbClient dbClient) {
    final MongoCollection collection = dbClient.getMongoCollection(TRADE_ALGO_COLLECTION);
    return collection;
  }
}
