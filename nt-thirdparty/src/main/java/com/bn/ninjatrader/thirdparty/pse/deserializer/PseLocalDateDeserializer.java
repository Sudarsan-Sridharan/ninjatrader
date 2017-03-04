package com.bn.ninjatrader.thirdparty.pse.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Brad on 6/3/16.
 */
public class PseLocalDateDeserializer extends JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    String string = p.getText().trim();
    if (string.length() == 0) {
      return null;
    }
    return LocalDate.parse(string.split(" ")[0], DateTimeFormatter.ISO_DATE);
  }
}
