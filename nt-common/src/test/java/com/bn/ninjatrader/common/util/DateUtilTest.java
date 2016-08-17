package com.bn.ninjatrader.common.util;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/16/16.
 */
public class DateUtilTest {

  @Test
  public void testStartOfWeek() {
    LocalDate date = DateUtil.toStartOfWeek(LocalDate.of(2016, 1, 1));
    assertEquals(date, LocalDate.of(2015, 12, 28));

    date = DateUtil.toStartOfWeek(LocalDate.of(2016, 2, 1));
    assertEquals(date, LocalDate.of(2016, 2, 1));

    date = DateUtil.toStartOfWeek(LocalDate.of(2016, 2, 14));
    assertEquals(date, LocalDate.of(2016, 2, 8));
  }

  @Test
  public void testIsWeekday() {
    assertTrue(DateUtil.isWeekday(LocalDate.of(2016, 1, 1)));
    assertTrue(DateUtil.isWeekday(LocalDate.of(2016, 2, 1)));

    assertFalse(DateUtil.isWeekday(LocalDate.of(2016, 2, 6)));
    assertFalse(DateUtil.isWeekday(LocalDate.of(2016, 2, 7)));
    assertFalse(DateUtil.isWeekday(LocalDate.of(2016, 4, 3)));
    assertFalse(DateUtil.isWeekday(LocalDate.of(2016, 4, 16)));
  }
}
