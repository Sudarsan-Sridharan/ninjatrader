package com.bn.ninjatrader.service.provider;

import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.bn.ninjatrader.service.event.message.ImportedFullPricesMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * @author bradwee2000@gmail.com
 */
@Provider
@Singleton
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperContextResolver.class);

  private final ObjectMapper OM;

  @Inject
  public ObjectMapperContextResolver(final ObjectMapperProvider objectMapperProvider) {
    OM = objectMapperProvider.get();
    OM.registerSubtypes(ImportedFullPricesMessage.class);
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return OM;
  }
}
