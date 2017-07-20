package com.bn.ninjatrader.model.mongo.guice;

import com.bn.ninjatrader.common.guice.NtClockModule;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.mongo.annotation.PriceCollection;
import com.bn.ninjatrader.model.mongo.annotation.StockCollection;
import com.bn.ninjatrader.model.mongo.annotation.TradeAlgorithmCollection;
import com.bn.ninjatrader.model.mongo.annotation.UserCollection;
import com.bn.ninjatrader.model.mongo.client.DbClient;
import com.bn.ninjatrader.model.mongo.dao.MongoAlgorithmDao;
import com.bn.ninjatrader.model.mongo.dao.MongoPriceDao;
import com.bn.ninjatrader.model.mongo.dao.MongoUserDao;
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

  public static final String USER_COLLECTION = "user";
  public static final String PRICE_COLLECTION = "price";
  public static final String STOCK_COLLECTION = "stock";
  public static final String TRADE_ALGO_COLLECTION = "trade_algo";

  private DbClient dbClient;

  public NtModelMongoModule() {}

  public NtModelMongoModule(final DbClient dbClient) {
    this.dbClient = dbClient;
  }

  @Override
  protected void configure() {
    install(new NtClockModule());
    install(new ArchaiusModule());

    bind(UserDao.class).to(MongoUserDao.class);
    bind(PriceDao.class).to(MongoPriceDao.class);
    bind(AlgorithmDao.class).to(MongoAlgorithmDao.class);
  }

  @Provides
  private DbClient provideDbClient(final Config config) {
    if (dbClient == null) {
      final String host = config.getString("mongo.host");
      final String databaseName = config.getString("mongo.database.name");
      dbClient = DbClient.create().host(host).databaseName(databaseName).connect();
    }
    return dbClient;
  }

  @Provides
  @PriceCollection
  private MongoCollection providePriceCollection(final DbClient dbClient) {
    return dbClient.getMongoCollection(PRICE_COLLECTION);
  }

  @Provides
  @UserCollection
  private MongoCollection provideUserCollection(final DbClient dbClient) {
    return dbClient.getMongoCollection(USER_COLLECTION);
  }

  @Provides
  @TradeAlgorithmCollection
  private MongoCollection provideTradeAlgoCollection(final DbClient dbClient) {
    return dbClient.getMongoCollection(TRADE_ALGO_COLLECTION);
  }

  @Provides
  @StockCollection
  private MongoCollection provideStockCollection(final DbClient dbClient) {
    return dbClient.getMongoCollection(STOCK_COLLECTION);
  }
}
