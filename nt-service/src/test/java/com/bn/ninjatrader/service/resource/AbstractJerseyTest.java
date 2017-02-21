package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.core.Application;

/**
 * @author bradwee2000@gmail.com
 */
public abstract class AbstractJerseyTest extends JerseyTest {

  @Override
  protected Application configure() {
    return configureResource(new ResourceConfig()
        .register(JacksonFeature.class)
        .register(ObjectMapperContextResolver.class)
        .register(LocalDateParamConverterProvider.class));
  }

  @Override
  protected void configureClient(final ClientConfig config) {
    config.register(ObjectMapperContextResolver.class);
  }

  protected abstract ResourceConfig configureResource(final ResourceConfig resourceConfig);
}
