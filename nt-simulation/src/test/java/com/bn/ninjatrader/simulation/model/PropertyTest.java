package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class PropertyTest {

  private final Property orig = Property.of("KEY", 100);
  private final Property equal = Property.of("KEY", 100);
  private final Property diffKey = Property.of("DIFF_KEY", 100);
  private final Property diffValue = Property.of("KEY", 100.01);
  private final Property diffValueType = Property.of("KEY", true);

  @Test
  public void testCreate_shouldSetKeyValue() {
    assertThat(orig.getKey()).isEqualTo("KEY");
    assertThat(orig.getValue()).isEqualTo(100);
    assertThat(diffValue.getValue()).isEqualTo(100.01);
  }

  @Test
  public void testGetValueAsDoubleForBooleanType_shouldReturnAsDouble() {
    assertThat(Property.of("KEY", true).getValue()).isEqualTo(Double.POSITIVE_INFINITY);
    assertThat(Property.of("KEY", false).getValue()).isEqualTo(Double.NaN);
  }

  @Test
  public void testGetValueAsBooleanForDoubleType_shouldReturnAsBoolean() {
    assertThat(Property.of("KEY", 0.01).getValueAsBoolean()).isTrue();
    assertThat(Property.of("KEY", 0.000).getValueAsBoolean()).isTrue();
    assertThat(Property.of("KEY", Double.NaN).getValueAsBoolean()).isFalse();
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffKey)
        .isNotEqualTo(diffValue)
        .isNotEqualTo(diffValueType);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffKey, diffValue, diffValueType))
        .containsExactlyInAnyOrder(orig, diffKey, diffValue, diffValueType);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, Property.class)).isEqualTo(orig);
  }
}
