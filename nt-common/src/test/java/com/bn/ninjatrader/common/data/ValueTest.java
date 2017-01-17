package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 7/27/16.
 */
public class ValueTest {
  private final LocalDate now = LocalDate.of(2016, 1, 1);

  private final Value orig = Value.of(now, 1);
  private final Value equal = Value.of(now, 1);
  private final Value diffValue = Value.of(now, 1.0000001);
  private final Value diffDate = Value.of(now.plusDays(1), 1);

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffValue)
        .isNotEqualTo(diffDate);
  }

  @Test
  public void testHashCode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffValue, diffDate))
        .containsExactlyInAnyOrder(orig, diffValue, diffDate);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    final Value deserialized = om.readValue(json, Value.class);
    assertThat(deserialized).isEqualTo(orig);
  }
}
