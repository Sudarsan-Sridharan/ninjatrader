package com.bn.ninjatrader.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

/**
 * Created by Brad on 8/16/16.
 */
public class DateUtil {

  private DateUtil() {}

  public static LocalDate toStartOfWeek(LocalDate date) {
    Preconditions.checkNotNull(date);
    return date.with(WeekFields.ISO.dayOfWeek(), 1);
  }

  public static boolean isWeekday(LocalDate date) {
    Preconditions.checkNotNull(date);
    switch (date.getDayOfWeek()) {
      case SATURDAY:
      case SUNDAY: return false;
      default: return true;
    }
  }

  public static LocalDate nextWeekday(LocalDate date) {
    date = date.plusDays(1);
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
      date = date.plusDays(2);
    } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
      date = date.plusDays(1);
    }
    return date;
  }

  public static List<String> toListOfString(List<LocalDate> dates) {
    List<String> result = Lists.newArrayList();

    for (LocalDate date : dates) {
      result.add(date.format(DateFormats.DB_DATE_FORMAT));
    }

    return result;
  }

  public static LocalDate parse(String date, LocalDate valueIffEmpty) {
    if (StringUtils.isEmpty(date)) {
      return valueIffEmpty;
    } else {
      return LocalDate.parse(date, DateFormats.DB_DATE_FORMAT);
    }
  }
}
