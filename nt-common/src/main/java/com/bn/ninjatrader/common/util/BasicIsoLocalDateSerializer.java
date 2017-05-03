package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.format.DateTimeFormatter;

/**
 * Created by Brad on 6/3/16.
 */
public class BasicIsoLocalDateSerializer extends LocalDateSerializer {

  public BasicIsoLocalDateSerializer() {
    super(DateTimeFormatter.BASIC_ISO_DATE);
  }
}
