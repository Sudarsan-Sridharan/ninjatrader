package com.bn.ninjatrader.common.util;

import java.time.format.DateTimeFormatter;

/**
 * Created by Brad on 6/3/16.
 */
public class DateFormats {

  private DateFormats() {}

  public static final DateTimeFormatter DB_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
  public static final DateTimeFormatter DB_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
}
