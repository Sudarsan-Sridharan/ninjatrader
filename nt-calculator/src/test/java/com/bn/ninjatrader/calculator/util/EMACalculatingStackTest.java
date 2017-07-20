package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.model.entity.Price;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class EMACalculatingStackTest {

  @Test
  public void testCreateEmptyStack_shouldHaveNanValue() {
    assertThat(EMACalculatingStack.withFixedSize(10).getValue()).isEqualTo(Double.NaN);
  }

  @Test
  public void testCalculatedValueOfFirstEMA_shouldReturnSimpleAverage() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSize(5);
    stack.addAll(Price.builder().close(1).build(),
        Price.builder().close(2).build(),
        Price.builder().close(3).build(),
        Price.builder().close(4).build(),
        Price.builder().close(5).build());
    assertThat(stack.getValue()).isEqualTo(3);
  }

  @Test
  public void testCalculatedValueOfSecondEMA_shouldReturnExponentialAverage() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSize(5);
    stack.addAll(Price.builder().close(1).build(),
        Price.builder().close(2).build(),
        Price.builder().close(3).build(),
        Price.builder().close(4).build(),
        Price.builder().close(5).build(),
        Price.builder().close(6).build());
    assertThat(stack.getValue()).isEqualTo(4);
  }

  @Test
  public void testEmaAfterAddingMultiplePrices_shouldReturnEmaOfLastAddedPrice() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSize(5);
    stack.addAll(Price.builder().close(1).build(),
        Price.builder().close(2).build(),
        Price.builder().close(3).build(),
        Price.builder().close(4).build(),
        Price.builder().close(5).build(),
        Price.builder().close(6).build(),
        Price.builder().close(7).build(),
        Price.builder().close(8).build(),
        Price.builder().close(9).build()
    );
    assertThat(stack.getValue()).isEqualTo(7);
  }

  @Test
  public void testWithContinueFromEma_shouldCalculateEmaContinuingFromGivenEma() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSizeAndPriorValue(5, 10);

    stack.addAll(Price.builder().close(1).build());
    assertThat(stack.getValue()).isEqualTo(7);

    stack.addAll(Price.builder().close(2).build());
    assertThat(stack.getValue()).isEqualTo(5.333333333);
  }
}
