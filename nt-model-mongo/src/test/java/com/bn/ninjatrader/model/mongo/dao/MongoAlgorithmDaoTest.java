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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/4/16.
 */
public class MongoAlgorithmDaoTest {
  private static final Logger LOG = LoggerFactory.getLogger(MongoAlgorithmDaoTest.class);

  private final TradeAlgorithm algo = TradeAlgorithm.builder()
      .algoId("test").userId("sys").description("desc").algorithm("{ sample algorithm }").build();

  private static Injector injector;

  private MongoAlgorithmDao dao;

  @BeforeClass
  public static void setup() {
    injector = Guice.createInjector(new NtModelTestModule());
  }

  @Before
  public void before() {
    dao = injector.getInstance(MongoAlgorithmDao.class);
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
  public void testSaveWithNoId_shouldGenerateIdAndSave() {
    final TradeAlgorithm saved = dao.save(TradeAlgorithm.builder()
        .userId("sys").description("desc").algorithm("{ sample algorithm }").build());

    assertThat(saved.getId()).isNotEmpty();

    final Optional<TradeAlgorithm> found = dao.findByTradeAlgorithmId(saved.getId());
    assertThat(found).isNotEmpty();
    assertThat(found.get().getAlgorithm()).isEqualTo("{ sample algorithm }");
  }

  @Test
  public void testUpdate_shouldUpdateInDb() {
    final TradeAlgorithm updatedAlgo = TradeAlgorithm.builder()
        .algoId("test").userId("sys").algorithm("{ sample overwritten algorithm }").description("").build();

    // Save old algorithm
    dao.save(algo);

    // Update with new details
    dao.save(updatedAlgo);

    assertThat(dao.findByTradeAlgorithmId("test")).hasValue(updatedAlgo);
  }
}
