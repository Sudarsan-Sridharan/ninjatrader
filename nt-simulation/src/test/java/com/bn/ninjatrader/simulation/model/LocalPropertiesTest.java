package com.bn.ninjatrader.simulation.model;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalPropertiesTest {

  private final LocalProperties orig = new LocalProperties().put("1", 100).put("2", false);
  private final LocalProperties equal = new LocalProperties().put("1", 100).put("2", false);
  private final LocalProperties diffValue1 = new LocalProperties().put("1", 200).put("2", false);
  private final LocalProperties diffValue2 = new LocalProperties().put("1", 100).put("2", true);

  private LocalProperties properties;

  @Before
  public void before() {
    properties = new LocalProperties();
  }

  @Test
  public void testPutDiffDataType_shouldOverwriteOldValue() {
    properties.put("TEST_KEY", 1.0).put("TEST_KEY", false);

    assertThat(properties.size()).isEqualTo(1);
    assertThat(properties.get("TEST_KEY").getValueAsBoolean()).isFalse();

    properties.put("TEST_KEY", 2.0);

    assertThat(properties.get("TEST_KEY").getValue()).isEqualTo(2.0);
  }

  @Test
  public void testGetNonExistingKey_shouldReturnNull() {
    assertThat(properties.get("NON_EXISTING")).isNull();
  }

  @Test
  public void testRemove_shouldRemoveProperty() {
    properties.put("KEY1", 1.0).put("KEY2", true);
    properties.remove("KEY1");

    assertThat(properties.get("KEY1")).isNull();
    assertThat(properties.get("KEY2").getValueAsBoolean()).isTrue();

    properties.remove("KEY2");

    assertThat(properties.size()).isEqualTo(0);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffValue1)
        .isNotEqualTo(diffValue2);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffValue1, diffValue2))
        .containsExactlyInAnyOrder(orig, diffValue1, diffValue2);
  }
}
