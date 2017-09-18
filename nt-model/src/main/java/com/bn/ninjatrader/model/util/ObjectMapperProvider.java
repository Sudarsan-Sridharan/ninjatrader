package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.jackson.PriceDeserializer;
import com.bn.ninjatrader.model.jackson.PriceSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ObjectMapperProvider {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperProvider.class);

  private static ObjectMapper OM;

  @Inject
  public ObjectMapperProvider() {
    OM = createDefaultObjectMapper();
  }

  public ObjectMapper get() {
    return OM;
  }

  private ObjectMapper createDefaultObjectMapper() {
    return new ObjectMapper()
        .registerModule(new SimpleModule()
            .addSerializer(Price.class, new PriceSerializer())
            .addDeserializer(Price.class, new PriceDeserializer()))
        .registerModule(new GuavaModule())
        .registerModule(new JavaTimeModule()
            .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.BASIC_ISO_DATE))
            .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.BASIC_ISO_DATE))
        );
  }
}
