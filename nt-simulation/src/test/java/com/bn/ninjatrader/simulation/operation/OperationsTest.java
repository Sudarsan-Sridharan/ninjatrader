package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.DataMap;
import org.junit.Before;
import org.junit.Test;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/2/16.
 */
public class OperationsTest {

  private BarData barData;

  @Before
  public void before() {
    final DataMap dataMap = new DataMap();
    dataMap.put(Variables.PRICE_OPEN, 1.0);
    dataMap.put(Variables.PRICE_HIGH, 2.0);
    dataMap.put(Variables.PRICE_LOW, 3.0);
    dataMap.put(Variables.PRICE_CLOSE, 4.0);
    dataMap.put(Variables.VOLUME, 10000);

    barData = new BarData();
    barData.put(dataMap);
  }


  @Test
  public void testWithSingleOperand() {
    assertThat(Operations.create(1.0).getValue(barData)).isEqualTo(1.0);
    assertThat(Operations.create(PRICE_OPEN).getValue(barData)).isEqualTo(1.0);
  }

  @Test
  public void testWithConstants_shouldReturnCalculatedValue() {
    assertThat(Operations.create(1).plus(2.1).plus(3.2).getValue(barData)).isEqualTo(6.3);
    assertThat(Operations.create(10).minus(4).minus(1).getValue(barData)).isEqualTo(5.0);
    assertThat(Operations.create(10).plus(4).minus(2).getValue(barData)).isEqualTo(12.0);
    assertThat(Operations.create(10).minus(4).plus(9).getValue(barData)).isEqualTo(15.0);
  }

  @Test
  public void testWithVariables_shouldReturnCalculatedValue() {
    assertThat(Operations.create(PRICE_OPEN).plus(PRICE_HIGH).plus(Variables.PRICE_LOW).getValue(barData))
        .isEqualTo(6.0);
    assertThat(Operations.create(PRICE_CLOSE).minus(PRICE_OPEN).minus(PRICE_HIGH).getValue(barData))
        .isEqualTo(1.0);
  }

  @Test
  public void testWithConstantsAndVariables_shouldReturnCalculatedValue() {
    assertThat(Operations.create(10).plus(PRICE_HIGH).getValue(barData)).isEqualTo(12.0);
    assertThat(Operations.create(PRICE_HIGH).minus(3).getValue(barData)).isEqualTo(-1.0);
  }

  @Test
  public void testGetVariables_shouldReturnVariablesUsedInOperation() {
    assertThat(Operations.create(PRICE_HIGH).minus(3).getVariables()).containsExactly(PRICE_HIGH);
    assertThat(Operations.create(PRICE_HIGH).plus(PRICE_HIGH).getVariables()).containsExactly(PRICE_HIGH);
    assertThat(Operations.create(PRICE_HIGH).plus(PRICE_LOW).getVariables())
        .containsExactlyInAnyOrder(PRICE_HIGH, PRICE_LOW);
  }
}
