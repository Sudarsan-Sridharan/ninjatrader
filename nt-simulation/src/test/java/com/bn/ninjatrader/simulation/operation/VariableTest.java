package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.DataType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class VariableTest {
  private static final Logger LOG = LoggerFactory.getLogger(VariableTest.class);

  private final Variable orig = Variable.of(DataType.PRICE_CLOSE).withPeriod(10);
  private final Variable equal = Variable.of(DataType.PRICE_CLOSE).withPeriod(10);
  private final Variable diffDataType = Variable.of(DataType.PRICE_HIGH).withPeriod(10);
  private final Variable diffPeriod = Variable.of(DataType.PRICE_CLOSE).withPeriod(20);

  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffDataType)
        .isNotEqualTo(diffPeriod);
  }

  @Test
  public void testHashCode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffDataType, diffPeriod))
        .containsExactlyInAnyOrder(orig, diffDataType, diffPeriod);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final Variable variable = Variable.of(DataType.SMA).withPeriod(200);
    final String json = om.writeValueAsString(variable);
    final Operation deserialized = om.readValue(json, Operation.class);
    assertThat(deserialized).isEqualTo(variable);
  }
}
