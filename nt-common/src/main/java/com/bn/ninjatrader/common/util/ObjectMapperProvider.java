package com.bn.ninjatrader.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
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

  @Inject
  public ObjectMapperProvider() {
    OM = createDefaultObjectMapper();
  }

  public ObjectMapper get() {
    return OM;
  }

  private static ObjectMapper createDefaultObjectMapper() {
    return new ObjectMapper()
        .registerModule(new GuavaModule());
  }
}
