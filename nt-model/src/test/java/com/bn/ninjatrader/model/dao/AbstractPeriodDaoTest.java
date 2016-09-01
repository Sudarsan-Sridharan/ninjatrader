package com.bn.ninjatrader.model.dao;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import org.jongo.MongoCollection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.dao.period.SaveRequest.save;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 5/4/16.
 */
public abstract class AbstractPeriodDaoTest extends AbstractDaoTest {

  final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final LocalDate date2 = LocalDate.of(2016, 1, 2);
  private final LocalDate date3 = LocalDate.of(2016, 1, 3);

  private final Value value1 = new Value(date1, 1d);
  private final Value value2 = new Value(date2, 2d);
  private final Value value3 = new Value(date3, 3d);

  private ValueDao dao;

  @BeforeClass
  public void init() {
    this.dao = initDao();
  }

  @BeforeMethod
  @AfterMethod
  public void cleanup() {
    MongoCollection collection = dao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSorting() {
    dao.save(save("X").period(1).values(value3, value1, value2));

    List<Value> result = dao.find(FindRequest.forSymbol("X").period(1).from(date1).to(date3));

    assertEqualValues(result, value1, value2, value3);
  }

  @Test
  public void testSaveForMultipleSymbols() {
    dao.save(save("MEG").period(1).values(value1, value2));
    dao.save(save("BDO").period(1).values(value2, value3));

    List<Value> result = dao.find(FindRequest.forSymbol("MEG").period(1).from(date1).to(date3));
    assertEqualValues(result, value1, value2);

    result = dao.find(FindRequest.forSymbol("BDO").period(1).from(date1).to(date3));
    assertEqualValues(result, value2, value3);
  }

  @Test
  public void testSaveForMultiplePeriods() {
    dao.save(save("MEG").period(1).values(value1, value2));
    dao.save(save("MEG").period(2).values(value2, value3));

    List<Value> result = dao.find(FindRequest.forSymbol("MEG").period(1).from(date1).to(date2));
    assertEqualValues(result, value1, value2);

    result = dao.find(FindRequest.forSymbol("MEG").period(2).from(date1).to(date3));
    assertEqualValues(result, value2, value3);
  }

  @Test
  public void testSaveForMultipleYears() {
    LocalDate diffYearDate = LocalDate.of(2015, 1, 1);
    Value diffYearValue = new Value(diffYearDate, 0.5d);

    dao.save(save("MEG").period(1).values(value3, value1, value2, diffYearValue));

    List<Value> result = dao.find(FindRequest.forSymbol("MEG").period(1).from(diffYearDate).to(date3));
    assertEqualValues(result, diffYearValue, value1, value2, value3);
  }

  @Test
  public void testSaveOverwrite() {
    dao.save(save("MEG").period(1).values(value3, value1, value2));

    Value overwriteValue = new Value(date1, 10d);
    dao.save(save("MEG").period(1).values(overwriteValue));

    List<Value> result;
    result = dao.find(FindRequest.forSymbol("MEG").period(1).from(date1).to(date3));
    assertEqualValues(result, overwriteValue, value2, value3);
  }

  @Test
  public void testFindByDateRange() {
    dao.save(save("MEG").period(26).values(value1, value2, value3));

    List<Value> results = dao.find(FindRequest.forSymbol("MEG").period(26).from(date1).to(date3));
    assertEqualValues(results, value1, value2, value3);

    results = dao.find(FindRequest.forSymbol("MEG").period(26).from(date2).to(date3));
    assertEqualValues(results, value2, value3);

    // Same from and to date
    results = dao.find(FindRequest.forSymbol("MEG").period(26).from(date2).to(date2));
    assertEqualValues(results, value2);

    // Wrong symbol
    results = dao.find(FindRequest.forSymbol("WRONG_SYMBOL").period(26).from(date1).to(date3));
    assertTrue(results.isEmpty());

    // Wrong period
    results = dao.find(FindRequest.forSymbol("MEG").period(20).from(date1).to(date3));
    assertTrue(results.isEmpty());
  }

  private void assertEqualValues(List<Value> actual, Value ... expected) {
    assertEquals(actual, Lists.newArrayList(expected));
  }

  public abstract ValueDao initDao();
}
