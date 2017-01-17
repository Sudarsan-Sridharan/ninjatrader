package com.bn.ninjatrader.model.mongo;

import com.bn.ninjatrader.model.guice.NtModelModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class DbClient {
  private static final Logger LOG = LoggerFactory.getLogger(NtModelModule.class);
  private static final String DEFAULT_DB_NAME = "ninja_trader";
  private static final String DEFAULT_HOST = "192.168.99.100:32768";
  private static final int CONNECTION_TIMEOUT_MILLIS = 5000;

  public static final DbClient createDefault() {
    DbClient dbClient = new DbClient().databaseName(DEFAULT_DB_NAME).host(DEFAULT_HOST).connect();
    return dbClient;
  }

  public static final DbClient create() {
    return new DbClient();
  }

  private String dbName;
  private String host;
  private Jongo jongo;

  private DbClient() {}

  public DbClient connect() {
    final MongoClient mongoClient = new MongoClient(host,
        MongoClientOptions.builder()
            .serverSelectionTimeout(CONNECTION_TIMEOUT_MILLIS)
            .build());

    jongo = new Jongo(new DB(mongoClient, dbName),
        new JacksonMapper.Builder()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .build());

    LOG.info("Connected to mongodb: {}", mongoClient.getAddress());
    return this;
  }

  public MongoCollection getMongoCollection(String collectionName) {
    return jongo.getCollection(collectionName);
  }

  public DbClient host(String host) {
    this.host = host;
    return this;
  }

  public DbClient databaseName(final String dbName) {
    this.dbName = dbName;
    return this;
  }
}
