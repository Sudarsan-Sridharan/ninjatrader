package com.bn.ninjatrader.model.guice;

import com.bn.ninjatrader.model.annotation.TestCollection;
import com.bn.ninjatrader.model.mongo.DbClient;
import org.jongo.MongoCollection;

/**
 * Created by Brad on 4/30/16.
 */
public class NtModelTestModule extends NtModelMongoModule {

  private static final String DB_NAME = "test_ninja_trader";
  private static final String HOST = "192.168.99.100:32768";
  private static final DbClient dbClient = DbClient.create().host(HOST).databaseName(DB_NAME).connect();
  private MongoCollection testCollection;

  public NtModelTestModule() {
    super(dbClient);
  }

  @Override
  protected void configure() {
    super.configure();
    testCollection = dbClient.getMongoCollection("test_collection");

    bind(MongoCollection.class).annotatedWith(TestCollection.class).toInstance(testCollection);
  }
}
