package com.bn.ninjatrader.queue.config;

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
public class TaskConfig {

  private final Property<String> serviceUrlProperty;
  private final Property<String> serviceApiKeyProperty;

  @Inject
  public TaskConfig(final Config config) {
    final PropertyFactory factory = DefaultPropertyFactory.from(config);
    serviceUrlProperty = factory.getProperty("service.url").asString("");
    serviceApiKeyProperty = factory.getProperty("service.api.key").asString("");
  }

  public String getServiceUrl() {
    return serviceUrlProperty.get();
  }

  public String getServiceApiKey() {
    return serviceApiKeyProperty.get();
  }
}
