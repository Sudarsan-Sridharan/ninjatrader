package com.bn.ninjatrader.common.util;

import com.beust.jcommander.internal.Lists;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.*;

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

  @Test
  public void testToListOfString() {
    LocalDate date1 = LocalDate.of(2015, 12, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 1);
    LocalDate date3 = LocalDate.of(2016, 2, 2);

    List<String> result = DateUtil.toListOfString(Lists.newArrayList(date1));
    assertEquals(result.size(), 1);
    assertEquals(result.get(0), "20151201");

    result = DateUtil.toListOfString(Lists.newArrayList(date1, date2, date3));
    assertEquals(result.size(), 3);
    assertEquals(result.get(0), "20151201");
    assertEquals(result.get(1), "20160101");
    assertEquals(result.get(2), "20160202");
  }

  @Test
  public void testNextWeekday() {
    LocalDate wed = LocalDate.of(2016, 2, 3);
    LocalDate thur = LocalDate.of(2016, 2, 4);
    LocalDate fri = LocalDate.of(2016, 2, 5);
    LocalDate sat = LocalDate.of(2016, 2, 6);
    LocalDate sun = LocalDate.of(2016, 2, 7);
    LocalDate mon = LocalDate.of(2016, 2, 8);

    assertEquals(DateUtil.nextWeekday(wed), thur);
    assertEquals(DateUtil.nextWeekday(thur), fri);
    assertEquals(DateUtil.nextWeekday(fri), mon);
    assertEquals(DateUtil.nextWeekday(sat), mon);
    assertEquals(DateUtil.nextWeekday(sun), mon);
  }

  @Test
  public void testParse() {
    LocalDate valueIfEmpty = LocalDate.of(2016, 10, 1);

    assertEquals(DateUtil.parse("20160201", valueIfEmpty), LocalDate.of(2016, 2, 1));
    assertEquals(DateUtil.parse("", valueIfEmpty), valueIfEmpty);
    assertEquals(DateUtil.parse(null, valueIfEmpty), valueIfEmpty);
  }
}
