package com.bn.ninjatrader.service.provider;

import com.bn.ninjatrader.common.util.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperContextResolver.class);

  private final ObjectMapper OM;

  public ObjectMapperContextResolver() {
    OM = new ObjectMapperProvider().get();
  }

  @Inject
  public ObjectMapperContextResolver(final ObjectMapperProvider objectMapperProvider) {
    OM = objectMapperProvider.get();
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return OM;
  }

  public ObjectMapper get() {
    return OM;
  }
}
