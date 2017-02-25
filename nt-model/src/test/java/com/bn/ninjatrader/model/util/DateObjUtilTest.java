package com.bn.ninjatrader.model.util;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.model.deprecated.Value;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/28/16.
 */
public class DateObjUtilTest {

  @Test
  public void testTrimToDateRange_shouldRemoveDatesOutsideOfGivenRange() {
    // Prepare data
    final LocalDate date1 = LocalDate.of(2014, 1, 1);
    final LocalDate date2 = LocalDate.of(2016, 2, 2);
    final LocalDate date3 = LocalDate.of(2017, 3, 3);

    final Value value1 = new Value(date1, 100d);
    final Value value2 = new Value(date2, 200d);
    final Value value3 = new Value(date3, 300d);

    final List<Value> values = Lists.newArrayList(value1, value2, value3);

    DateObjUtil.trimToDateRange(values, date1, date3);
    assertThat(values).containsExactly(value1, value2, value3);

    DateObjUtil.trimToDateRange(values, date1.plusDays(1), date3);
    assertThat(values).containsExactly(value2, value3);

    DateObjUtil.trimToDateRange(values, date1.plusDays(1), date3.minusDays(1));
    assertThat(values).containsExactly(value2);
  }
}
