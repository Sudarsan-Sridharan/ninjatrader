package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.DataMap;
import org.junit.Test;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static com.bn.ninjatrader.simulation.type.Operator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/2/16.
 */
public class BinaryOperationTest {

  private final Price price = Price.builder().open(1).high(2).low(3).close(4).volume(10000).build();
  private final DataMap dataMap = DataMap.newInstance()
      .addData(PRICE_OPEN, 1.0)
      .addData(PRICE_HIGH, 2.0)
      .addData(PRICE_LOW, 3.0)
      .addData(PRICE_CLOSE, 4.0)
      .addData(VOLUME, 10000d);
  private BarData barData = BarData.builder().price(price).addData(dataMap).build();


  @Test
  public void testGetValueOnEquationWithOnlyConstants_shouldReturnCalculatedValue() {
    assertThat(BinaryOperation.of(5.6, PLUS, 5.8).getValue(barData)).isEqualTo(11.4);
    assertThat(BinaryOperation.of(-1.1, PLUS, 1.1).getValue(barData)).isEqualTo(0);
    assertThat(BinaryOperation.of(0, PLUS, -0).getValue(barData)).isEqualTo(0);

    assertThat(BinaryOperation.of(5.1, MINUS, 4.1).getValue(barData)).isEqualTo(1.0);
    assertThat(BinaryOperation.of(0, MINUS, 0).getValue(barData)).isEqualTo(0);
    assertThat(BinaryOperation.of(1, MINUS, 2).getValue(barData)).isEqualTo(-1);
    assertThat(BinaryOperation.of(1, MINUS, -1).getValue(barData)).isEqualTo(2);
  }

  @Test
  public void testGetValueOnEquationWithVariables_shouldReturnCalculatedValue() {
    assertThat(BinaryOperation.of(PRICE_CLOSE, PLUS, PRICE_HIGH).getValue(barData)).isEqualTo(6.0);
    assertThat(BinaryOperation.of(PRICE_CLOSE, PLUS, PRICE_CLOSE).getValue(barData)).isEqualTo(8.0);
  }

  @Test
  public void testGetVariablesOnConstantOnlyEquation_shouldReturnEmpty() {
    assertThat(BinaryOperation.of(1.0, PLUS, 2.0).getVariables()).isEmpty();
  }

  @Test
  public void testGetVariables_shouldReturnAllVariablesInEquation() {
    assertThat(BinaryOperation.of(PRICE_OPEN, PLUS, PRICE_OPEN).getVariables())
        .containsExactlyInAnyOrder(PRICE_OPEN);

    assertThat(BinaryOperation.of(PRICE_OPEN, PLUS, PRICE_CLOSE).getVariables())
        .containsExactlyInAnyOrder(PRICE_OPEN, PRICE_CLOSE);
  }

  @Test
  public void testGetVariablesOfNestedOperations_shouldReturnAllVariablesInEquation() {
    BinaryOperation operation = BinaryOperation.of(PRICE_OPEN, PLUS, PRICE_CLOSE);
    operation = BinaryOperation.of(operation, MINUS, 1.0);
    operation = BinaryOperation.of(operation, MULTIPLY, PRICE_HIGH);

    assertThat(operation.getVariables()).containsExactlyInAnyOrder(PRICE_OPEN, PRICE_CLOSE, PRICE_HIGH);
  }
}
