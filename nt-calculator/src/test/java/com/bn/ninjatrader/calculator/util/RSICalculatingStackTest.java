package com.bn.ninjatrader.calculator.util;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 7/21/16.
 */
public class RSICalculatingStackTest {

  private static final Logger log = LoggerFactory.getLogger(RSICalculatingStackTest.class);

  private List<Price> priceList = Lists.newArrayList();
  private double[] priceChanges = new double[] {1.11, 0.24, -0.27, -0.15, -0.05, 0.80, 0.86, -0.15, 1.71, -0.02,-1.03,
      -0.63, 0.04, 0.47};

  @BeforeClass
  public void setupPrices() {
    for (double priceChange : priceChanges) {
      Price price = createPriceWithChange(priceChange);
      priceList.add(price);
    }
  }

  @Test
  public void testWithEmptyStack() {
    RSICalculatingStack stack = RSICalculatingStack.withFixedSize(2);
    assertEquals(stack.size(), 0);
    assertEquals(stack.getValue(), Double.NaN);
  }

  @Test
  public void testWithZeroLoss() {
    RSICalculatingStack stack = RSICalculatingStack.withFixedSize(1);
    stack.add(createPriceWithChange(1));
    assertEquals(stack.getValue(), 100.0);

    stack = RSICalculatingStack.withFixedSize(3);
    stack.add(createPriceWithChange(1));
    stack.add(createPriceWithChange(2));
    stack.add(createPriceWithChange(3));
    assertEquals(stack.getValue(), 100.0);
  }

  @Test
  public void testWithZeroGain() {
    RSICalculatingStack stack = RSICalculatingStack.withFixedSize(1);
    stack.add(createPriceWithChange(-1));
    assertEquals(stack.getValue(), 0.0);

    stack = RSICalculatingStack.withFixedSize(3);
    stack.add(createPriceWithChange(-1));
    stack.add(createPriceWithChange(-2));
    stack.add(createPriceWithChange(-3));
    assertEquals(stack.getValue(), 0.0);
  }

  @Test
  public void testWithNonSmoothAverage() {
    RSICalculatingStack stack = RSICalculatingStack.withFixedSize(2);
    stack.add(createPriceWithChange(1));
    stack.add(createPriceWithChange(-1));
    assertEquals(stack.getValue(), 50.0);

    stack = RSICalculatingStack.withFixedSize(2);
    stack.add(createPriceWithChange(100));
    stack.add(createPriceWithChange(-20));
    assertEquals(stack.getValue(), 83.33);

    stack = RSICalculatingStack.withFixedSize(priceChanges.length);
    stack.addAll(priceList);
    assertEquals(stack.getValue(), 69.46);
  }

  @Test
  public void testWithSmoothAverage() {
    RSICalculatingStack stack = RSICalculatingStack.withFixedSize(priceChanges.length);

    stack.addAll(priceList);
    assertEquals(stack.getValue(), 69.46);

    stack.add(createPriceWithChange(-0.87));
    assertEquals(stack.getValue(), 61.77);

    stack.add(createPriceWithChange(-0.45));
    assertEquals(stack.getValue(), 58.18);

    stack.add(createPriceWithChange(-0.08));
    assertEquals(stack.getValue(), 57.54);

    stack.add(createPriceWithChange(-0.47));
    assertEquals(stack.getValue(), 53.80);

    stack.add(createPriceWithChange(1.26));
    assertEquals(stack.getValue(), 61.10);
  }

  private Price createPriceWithChange(double change) {
    Price price = TestUtil.randomPrice();
    price.setChange(change);
    return price;
  }
}
