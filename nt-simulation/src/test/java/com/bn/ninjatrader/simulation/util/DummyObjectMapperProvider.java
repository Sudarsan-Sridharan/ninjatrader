package com.bn.ninjatrader.simulation.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author bradwee2000@gmail.com
 */
public class DummyObjectMapperProvider {

  private static ObjectMapper om;

  public static ObjectMapper get() {
    if (om == null) {
      om = create();
    }
    return om;
  }

  private static ObjectMapper create() {
    return new ObjectMapper();
  }
}
