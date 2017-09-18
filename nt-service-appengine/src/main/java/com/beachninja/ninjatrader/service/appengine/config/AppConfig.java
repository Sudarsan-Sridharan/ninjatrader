package com.beachninja.ninjatrader.service.appengine.config;

import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AppConfig {
  private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

  private static final String API_KEY = "service.api.key";
  private final PropertyFactory propertyFactory;

  private final Property<String> apiKey;

  @Inject
  public AppConfig(Config config) {
    propertyFactory = DefaultPropertyFactory.from(config);
    apiKey = propertyFactory.getProperty(API_KEY).asString("");
  }

  public String getApiKey() {
    return apiKey.get();
  }
}
