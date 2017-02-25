package com.bn.ninjatrader.logical.expression.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author bradwee2000@gmail.com
 */
public class TestUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static ObjectMapper om() {
    return objectMapper;
  }
}
