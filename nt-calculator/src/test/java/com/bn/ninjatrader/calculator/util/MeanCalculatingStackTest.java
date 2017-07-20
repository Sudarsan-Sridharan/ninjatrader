package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.model.entity.Price;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 7/11/16.
 */
public class MeanCalculatingStackTest {

  private final Price highPrecisionPrice = Price.builder()
      .open(0.00001).high(0.00002).low(0.00003).close(0.00004).volume(100).build();
  private final Price price1 = Price.builder()
      .open(1.0).high(2.0).low(3.0).close(4.0).volume(1000).build();
  private final Price price2 = Price.builder()
      .open(10.0).high(20.0).low(30.0).close(40.0).volume(2000).build();
  private final Price price3 = Price.builder()
      .open(100.0).high(200.0).low(300.0).close(400.0).volume(3000).build();
  private final Price price4 = Price.builder()
      .open(1000.0).high(2000.0).low(3000.0).close(4000.0).volume(4000).build();

  @Test
  public void testEmptyStackValue_shouldReturnNaN() {
    assertThat(MeanCalculatingStack.withFixedSize(1).getValue()).isEqualTo(Double.NaN);
    assertThat(MeanCalculatingStack.withFixedSize(10).getValue()).isEqualTo(Double.NaN);
  }

  @Test
  public void testCalcMeanOfSinglePrice_shouldReturnAverageOfHighAndLow() {
    final MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    stack.add(price1);
    assertThat(stack.getValue()).isEqualTo(2.5);
  }

  @Test
  public void testCalcMeanWithHighDecimalPrecision_shouldRetainHighDecimalPrecision() {
    final MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    stack.add(highPrecisionPrice);

    assertThat(stack.getValue()).isEqualTo(0.000025);
  }

  @Test
  public void testMultipleInsert_shouldReturnAverageOfHighestAndLowest() {
    final MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    stack.add(price1);
    stack.add(price2);

    assertThat(stack.getValue()).isEqualTo(25d);
  }

  @Test
  public void testMeanValueOfMultiplePrices_shouldReturnAverageOfHighestAndLowestForGivenPeriod() {
    final MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(3);

    stack.add(price1);
    assertThat(stack.getValue()).isEqualTo(Double.NaN);

    stack.add(price2);
    assertThat(stack.getValue()).isEqualTo(Double.NaN);

    stack.add(price3);
    assertThat(stack.getValue()).isEqualTo(101.5);

    stack.add(price4);
    assertThat(stack.getValue()).isEqualTo(1015d);
  }

  @Test
  public void testInsertSameValues_shouldReturnAverageOfPriceHighAndLow() {
    final MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(3);
    stack.add(price1);
    stack.add(price1);
    stack.add(price1);
    assertThat(stack.getValue()).isEqualTo(2.5);
  }
}
