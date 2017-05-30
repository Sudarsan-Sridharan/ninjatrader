package com.bn.ninjatrader.auth.config;

import com.netflix.archaius.api.Config;
import com.netflix.archaius.config.DefaultSettableConfig;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class AuthConfigTest {

  @Test
  public void testWithEmptyConfig_shouldSetDefaults() {
    final Config config = new DefaultSettableConfig();
    final AuthConfig authConfig = new AuthConfig(config);

    assertThat(authConfig.getHsaKey()).isEqualTo("test_key");
    assertThat(authConfig.getZoneId()).isEqualTo("UTC");
    assertThat(authConfig.getTokenExpiryDays()).isEqualTo(15);
  }

  @Test
  public void testWithSetConfig_shouldOverwriteDefaults() {
    final DefaultSettableConfig config = new DefaultSettableConfig();
    config.setProperty("auth.token.expiry.days", 100);
    config.setProperty("system.time.zone.id", "Asia/Singapore");
    final AuthConfig authConfig = new AuthConfig(config);

    assertThat(authConfig.getZoneId()).isEqualTo("Asia/Singapore");
    assertThat(authConfig.getTokenExpiryDays()).isEqualTo(100);
  }
}
