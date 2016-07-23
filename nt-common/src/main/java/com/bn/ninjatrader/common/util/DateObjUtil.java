package com.bn.ninjatrader.common.util;

import com.bn.ninjatrader.common.data.DateObj;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 6/11/16.
 */
public class DateObjUtil {

  private static final Logger log = LoggerFactory.getLogger(DateObjUtil.class);

  private DateObjUtil() {}

  public static boolean isDateEquals(DateObj<? extends DateObj> lhs, DateObj<? extends DateObj> rhs) {
    return lhs.getDate().equals(rhs.getDate());
  }

  public static void trimToDateRange(List<? extends DateObj> list, LocalDate fromDate, LocalDate toDate) {
    List<DateObj> removeList = Lists.newArrayList();

    // Remove all values prior to from date
    for (DateObj dateObj : list) {
      if (dateObj.getDate().isBefore(fromDate)) {
        removeList.add(dateObj);
      } else {
        break;
      }
    }
    list.removeAll(removeList);

    // Reverse to descending order
    Collections.reverse(list);

    // Remove all prices after to date
    removeList.clear();
    for (DateObj dateObj: list) {

      // If future value (no date), continue
      if (dateObj.getDate() == null) {
        continue;
      }

      if (dateObj.getDate().isAfter(toDate)) {
        removeList.add(dateObj);
      } else {
        break;
      }
    }
    list.removeAll(removeList);

    // Reverse to ascending order
    Collections.reverse(list);
  }

  public static <T extends DateObj> Map<Integer, List<T>> splitByYear(List<T> list) {
    Collections.sort(list);
    Map<Integer, List<T>> map = Maps.newHashMap();
    for (T t : list) {

    }
    return map;
  }
}
