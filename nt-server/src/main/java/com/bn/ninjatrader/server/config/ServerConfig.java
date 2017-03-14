package com.bn.ninjatrader.server.config;

import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ServerConfig {
  private static final String DEFAULT_SERVICE_HOST = "http://localhost:8080";

  private final PropertyFactory propertyFactory;

  private final Property<String> serviceHostProperty;

  @Inject
  public ServerConfig(final Config config) {
    this.propertyFactory = DefaultPropertyFactory.from(config);
    this.serviceHostProperty = propertyFactory.getProperty(PropertyNames.SERVICE_HOST).asString(DEFAULT_SERVICE_HOST);
  }

  public String getServiceHost() {
    return serviceHostProperty.get();
  }
}
