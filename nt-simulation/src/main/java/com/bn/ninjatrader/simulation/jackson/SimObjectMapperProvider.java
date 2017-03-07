package com.bn.ninjatrader.simulation.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimObjectMapperProvider {

  private static ObjectMapper OM;

  public ObjectMapper get() {
    if (OM == null) {
      OM = createObjectMapper();
    }
    return OM;
  }

  private ObjectMapper createObjectMapper() {
    return new ObjectMapper().registerModule(new NtSimulationModule());
  }
}
