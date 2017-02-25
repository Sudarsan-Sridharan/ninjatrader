package com.bn.ninjatrader.common.util;

import com.beust.jcommander.internal.Lists;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.bn.ninjatrader.common.util.DateUtil.PH_ZONE_ID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/16/16.
 */
public class DateUtilTest {

  @Test
  public void testStartOfWeek() {
    LocalDate date = DateUtil.toStartOfWeek(LocalDate.of(2016, 1, 1));
    assertThat(date).isEqualTo(LocalDate.of(2015, 12, 28));

    date = DateUtil.toStartOfWeek(LocalDate.of(2016, 2, 1));
    assertThat(date).isEqualTo(LocalDate.of(2016, 2, 1));

    date = DateUtil.toStartOfWeek(LocalDate.of(2016, 2, 14));
    assertThat(date).isEqualTo(LocalDate.of(2016, 2, 8));
  }

  @Test
  public void testIsWeekday() {
    assertThat(DateUtil.isWeekday(LocalDate.of(2016, 1, 1))).isTrue();
    assertThat(DateUtil.isWeekday(LocalDate.of(2016, 2, 1))).isTrue();

    assertThat(DateUtil.isWeekday(LocalDate.of(2016, 2, 6))).isFalse();
    assertThat(DateUtil.isWeekday(LocalDate.of(2016, 2, 7))).isFalse();
    assertThat(DateUtil.isWeekday(LocalDate.of(2016, 4, 3))).isFalse();
    assertThat(DateUtil.isWeekday(LocalDate.of(2016, 4, 16))).isFalse();
  }

  @Test
  public void testToListOfString() {
    final LocalDate date1 = LocalDate.of(2015, 12, 1);
    final LocalDate date2 = LocalDate.of(2016, 1, 1);
    final LocalDate date3 = LocalDate.of(2016, 2, 2);

    assertThat(DateUtil.toListOfString(Lists.newArrayList(date1)))
        .containsExactly("20151201");

    assertThat(DateUtil.toListOfString(Lists.newArrayList(date1, date2, date3)))
        .containsExactly("20151201", "20160101", "20160202");
  }

  @Test
  public void testNextWeekday_shouldReturnDateOfNextWeekday() {
    final LocalDate wed = LocalDate.of(2016, 2, 3);
    final LocalDate thur = LocalDate.of(2016, 2, 4);
    final LocalDate fri = LocalDate.of(2016, 2, 5);
    final LocalDate sat = LocalDate.of(2016, 2, 6);
    final LocalDate sun = LocalDate.of(2016, 2, 7);
    final LocalDate mon = LocalDate.of(2016, 2, 8);

    assertThat(DateUtil.nextWeekday(wed)).isEqualTo(thur);
    assertThat(DateUtil.nextWeekday(thur)).isEqualTo(fri);
    assertThat(DateUtil.nextWeekday(fri)).isEqualTo(mon);
    assertThat(DateUtil.nextWeekday(sat)).isEqualTo(mon);
    assertThat(DateUtil.nextWeekday(sun)).isEqualTo(mon);
  }

  @Test
  public void testParse() {
    final LocalDate valueIfEmpty = LocalDate.of(2016, 10, 1);

    assertThat(DateUtil.parse("20160201", valueIfEmpty)).isEqualTo(LocalDate.of(2016, 2, 1));
    assertThat(DateUtil.parse("", valueIfEmpty)).isEqualTo(valueIfEmpty);
    assertThat(DateUtil.parse(null, valueIfEmpty)).isEqualTo(valueIfEmpty);
  }

  @Test
  public void testPhDateNow_shouldReturnPhDateOfToday() {
    final LocalDate now = LocalDate.of(2016, 2, 1);
    final Clock clock = Clock.fixed(now.atStartOfDay(ZoneId.of(PH_ZONE_ID)).toInstant(), ZoneId.of(PH_ZONE_ID));
    assertThat(DateUtil.phNow(clock)).isEqualTo(now);
  }
}
