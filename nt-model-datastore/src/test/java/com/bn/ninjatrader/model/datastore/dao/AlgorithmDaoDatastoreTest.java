package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.common.util.IdGenerator;
import com.bn.ninjatrader.model.datastore.document.AlgorithmDocument;
import com.bn.ninjatrader.model.util.TestUtil;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class AlgorithmDaoDatastoreTest {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final LocalDate now = LocalDate.of(2017, 1, 1);

  private Closeable session;
  private AlgorithmDaoDatastore algorithmDao;
  private IdGenerator idGenerator;
  private Algorithm algorithm;

  @BeforeClass
  public static void setUpBeforeClass() {
    ObjectifyService.setFactory(new ObjectifyFactory());
    ObjectifyService.register(AlgorithmDocument.class);
  }

  @Before
  public void before() {
    idGenerator = mock(IdGenerator.class);

    when(idGenerator.createId()).thenReturn("test_id");

    this.session = ObjectifyService.begin();
    this.helper.setUp();
    algorithmDao = new AlgorithmDaoDatastore(TestUtil.fixedClock(now), idGenerator);
    algorithm = Algorithm.builder().algorithm("test").description("sample desc").userId("abc").isAutoScan(true).build();
  }

  @After
  public void after() {
    this.session.close();
    this.helper.tearDown();
  }

  @Test
  public void testSaveAndFind_shouldSaveAndReturnAlgorithm() {
    assertThat(algorithmDao.findAlgorithms().now()).isEmpty();
    final Algorithm savedAlgorithm = algorithmDao.save(algorithm);
    assertThat(algorithmDao.findAlgorithms().now()).containsExactly(savedAlgorithm);
  }

  @Test
  public void testFindById_shouldReturnAlgorithmWithGivenId() {
    final Algorithm savedAlgorithm = algorithmDao.save(algorithm);
    assertThat(algorithmDao.findOneByAlgorithmId("test_id")).hasValue(savedAlgorithm);
    assertThat(algorithmDao.findOneByAlgorithmId("unknown_id")).isEmpty();
  }

  @Test
  public void testFindByUserId_shouldReturnAlgorithmWithGivenUserId() {
    final Algorithm savedAlgorithm = algorithmDao.save(algorithm);
    assertThat(algorithmDao.findAlgorithms().withUserId("abc").now()).containsExactly(savedAlgorithm);
    assertThat(algorithmDao.findAlgorithms().withUserId("unknown_userId").now()).isEmpty();
  }

  @Test
  public void testFindByScanType_shouldReturnAlgorithmsWithGivenScanType() {
    final Algorithm savedAlgorithm = algorithmDao.save(algorithm);
    assertThat(algorithmDao.findAlgorithms().isAutoScan(true).now()).containsExactly(savedAlgorithm);
    assertThat(algorithmDao.findAlgorithms().isAutoScan(false).now()).isEmpty();
  }
}
