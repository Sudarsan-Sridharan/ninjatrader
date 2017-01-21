package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import org.junit.Test;

import static com.bn.ninjatrader.simulation.condition.Conditions.*;
import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/6/16.
 */
public class ConditionsTest {

  private BarData barData = BarData.builder()
      .addData(PRICE_OPEN, 1.1)
      .addData(PRICE_HIGH, 2.2)
      .addData(PRICE_LOW, 3.3)
      .addData(PRICE_CLOSE, 4.4)
      .addData(ICHIMOKU_CHIKOU, 1.1)
      .addData(ICHIMOKU_TENKAN, 1.2)
      .addData(ICHIMOKU_SENKOU_B, 1.5)
      .build();

  @Test
  public void testSingleEqualsCondition_shouldReturnTrueIfEqual() {
    assertThat(create().add(eq(PRICE_CLOSE, PRICE_HIGH)).isMatch(barData)).isFalse();
    assertThat(create().add(eq(PRICE_OPEN, ICHIMOKU_CHIKOU)).isMatch(barData)).isTrue();
    assertThat(create().add(eq(PRICE_OPEN, 1.1)).isMatch(barData)).isTrue();
    assertThat(create().add(eq(PRICE_OPEN, 1.11)).isMatch(barData)).isFalse();
  }

  @Test
  public void testMultipleEqualsCondition_shouldReturnTrueIfAllEqualsMatch() {
    final AndCondition condition = create()
        .add(eq(PRICE_OPEN, ICHIMOKU_CHIKOU))
        .add(eq(PRICE_OPEN, 1.1))
        .add(eq(PRICE_HIGH, 2.2))
        .add(eq(PRICE_LOW, 3.3))
        .add(eq(PRICE_CLOSE, 4.4));
    assertThat(condition.isMatch(barData)).isTrue();

    condition.add(eq(PRICE_OPEN, 0));
    assertThat(condition.isMatch(barData)).isFalse();
  }

  @Test
  public void testGreaterThanCondition_shouldReturnTrueIfLeftGreaterThanRight() {
    assertThat(create().add(gt(PRICE_OPEN, 1.1)).isMatch(barData)).isFalse();
    assertThat(create().add(gt(PRICE_OPEN, 1.099)).isMatch(barData)).isTrue();
    assertThat(create()
        .add(gt(PRICE_CLOSE, PRICE_LOW))
        .add(gt(PRICE_HIGH, PRICE_OPEN))
        .add(gt(ICHIMOKU_SENKOU_B, 1.4))
        .isMatch(barData))
        .isTrue();
    assertThat(create()
        .add(gt(PRICE_CLOSE, PRICE_LOW))
        .add(gt(PRICE_HIGH, PRICE_OPEN))
        .add(gt(ICHIMOKU_SENKOU_B, 1.5))
        .isMatch(barData))
        .isFalse();
  }

  @Test
  public void testGreaterThanOrEqualsCondition_shouldReturnTrueIfLeftGreaterThanOrEqualToRight() {
    assertThat(create().add(gte(PRICE_OPEN, 1.1)).isMatch(barData)).isTrue();
    assertThat(create().add(gte(PRICE_OPEN, 1.11)).isMatch(barData)).isFalse();
    assertThat(create()
        .add(gte(ICHIMOKU_CHIKOU, 1.1))
        .add(gte(ICHIMOKU_TENKAN, ICHIMOKU_CHIKOU))
        .add(gte(PRICE_OPEN, ICHIMOKU_CHIKOU))
        .isMatch(barData))
        .isTrue();
    assertThat(create()
        .add(gte(ICHIMOKU_CHIKOU, 1.11))
        .add(gte(ICHIMOKU_TENKAN, ICHIMOKU_CHIKOU))
        .add(gte(PRICE_OPEN, ICHIMOKU_CHIKOU))
        .isMatch(barData))
        .isFalse();
  }
}
