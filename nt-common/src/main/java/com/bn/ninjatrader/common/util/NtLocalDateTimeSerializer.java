package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Brad on 6/3/16.
 */
public class NtLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(LocalDateTime localDatetime, JsonGenerator generator, SerializerProvider serializerProvider)
      throws IOException {
    generator.writeString(localDatetime.format(DateFormats.DB_DATE_TIME_FORMAT));
  }
}
