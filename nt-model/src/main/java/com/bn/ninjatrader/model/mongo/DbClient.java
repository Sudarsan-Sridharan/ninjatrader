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
  private static final String MONGODB_NAME = "ninja_trader";
  private static final int CONNECTION_TIMEOUT_MILLIS = 5000;

  private String dbName = "ninja_trader";
  private String host = "192.168.99.100:32769";
  private Jongo jongo;

  public DbClient connect() {
    final MongoClient mongoClient = new MongoClient(getHost(),
        MongoClientOptions.builder()
            .serverSelectionTimeout(CONNECTION_TIMEOUT_MILLIS)
            .build());

    jongo = new Jongo(new DB(mongoClient, getDbName()),
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

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
