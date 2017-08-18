package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.common.model.Algorithm;
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

  private final Algorithm algo = Algorithm.builder()
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

    assertThat(dao.findOneByAlgorithmId("test")).hasValue(algo);
    assertThat(dao.findAlgorithms().withUserId("sys").now()).containsExactly(algo);

    assertThat(dao.findOneByAlgorithmId("non-existing")).isEmpty();
    assertThat(dao.findAlgorithms().withUserId("non-existing").now()).isEmpty();
  }

  @Test
  public void testSaveWithNoId_shouldGenerateIdAndSave() {
    final Algorithm saved = dao.save(Algorithm.builder()
        .userId("sys").description("desc").algorithm("{ sample algorithm }").build());

    assertThat(saved.getId()).isNotEmpty();

    final Optional<Algorithm> found = dao.findOneByAlgorithmId(saved.getId());
    assertThat(found).isNotEmpty();
    assertThat(found.get().getAlgorithm()).isEqualTo("{ sample algorithm }");
  }

  @Test
  public void testFindAutoScanEnabledOnly_shouldRetrieveOnlyThoseWithAutoScan() {
    dao.save(algo); // isAutoScan = false

    final Algorithm autoScanAlgo = dao.save(Algorithm.builder()
        .userId("sys").description("desc").algorithm("{ sample algorithm }").isAutoScan(true).build());

    assertThat(dao.findAlgorithms().isAutoScan(true).now()).containsExactly(autoScanAlgo);
    assertThat(dao.findAlgorithms().withUserId("sys").isAutoScan(true).now()).containsExactly(autoScanAlgo);

    assertThat(dao.findAlgorithms().isAutoScan(false).now()).containsExactly(algo);
    assertThat(dao.findAlgorithms().withUserId("sys").isAutoScan(false).now()).containsExactly(autoScanAlgo);
  }

  @Test
  public void testUpdate_shouldUpdateInDb() {
    final Algorithm updatedAlgo = Algorithm.builder()
        .algoId("test").userId("sys")
        .algorithm("{ sample overwritten algorithm }").description("").isAutoScan(true).build();

    // Save old algorithm
    dao.save(algo);

    // Update with new details
    dao.save(updatedAlgo);

    // Verify that algorithm has new content
    assertThat(dao.findOneByAlgorithmId("test")).hasValue(updatedAlgo);
  }

  @Test
  public void testDelete_shouldRemoveFromDb() {
    final Algorithm algo1 = Algorithm.builder().algoId("algo1").userId("sys").algorithm("{ algo 1 }").build();
    final Algorithm algo2 = Algorithm.builder().algoId("algo2").userId("sys").algorithm("{ algo 2 }").build();

    // Save algorithms
    dao.save(algo1);
    dao.save(algo2);

    // Delete algo1
    dao.delete("algo1");

    // Verify that algo1 is deleted and algo2 still exists
    assertThat(dao.findOneByAlgorithmId("algo1")).isEmpty();
    assertThat(dao.findOneByAlgorithmId("algo2")).hasValue(algo2);
  }
}
