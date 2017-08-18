package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;

/**
 * @author bradwee2000@gmail.com
 */
public abstract class AbstractJerseyTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractJerseyTest.class);

  private Injector injector; // TODO why use injector?
  private ObjectMapperContextResolver objectMapperContextResolver;

  @Override
  protected Application configure() {
    injector = Guice.createInjector();

    objectMapperContextResolver = injector.getInstance(ObjectMapperContextResolver.class);

    return configureResource(new ResourceConfig()
        .register(JacksonFeature.class)
        .register(objectMapperContextResolver)
        .register(LocalDateParamConverterProvider.class));
  }

  @Override
  protected void configureClient(final ClientConfig config) {
    config.register(objectMapperContextResolver);
  }

  protected abstract ResourceConfig configureResource(final ResourceConfig resourceConfig);

  public Injector getInjector() {
    return injector;
  }
}
