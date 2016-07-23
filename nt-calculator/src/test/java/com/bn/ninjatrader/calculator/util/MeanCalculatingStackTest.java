package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 7/11/16.
 */
public class MeanCalculatingStackTest {

  private final Price highPrecisionPrice = new Price(0.00001, 0.00002, 0.00003, 0.00004, 1000);
  private final Price price1 = new Price(1.0, 2.0, 3.0, 4.0, 10000);
  private final Price price2 = new Price(10.0, 20.0, 30.0, 40.0, 20000);
  private final Price price3 = new Price(100.0, 200.0, 300.0, 400.0, 30000);
  private final Price price4 = new Price(1000.0, 2000.0, 3000.0, 4000.0, 40000);

  @Test
  public void testEmptyStack() {
    MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    assertEquals(stack.getMean(), 0d);

    stack = MeanCalculatingStack.withFixedSize(10);
    assertEquals(stack.getMean(), 0d);
  }

  @Test
  public void testCalcMeanOfSinglePrice() {
    MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    stack.add(price1);

    assertEquals(stack.getMean(), 2.5);
  }

  @Test
  public void testCalcMeanWithHighPrecision() {
    MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    stack.add(highPrecisionPrice);

    assertEquals(stack.getMean(), 0.000025);
  }

  @Test
  public void testMultipleInsert() {
    MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(1);
    stack.add(price1);
    stack.add(price2);

    assertEquals(stack.getMean(), 25d);
  }

  @Test
  public void testCalcMeanOfMultiplePrices() {
    MeanCalculatingStack stack = MeanCalculatingStack.withFixedSize(3);

    stack.add(price1);
    assertEquals(stack.getMean(), 0d);

    stack.add(price2);
    assertEquals(stack.getMean(), 0d);

    stack.add(price3);
    assertEquals(stack.getMean(), 101.5);

    stack.add(price4);
    assertEquals(stack.getMean(), 1015d);
  }
}
