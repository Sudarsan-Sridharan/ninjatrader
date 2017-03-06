package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.mongo.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.model.request.SaveTradeAlgorithmRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/4/16.
 */
public class MongoTradeAlgorithmDaoTest {
  private static final Logger LOG = LoggerFactory.getLogger(MongoTradeAlgorithmDaoTest.class);

  private final TradeAlgorithm algo = TradeAlgorithm.builder()
      .id("test").userId("sys").algorithm("{ sample algorithm }").build();

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
    final List<TradeAlgorithm> saved = dao.save(SaveTradeAlgorithmRequest.addEntity(algo));

    assertThat(saved).containsExactly(algo);

    assertThat(dao.find(FindTradeAlgorithmRequest.withUserId("sys"))).containsExactly(algo);
    assertThat(dao.findOne(FindTradeAlgorithmRequest.withTradeAlgorithmId("test"))).hasValue(algo);

    assertThat(dao.find(FindTradeAlgorithmRequest.withUserId("non-existing"))).isEmpty();
    assertThat(dao.find(FindTradeAlgorithmRequest.withTradeAlgorithmId("non-existing"))).isEmpty();
  }

  @Test
  public void testOverwrite_shouldOverwriteOldRecordWithNew() {
    final TradeAlgorithm overwrite = TradeAlgorithm.builder()
        .id("test").userId("sys").algorithm("{ sample overwritten algorithm }").build();

    // Save old algorithm
    dao.save(SaveTradeAlgorithmRequest.addEntity(algo));

    // Overwrite old with new
    dao.save(SaveTradeAlgorithmRequest.addEntity(overwrite));

    assertThat(dao.findOne(FindTradeAlgorithmRequest.withTradeAlgorithmId("test"))).hasValue(overwrite);
  }

  @Test
  public void testSaveMultiple_shouldSaveAll() {
    final TradeAlgorithm algo2 = TradeAlgorithm.builder()
        .id("test2").userId("sys").algorithm("{ sample algorithm 2 }").build();
    final TradeAlgorithm overwrite = TradeAlgorithm.builder()
        .id("test").userId("sys").algorithm("{ sample overwritten algorithm }").build();

    dao.save(SaveTradeAlgorithmRequest.addEntities(algo, algo2, overwrite));

    assertThat(dao.find(FindTradeAlgorithmRequest.withUserId("sys"))).containsExactlyInAnyOrder(algo2, overwrite);
  }
}
