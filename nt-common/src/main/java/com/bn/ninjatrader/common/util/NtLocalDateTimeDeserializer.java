package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Brad on 6/3/16.
 */
public class NtLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

  public NtLocalDateTimeDeserializer() {
    super(DateFormats.DB_DATE_TIME_FORMAT);
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String string = p.getText().trim();
    if (string.length() == 0) {
      return null;
    }

    System.out.println("SHITAKE MUSHROOMS: " + string);

    return LocalDateTime.parse(string, DateFormats.DB_DATE_TIME_FORMAT);
  }

}
