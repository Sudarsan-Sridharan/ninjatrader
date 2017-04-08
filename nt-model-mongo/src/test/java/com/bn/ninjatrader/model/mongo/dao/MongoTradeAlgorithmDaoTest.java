package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.mongo.guice.NtModelTestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/4/16.
 */
public class MongoTradeAlgorithmDaoTest {
  private static final Logger LOG = LoggerFactory.getLogger(MongoTradeAlgorithmDaoTest.class);

  private final TradeAlgorithm algo = TradeAlgorithm.builder()
      .id("test").userId("sys").description("desc").algorithm("{ sample algorithm }").build();

  private static Injector injector;

  private MongoTradeAlgorithmDao dao;

  @BeforeClass
  public static void setup() {
    injector = Guice.createInjector(new NtModelTestModule());
  }

  @Before
  public void before() {
    dao = injector.getInstance(MongoTradeAlgorithmDao.class);
    dao.getMongoCollection().remove();
  }

  @Test
  public void testSaveAndFind_shouldReturnEqualObject() {
    dao.save(algo);

    assertThat(dao.findByTradeAlgorithmId("test")).hasValue(algo);
    assertThat(dao.findByUserId("sys")).containsExactly(algo);

    assertThat(dao.findByTradeAlgorithmId("non-existing")).isEmpty();
    assertThat(dao.findByUserId("non-existing")).isEmpty();
  }

  @Test
  public void testUpdate_shouldUpdateInDb() {
    final TradeAlgorithm updatedAlgo = TradeAlgorithm.builder()
        .id("test").userId("sys").algorithm("{ sample overwritten algorithm }").description("").build();

    // Save old algorithm
    dao.save(algo);

    // Update with new details
    dao.save(updatedAlgo);

    assertThat(dao.findByTradeAlgorithmId("test")).hasValue(updatedAlgo);
  }
}
