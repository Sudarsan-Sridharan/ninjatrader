package com.bn.ninjatrader.common.util;

import com.google.common.base.Preconditions;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

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
}
