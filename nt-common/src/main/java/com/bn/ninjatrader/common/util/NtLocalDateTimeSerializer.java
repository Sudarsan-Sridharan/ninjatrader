package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * Created by Brad on 6/3/16.
 */
public class NtLocalDateTimeSerializer extends LocalDateTimeSerializer {

  public NtLocalDateTimeSerializer() {
    super(DateFormats.DB_DATE_TIME_FORMAT);
  }
}