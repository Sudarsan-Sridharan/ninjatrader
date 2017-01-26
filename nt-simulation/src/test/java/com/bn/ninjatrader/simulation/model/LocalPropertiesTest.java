package com.bn.ninjatrader.simulation.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalPropertiesTest {

  private LocalProperties properties;

  @Before
  public void before() {
    properties = new LocalProperties();
  }

  @Test
  public void testPutDiffDataType_shouldOverwriteOldValue() {
    properties.put("TEST_KEY", 1.0).put("TEST_KEY", true);

    assertThat(properties.size()).isEqualTo(1);
    assertThat(properties.getDouble("TEST_KEY")).isNull();
    assertThat(properties.getBoolean("TEST_KEY")).isEqualTo(Boolean.TRUE);

    properties.put("TEST_KEY", 2.0);

    assertThat(properties.getDouble("TEST_KEY")).isEqualTo(2.0);
    assertThat(properties.getBoolean("TEST_KEY")).isNull();
  }

  @Test
  public void testGetNonExistingKey_shouldReturnNull() {
    assertThat(properties.getBoolean("NON_EXISTING")).isNull();
    assertThat(properties.getDouble("NON_EXISTING")).isNull();
  }

  @Test
  public void testRemove_shouldRemovePropertyRegardlessOfDataType() {
    properties.put("KEY1", 1.0).put("KEY2", true);
    properties.remove("KEY1");

    assertThat(properties.getDouble("KEY1")).isNull();
    assertThat(properties.getBoolean("KEY2")).isEqualTo(Boolean.TRUE);

    properties.remove("KEY2");

    assertThat(properties.size()).isEqualTo(0);
  }
}
