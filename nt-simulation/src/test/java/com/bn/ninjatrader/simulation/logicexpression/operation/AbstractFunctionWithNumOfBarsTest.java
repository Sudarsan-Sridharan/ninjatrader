package com.bn.ninjatrader.simulation.logicexpression.operation;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class AbstractFunctionWithNumOfBarsTest {

  private AbstractFunctionWithNumOfBars orig = new MockedClass(Variable.of("VAR1"), 1);
  private AbstractFunctionWithNumOfBars equal = new MockedClass(Variable.of("VAR1"), 1);
  private AbstractFunctionWithNumOfBars diffOp = new MockedClass(Variable.of("VAR2"), 1);
  private AbstractFunctionWithNumOfBars diffNumOfBars = new MockedClass(Variable.of("VAR2"), 2);

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(orig.getOperation()).isEqualTo(Variable.of("VAR1"));
    assertThat(orig.getNumOfBarsAgo()).isEqualTo(1);
    assertThat(orig.getVariables()).containsExactly(Variable.of("VAR1"));
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(diffNumOfBars)
        .isNotEqualTo(diffOp);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffNumOfBars, diffOp))
        .containsExactlyInAnyOrder(orig, diffNumOfBars, diffOp);
  }

  /**
   * Mocked class
   */
  public static final class MockedClass extends AbstractFunctionWithNumOfBars {

    public MockedClass(@JsonProperty("operation") Operation<BarData> operation,
                       @JsonProperty("numOfBarsAgo") int numOfBarsAgo) {
      super(operation, numOfBarsAgo);
    }

    @Override
    public double getValue(final BarData barData) {
      return 0;
    }

    @Override
    public String toString(BarData barData) {
      return "";
    }
  }
}
