package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 7/11/16.
 */
public class SMACalculatingStackTest {

  private final PriceBuilderFactory priceBuilderFactory = new DummyPriceBuilderFactory();

  private final Price highPrecisionPrice = priceBuilderFactory.builder()
      .open(0.00001).high(0.00002).low(0.00003).close(0.00004).volume(100).build();
  private final Price price1 = priceBuilderFactory.builder()
      .open(1.0).high(2.0).low(3.0).close(4.0).volume(1000).build();
  private final Price price2 = priceBuilderFactory.builder()
      .open(10.0).high(20.0).low(30.0).close(40.0).volume(2000).build();
  private final Price price3 = priceBuilderFactory.builder()
      .open(100.0).high(200.0).low(300.0).close(400.0).volume(3000).build();
  private final Price price4 = priceBuilderFactory.builder()
      .open(1000.0).high(2000.0).low(3000.0).close(4000.0).volume(4000).build();

  @Test
  public void testEmptyStack() {
    SMACalculatingStack stack = SMACalculatingStack.withFixedSize(1);
    assertEquals(stack.getValue(), Double.NaN);

    stack = SMACalculatingStack.withFixedSize(10);
    assertEquals(stack.getValue(), Double.NaN);
  }

  @Test
  public void testAverageOfSinglePrice() {
    SMACalculatingStack stack = SMACalculatingStack.withFixedSize(1);
    stack.add(price1);

    assertEquals(stack.getValue(), 4.0);
  }

  @Test
  public void testWithHighPrecision() {
    SMACalculatingStack stack = SMACalculatingStack.withFixedSize(1);
    stack.add(highPrecisionPrice);

    assertEquals(stack.getValue(), 0.00004);

    stack = SMACalculatingStack.withFixedSize(2);
    stack.add(highPrecisionPrice);
    stack.add(highPrecisionPrice);

    assertEquals(stack.getValue(), 0.00004);
  }

  @Test
  public void testWithMultipleInsertInSizeOne() {
    SMACalculatingStack stack = SMACalculatingStack.withFixedSize(1);
    stack.add(price1);
    stack.add(price2);

    assertEquals(stack.getValue(), 40d);
  }

  @Test
  public void testWithMultiplePrices() {
    SMACalculatingStack stack = SMACalculatingStack.withFixedSize(3);

    stack.add(price1);
    assertEquals(stack.getValue(), Double.NaN);

    stack.add(price2);
    assertEquals(stack.getValue(), Double.NaN);

    stack.add(price3);
    assertEquals(stack.getValue(), 148d);

    stack.add(price4);
    assertEquals(stack.getValue(), 1480d);
  }

  @Test
  public void testMultiplePricesPrecision() {
    final SMACalculatingStack stack = SMACalculatingStack.withFixedSize(7);
    stack.add(price1);
    stack.add(price1);
    stack.add(price1);
    stack.add(price1);
    stack.add(price1);
    stack.add(price1);
    stack.add(price2); // total is 64.

    assertEquals(stack.getValue(), 9.142857);

    stack.add(price2); // total is 100.

    assertEquals(stack.getValue(), 14.285714);
  }
}
