package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class EMACalculatingStackTest {

  private final PriceBuilderFactory priceBuilderFactory = new DummyPriceBuilderFactory();
  
  @Test
  public void testCreateEmptyStack_shouldHaveNanValue() {
    assertThat(EMACalculatingStack.withFixedSize(10).getValue()).isEqualTo(Double.NaN);
  }

  @Test
  public void testCalculatedValueOfFirstEMA_shouldReturnSimpleAverage() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSize(5);
    stack.addAll(priceBuilderFactory.builder().close(1).build(),
        priceBuilderFactory.builder().close(2).build(),
        priceBuilderFactory.builder().close(3).build(),
        priceBuilderFactory.builder().close(4).build(),
        priceBuilderFactory.builder().close(5).build());
    assertThat(stack.getValue()).isEqualTo(3);
  }

  @Test
  public void testCalculatedValueOfSecondEMA_shouldReturnExponentialAverage() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSize(5);
    stack.addAll(priceBuilderFactory.builder().close(1).build(),
        priceBuilderFactory.builder().close(2).build(),
        priceBuilderFactory.builder().close(3).build(),
        priceBuilderFactory.builder().close(4).build(),
        priceBuilderFactory.builder().close(5).build(),
        priceBuilderFactory.builder().close(6).build());
    assertThat(stack.getValue()).isEqualTo(4);
  }

  @Test
  public void testEmaAfterAddingMultiplePrices_shouldReturnEmaOfLastAddedPrice() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSize(5);
    stack.addAll(priceBuilderFactory.builder().close(1).build(),
        priceBuilderFactory.builder().close(2).build(),
        priceBuilderFactory.builder().close(3).build(),
        priceBuilderFactory.builder().close(4).build(),
        priceBuilderFactory.builder().close(5).build(),
        priceBuilderFactory.builder().close(6).build(),
        priceBuilderFactory.builder().close(7).build(),
        priceBuilderFactory.builder().close(8).build(),
        priceBuilderFactory.builder().close(9).build()
    );
    assertThat(stack.getValue()).isEqualTo(7);
  }

  @Test
  public void testWithContinueFromEma_shouldCalculateEmaContinuingFromGivenEma() {
    final EMACalculatingStack stack = EMACalculatingStack.withFixedSizeAndPriorValue(5, 10);

    stack.addAll(priceBuilderFactory.builder().close(1).build());
    assertThat(stack.getValue()).isEqualTo(7);

    stack.addAll(priceBuilderFactory.builder().close(2).build());
    assertThat(stack.getValue()).isEqualTo(5.333333333);
  }
}
