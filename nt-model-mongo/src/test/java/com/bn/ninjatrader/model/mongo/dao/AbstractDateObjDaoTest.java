package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.deprecated.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.common.collect.Lists;
import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Brad on 5/4/16.
 */
public class AbstractDateObjDaoTest {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractDateObjDaoTest.class);
  private static final int PERIOD = 10;

  private final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final LocalDate date2 = LocalDate.of(2016, 1, 2);
  private final LocalDate date3 = LocalDate.of(2016, 1, 3);

  private final Value value1 = new Value(date1, 1d);
  private final Value value2 = new Value(date2, 2d);
  private final Value value3 = new Value(date3, 3d);

  private final List<Value> values = Lists.newArrayList(value1, value2, value3);

  private MongoCollection collection;
  private AbstractDateObjDao dao;

  @Before
  public void setup() {
    collection = mock(MongoCollection.class);
    dao = new DummyDateObjDao(collection);
  }

  @Test
  public void testFindNBarsBeforeDate_shouldReturnValuesBeforeGivenDate() {
    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(date2).period(PERIOD).build()))
        .containsExactly(value1);

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(date3).period(PERIOD).build()))
        .containsExactly(value2);

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(2).beforeDate(date3).period(PERIOD).build()))
        .containsExactly(value1, value2);

    assertThat(dao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(3).beforeDate(date3).period(PERIOD).build()))
        .containsExactly(value1, value2);
  }

  /**
   * Dummy test class
   */
  private final class DummyDateObjDao extends AbstractDateObjDao {
    public DummyDateObjDao(MongoCollection mongoCollection) {
      super(mongoCollection);
    }

    @Override
    public List find(FindRequest findRequest) {
      LocalDate from = findRequest.getFromDate();
      LocalDate to = findRequest.getToDate();
      List<Value> result = Lists.newArrayList();

      for (Value value : values) {
        if (value.getDate().isEqual(from)
            || value.getDate().isEqual(to)
            || (value.getDate().isBefore(to) && value.getDate().isAfter(from))) {
          result.add(value);
        }
      }
      return result;
    }
  }
}
