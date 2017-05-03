package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.format.DateTimeFormatter;

/**
 * Created by Brad on 6/3/16.
 */
public class BasicIsoLocalDateDeserializer extends LocalDateDeserializer {

  public BasicIsoLocalDateDeserializer() {
    super(DateTimeFormatter.BASIC_ISO_DATE);
  }
}
