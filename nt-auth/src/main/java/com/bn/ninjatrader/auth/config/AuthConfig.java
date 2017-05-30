package com.bn.ninjatrader.auth.config;

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
public class AuthConfig {
  private static final Logger LOG = LoggerFactory.getLogger(AuthConfig.class);
  private static final int DEFAULT_TOKEN_EXPIRY_DAYS = 15;
  private static final String DEFAULT_HSA_KEY = "test_key";
  private static final String DEFAULT_ZONE_ID = "UTC";

  private final PropertyFactory propFactory;
  private final Property<Integer> tokenExpiryInDays;
  private final Property<String> hsaKey;
  private final Property<String> zoneId;

  @Inject
  public AuthConfig(final Config config) {
    this.propFactory = DefaultPropertyFactory.from(config);
    this.tokenExpiryInDays = propFactory.getProperty("auth.token.expiry.days").asInteger(DEFAULT_TOKEN_EXPIRY_DAYS);
    this.hsaKey = propFactory.getProperty("auth.token.hsa.key").asString(DEFAULT_HSA_KEY);
    this.zoneId = propFactory.getProperty("system.time.zone.id").asString(DEFAULT_ZONE_ID);
  }

  public int getTokenExpiryDays() {
    return tokenExpiryInDays.get();
  }

  public String getHsaKey() {
    return hsaKey.get();
  }

  public String getZoneId() {
    return zoneId.get();
  }
}
