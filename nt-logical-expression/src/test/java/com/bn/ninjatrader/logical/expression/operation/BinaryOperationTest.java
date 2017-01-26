package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import org.junit.Test;

import static com.bn.ninjatrader.logical.expression.model.Operator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/2/16.
 */
public class BinaryOperationTest {

  private final Variable var1 = Variable.of("DataType1");
  private final Variable var2 = Variable.of("DataType2");
  private final Variable var3 = Variable.of("DataType3");
  private final Variable var4 = Variable.of("DataType4");
  private final Variable var5 = Variable.of("DataType5");

  private final Data data = new Data() {
    @Override
    public Double get(final Variable variable) {
      if (variable == var1) {
        return 1.0;
      } else if (variable == var2) {
        return 2.0;
      } else if (variable == var3) {
        return 3.0;
      } else if (variable == var4) {
        return 4.0;
      } else if (variable == var5) {
        return 10000d;
      }
      return 0d;
    }
  };

  @Test
  public void testGetValueOnEquationWithOnlyConstants_shouldReturnCalculatedValue() {
    assertThat(BinaryOperation.of(5.6, PLUS, 5.8).getValue(data)).isEqualTo(11.4);
    assertThat(BinaryOperation.of(-1.1, PLUS, 1.1).getValue(data)).isEqualTo(0);
    assertThat(BinaryOperation.of(0, PLUS, -0).getValue(data)).isEqualTo(0);

    assertThat(BinaryOperation.of(5.1, MINUS, 4.1).getValue(data)).isEqualTo(1.0);
    assertThat(BinaryOperation.of(0, MINUS, 0).getValue(data)).isEqualTo(0);
    assertThat(BinaryOperation.of(1, MINUS, 2).getValue(data)).isEqualTo(-1);
    assertThat(BinaryOperation.of(1, MINUS, -1).getValue(data)).isEqualTo(2);
  }

  @Test
  public void testGetValueOnEquationWithVariables_shouldReturnCalculatedValue() {
    assertThat(BinaryOperation.of(var4, PLUS, var2).getValue(data)).isEqualTo(6.0);
    assertThat(BinaryOperation.of(var4, PLUS, var4).getValue(data)).isEqualTo(8.0);
  }

  @Test
  public void testGetVariablesOnConstantOnlyEquation_shouldReturnEmpty() {
    assertThat(BinaryOperation.of(1.0, PLUS, 2.0).getVariables()).isEmpty();
  }

  @Test
  public void testGetVariables_shouldReturnAllVariablesInEquation() {
    assertThat(BinaryOperation.of(var1, PLUS, var1).getVariables())
        .containsExactlyInAnyOrder(var1);

    assertThat(BinaryOperation.of(var1, PLUS, var4).getVariables())
        .containsExactlyInAnyOrder(var1, var4);
  }

  @Test
  public void testGetVariablesOfNestedOperations_shouldReturnAllVariablesInEquation() {
    BinaryOperation operation = BinaryOperation.of(var1, PLUS, var4);
    operation = BinaryOperation.of(operation, MINUS, 1.0);
    operation = BinaryOperation.of(operation, MULTIPLY, var2);

    assertThat(operation.getVariables()).containsExactlyInAnyOrder(var1, var4, var2);
  }
}
