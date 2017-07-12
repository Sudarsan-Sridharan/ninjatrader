package com.bn.ninjatrader.service.provider;

import com.bn.ninjatrader.model.jackson.PriceModuleProvider;
import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperContextResolver.class);

  private final ObjectMapper OM;

  @Inject
  public ObjectMapperContextResolver(final ObjectMapperProvider objectMapperProvider,
                                     final PriceModuleProvider priceModuleProvider) {
    OM = objectMapperProvider.get()
        .registerModule(priceModuleProvider.provide())
        .registerModule(new JavaTimeModule()
            .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.BASIC_ISO_DATE))
            .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.BASIC_ISO_DATE))
        );
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return OM;
  }

  public ObjectMapper get() {
    return OM;
  }
}
