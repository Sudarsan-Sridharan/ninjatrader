package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Brad on 6/3/16.
 */
public class NtLocalDateTimeDeserializer extends JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String string = p.getText().trim();
    if (string.length() == 0) {
      return null;
    }
    return LocalDate.parse(string, DateFormats.DB_DATE_FORMAT);
  }

}
