package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.annotation.TestCollection;
import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.jongo.MongoCollection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.SaveRequest.save;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/4/16.
 */
public class AbstractValueDaoTest {

  private static Injector injector;
  private final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final LocalDate date2 = LocalDate.of(2016, 1, 2);
  private final LocalDate date3 = LocalDate.of(2016, 1, 3);

  private final Value value1 = new Value(date1, 1d);
  private final Value value2 = new Value(date2, 2d);
  private final Value value3 = new Value(date3, 3d);

  private DummyValueDao dao;

  @BeforeClass
  public void initInjector() {
    injector = Guice.createInjector(new NtModelTestModule());
  }

  @BeforeMethod
  public void setup() {
    dao = injector.getInstance(DummyValueDao.class);
    dao.getMongoCollection().drop();
  }

  @AfterMethod
  public void cleanup() {
    dao.getMongoCollection().drop();
  }

  @Test
  public void testSorting_shouldSortByDateFromOldestToNewest() {
    dao.save(save("X").period(1).values(value3, value1, value2));

    List<Value> result = dao.find(FindRequest.findSymbol("X").period(1).from(date1).to(date3));

    assertThat(result).containsExactly(value1, value2, value3);
  }

  @Test
  public void testSaveForDiffSymbols_shouldSaveSeparatelyPerSymbol() {
    dao.save(save("MEG").period(1).values(value1, value2));
    dao.save(save("BDO").period(1).values(value2, value3));

    List<Value> result = dao.find(FindRequest.findSymbol("MEG").period(1).from(date1).to(date3));
    assertThat(result).containsExactly(value1, value2);

    result = dao.find(FindRequest.findSymbol("BDO").period(1).from(date1).to(date3));
    assertThat(result).containsExactly(value2, value3);
  }

  @Test
  public void testSaveForDiffPeriods_shouldSaveSeparatelyPerPeriod() {
    dao.save(save("MEG").period(1).values(value1, value2));
    dao.save(save("MEG").period(2).values(value2, value3));

    assertThat(dao.find(FindRequest.findSymbol("MEG").period(1).from(date1).to(date2)))
        .containsExactly(value1, value2);
    assertThat(dao.find(FindRequest.findSymbol("MEG").period(2).from(date1).to(date3)))
        .containsExactly(value2, value3);
  }

  @Test
  public void testSaveForDiffYears_shouldSaveForEachYear() {
    LocalDate diffYearDate = LocalDate.of(2015, 1, 1);
    Value diffYearValue = new Value(diffYearDate, 0.5d);

    dao.save(save("MEG").period(1).values(value3, value1, value2, diffYearValue));

    assertThat(dao.find(FindRequest.findSymbol("MEG").period(1).from(diffYearDate).to(date3)))
        .containsExactly(diffYearValue, value1, value2, value3);
  }

  @Test
  public void testSaveOverwrite_shouldOverwritePreviousValues() {
    dao.save(save("MEG").period(1).values(value3, value1, value2));

    Value overwriteValue = new Value(date1, 10d);
    dao.save(save("MEG").period(1).values(overwriteValue));

    assertThat(dao.find(FindRequest.findSymbol("MEG").period(1).from(date1).to(date3)))
        .containsExactly(overwriteValue, value2, value3);
  }

  @Test
  public void testFindByDateRange_shouldReturnValuesWithinDateRange() {
    dao.save(save("MEG").period(26).values(value1, value2, value3));

    assertThat(dao.find(FindRequest.findSymbol("MEG").period(26).from(date1).to(date3)))
        .containsExactly(value1, value2, value3);

    assertThat(dao.find(FindRequest.findSymbol("MEG").period(26).from(date2).to(date3)))
        .containsExactly(value2, value3);

    // Same from and to date
    assertThat(dao.find(FindRequest.findSymbol("MEG").period(26).from(date2).to(date2)))
        .containsExactly(value2);

    // Wrong symbol
    assertThat(dao.find(FindRequest.findSymbol("WRONG_SYMBOL").period(26).from(date1).to(date3))).isEmpty();

    // Wrong period
    assertThat(dao.find(FindRequest.findSymbol("MEG").period(20).from(date1).to(date3))).isEmpty();
  }

  @Test
  public void testFindNBarsBeforeDate_shouldReturnValuesBeforeDate() {
    dao.save(save("MEG").period(10).values(value1, value2, value3));

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(date2).period(10).build()))
        .containsExactly(value1);

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(date3).period(10).build()))
        .containsExactly(value2);

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(2).beforeDate(date3).period(10).build()))
        .containsExactly(value1, value2);

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(3).beforeDate(date3).period(10).build()))
        .containsExactly(value1, value2);
  }

  /**
   * Dummy test class
   */
  private static final class DummyValueDao extends AbstractValueDao {

    @Inject
    public DummyValueDao(@TestCollection final MongoCollection mongoCollection) {
      super(mongoCollection);
    }
  }
}
