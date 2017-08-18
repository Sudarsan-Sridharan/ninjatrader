package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.model.DateObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Brad on 6/11/16.
 */
public class DateObjUtil {
  private static final Logger LOG = LoggerFactory.getLogger(DateObjUtil.class);

  private DateObjUtil() {}

  public static boolean isDateEquals(final DateObj<? extends DateObj> lhs,
                                     final DateObj<? extends DateObj> rhs) {
    return lhs.getDate().equals(rhs.getDate());
  }

  public static void trimToDateRange(final List<? extends DateObj> list,
                                     final LocalDate fromDate,
                                     final LocalDate toDate) {
    final List<? extends DateObj> removeList = list.parallelStream()
        .filter(d -> d.getDate().isBefore(fromDate) || d.getDate().isAfter(toDate))
        .collect(Collectors.toList());
    list.removeAll(removeList);
  }
}
