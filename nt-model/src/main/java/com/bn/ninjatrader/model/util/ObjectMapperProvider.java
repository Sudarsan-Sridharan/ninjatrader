package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.jackson.PriceDeserializer;
import com.bn.ninjatrader.model.jackson.PriceSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ObjectMapperProvider {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperProvider.class);

  private static ObjectMapper OM;

  private final PriceSerializer priceSerializer;
  private final PriceDeserializer priceDeserializer;

  @Inject
  public ObjectMapperProvider(final PriceSerializer priceSerializer,
                              final PriceDeserializer priceDeserializer) {
    this.priceSerializer = priceSerializer;
    this.priceDeserializer = priceDeserializer;

    OM = createDefaultObjectMapper();
  }

  public ObjectMapper get() {
    return OM;
  }

  private ObjectMapper createDefaultObjectMapper() {
    final SimpleModule module = new SimpleModule()
        .addSerializer(Price.class, priceSerializer)
        .addDeserializer(Price.class, priceDeserializer);

    return new ObjectMapper()
        .registerModule(module)
        .registerModule(new GuavaModule());
  }
}
