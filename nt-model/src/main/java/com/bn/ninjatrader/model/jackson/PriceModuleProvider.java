package com.bn.ninjatrader.model.jackson;

import com.bn.ninjatrader.common.model.Price;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceModuleProvider {

  private final PriceSerializer priceSerializer;
  private final PriceDeserializer priceDeserializer;

  @Inject
  public PriceModuleProvider(final PriceSerializer priceSerializer,
                             final PriceDeserializer priceDeserializer) {
    this.priceSerializer = priceSerializer;
    this.priceDeserializer = priceDeserializer;
  }

  public Module provide() {
    return new SimpleModule()
        .addSerializer(Price.class, priceSerializer)
        .addDeserializer(Price.class, priceDeserializer);
  }
}
