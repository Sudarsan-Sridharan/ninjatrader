package com.bn.ninjatrader.service.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperProvider.class);

  private final ObjectMapper OM;

  @Inject
  public ObjectMapperProvider() {
    OM = createDefaultObjectMapper();
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return OM;
  }

  public ObjectMapper get() {
    return OM;
  }

  private static ObjectMapper createDefaultObjectMapper() {
    return new ObjectMapper()
        .registerModule(new GuavaModule());
  }
}
