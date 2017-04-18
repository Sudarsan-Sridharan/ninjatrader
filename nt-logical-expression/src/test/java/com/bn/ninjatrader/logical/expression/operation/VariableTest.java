package com.bn.ninjatrader.logical.expression.operation;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class VariableTest {
  private static final Logger LOG = LoggerFactory.getLogger(VariableTest.class);

  private final Variable orig = Variable.of("DataType1").withPeriod(10);
  private final Variable equal = Variable.of("DataType1").withPeriod(10);
  private final Variable diffDataType = Variable.of("DataType2").withPeriod(10);
  private final Variable diffPeriod = Variable.of("DataType3").withPeriod(20);

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
}
