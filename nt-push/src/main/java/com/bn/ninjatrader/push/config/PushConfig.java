package com.bn.ninjatrader.push.config;

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
public class PushConfig {

  private final Property<String> appIdProperty;
  private final Property<String> keyProperty;
  private final Property<String> secretProperty;
  private final Property<String> clusterProperty;

  @Inject
  public PushConfig(final Config config) {
    final PropertyFactory propertyFactory = DefaultPropertyFactory.from(config);
    appIdProperty = propertyFactory.getProperty("pusher.app.id").asString("");
    keyProperty = propertyFactory.getProperty("pusher.key").asString("");
    secretProperty = propertyFactory.getProperty("pusher.secret").asString("");
    clusterProperty = propertyFactory.getProperty("pusher.cluster").asString("");
  }

  public String getAppId() {
    return appIdProperty.get();
  }

  public String getKey() {
    return keyProperty.get();
  }

  public String getSecret() {
    return secretProperty.get();
  }

  public String getCluster() {
    return clusterProperty.get();
  }
}
