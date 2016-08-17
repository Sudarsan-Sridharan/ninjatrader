package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Brad on 6/3/16.
 */
public class NtLocalDateTimeSerializer extends JsonSerializer<LocalDate> {

  @Override
  public void serialize(LocalDate localDate, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
    generator.writeString(localDate.format(DateFormats.DB_DATE_FORMAT));
  }
}
