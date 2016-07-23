package com.bn.ninjatrader.common.util;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Value;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/28/16.
 */
public class DateObjUtilTest {

  @Test
  public void testTrimToDateRange() {
    // Prepare data
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 2, 2);
    LocalDate date3 = LocalDate.of(2017, 3, 3);

    Value value1 = new Value(date1, 100d);
    Value value2 = new Value(date2, 200d);
    Value value3 = new Value(date3, 300d);

    List<Value> values = Lists.newArrayList(value1, value2, value3);

    DateObjUtil.trimToDateRange(values, date1, date3);

    assertEquals(values.size(), 3);
    assertEquals(values.get(0).getDate(), date1);
    assertEquals(values.get(1).getDate(), date2);
    assertEquals(values.get(2).getDate(), date3);

    DateObjUtil.trimToDateRange(values, date1.plusDays(1), date3);
    assertEquals(values.size(), 2);
    assertEquals(values.get(0).getDate(), date2);
    assertEquals(values.get(1).getDate(), date3);

    DateObjUtil.trimToDateRange(values, date1.plusDays(1), date3.minusDays(1));
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date2);
  }
}
