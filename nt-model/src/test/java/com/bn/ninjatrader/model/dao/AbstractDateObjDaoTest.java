package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindRequest;
import org.jongo.MongoCollection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.SaveRequest.save;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 5/4/16.
 */
public class AbstractDateObjDaoTest {

  private final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final LocalDate date2 = LocalDate.of(2016, 1, 2);
  private final LocalDate date3 = LocalDate.of(2016, 1, 3);

  private final Value value1 = new Value(date1, 1d);
  private final Value value2 = new Value(date2, 2d);
  private final Value value3 = new Value(date3, 3d);

  private ValueDao dao;

  private MongoCollection collection;

  @BeforeMethod
  public void setup() {
    collection = mock(MongoCollection.class);
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

    List<Value> result = dao.find(FindRequest.findSymbol("X").period(1).from(date1).to(date3));

    assertValueEquals(result, value1, value2, value3);
  }

  @Test
  public void testSaveForMultipleSymbols() {
    dao.save(save("MEG").period(1).values(value1, value2));
    dao.save(save("BDO").period(1).values(value2, value3));

    List<Value> result = dao.find(FindRequest.findSymbol("MEG").period(1).from(date1).to(date3));
    assertValueEquals(result, value1, value2);

    result = dao.find(FindRequest.findSymbol("BDO").period(1).from(date1).to(date3));
    assertValueEquals(result, value2, value3);
  }

  @Test
  public void testSaveForMultiplePeriods() {
    dao.save(save("MEG").period(1).values(value1, value2));
    dao.save(save("MEG").period(2).values(value2, value3));

    List<Value> result = dao.find(FindRequest.findSymbol("MEG").period(1).from(date1).to(date2));
    assertValueEquals(result, value1, value2);

    result = dao.find(FindRequest.findSymbol("MEG").period(2).from(date1).to(date3));
    assertValueEquals(result, value2, value3);
  }

  @Test
  public void testSaveForMultipleYears() {
    LocalDate diffYearDate = LocalDate.of(2015, 1, 1);
    Value diffYearValue = new Value(diffYearDate, 0.5d);

    dao.save(save("MEG").period(1).values(value3, value1, value2, diffYearValue));

    List<Value> result = dao.find(FindRequest.findSymbol("MEG").period(1).from(diffYearDate).to(date3));
    assertValueEquals(result, diffYearValue, value1, value2, value3);
  }

  @Test
  public void testSaveOverwrite() {
    dao.save(save("MEG").period(1).values(value3, value1, value2));

    Value overwriteValue = new Value(date1, 10d);
    dao.save(save("MEG").period(1).values(overwriteValue));

    List<Value> result;
    result = dao.find(FindRequest.findSymbol("MEG").period(1).from(date1).to(date3));
    assertValueEquals(result, overwriteValue, value2, value3);
  }

  @Test
  public void testFindByDateRange() {
    dao.save(save("MEG").period(26).values(value1, value2, value3));

    List<Value> results = dao.find(FindRequest.findSymbol("MEG").period(26).from(date1).to(date3));
    assertValueEquals(results, value1, value2, value3);

    results = dao.find(FindRequest.findSymbol("MEG").period(26).from(date2).to(date3));
    assertValueEquals(results, value2, value3);

    // Same from and to date
    results = dao.find(FindRequest.findSymbol("MEG").period(26).from(date2).to(date2));
    assertValueEquals(results, value2);

    // Wrong symbol
    results = dao.find(FindRequest.findSymbol("WRONG_SYMBOL").period(26).from(date1).to(date3));
    assertTrue(results.isEmpty());

    // Wrong period
    results = dao.find(FindRequest.findSymbol("MEG").period(20).from(date1).to(date3));
    assertTrue(results.isEmpty());
  }

  @Test
  public void testFindNBarsBeforeDate() {
    dao.save(save("MEG").period(10).values(value1, value2, value3));

    List<Value> values = dao.findNBarsBeforeDate("MEG", TimeFrame.ONE_DAY, 1, date2, 10);
    assertEquals(values.size(), 1);
    assertValueEquals(values.get(0), value1);

    values = dao.findNBarsBeforeDate("MEG", TimeFrame.ONE_DAY, 1, date3, 10);
    assertEquals(values.size(), 1);
    assertValueEquals(values.get(0), value2);

    values = dao.findNBarsBeforeDate("MEG", TimeFrame.ONE_DAY, 2, date3, 10);
    assertEquals(values.size(), 2);
    assertValueEquals(values.get(0), value1);
    assertValueEquals(values.get(1), value2);

    values = dao.findNBarsBeforeDate("MEG", TimeFrame.ONE_DAY, 3, date3, 10);
    assertEquals(values.size(), 2);
    assertValueEquals(values.get(0), value1);
    assertValueEquals(values.get(1), value2);
  }

  private void assertValueEquals(List<Value> actual, Value ... expected) {
    assertEquals(actual.size(), expected.length);
    for(int i=0; i<actual.size(); i++) {
      assertValueEquals(actual.get(i), expected[i]);
    }
  }

  private void assertValueEquals(Value actual, Value expected) {
    assertEquals(actual.getDate(), expected.getDate());
    assertEquals(actual.getValue(), expected.getValue());
  }

  /**
   * Dummy test class
   */
  private static final class DummyValueDao extends AbstractValueDao {

    public DummyValueDao(MongoCollection mongoCollection) {
      super(mongoCollection);
    }
  }
}
