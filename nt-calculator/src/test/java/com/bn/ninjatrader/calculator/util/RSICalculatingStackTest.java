package com.bn.ninjatrader.calculator.util;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.model.deprecated.RSIValue;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.util.TestUtil;
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
      Price price = change(priceChange);
      priceList.add(price);
    }
  }

  @Test
  public void testWithEmptyStack() {
    RSICalculatingStack stack = RSICalculatingStack.withSize(2);
    assertEquals(stack.size(), 0);
    assertEquals(stack.getValue(), Double.NaN);
    assertAvgGainLossEquals(stack, Double.NaN, Double.NaN);
  }

  @Test
  public void testWithZeroLoss() {
    RSICalculatingStack stack = RSICalculatingStack.withSize(1);
    stack.add(change(1));
    assertEquals(stack.getValue(), 100.0);

    stack = RSICalculatingStack.withSize(3);
    stack.add(change(1));
    stack.add(change(2));
    stack.add(change(3));
    assertEquals(stack.getValue(), 100.0);
  }

  @Test
  public void testWithZeroGain() {
    RSICalculatingStack stack = RSICalculatingStack.withSize(1);
    stack.add(change(-1));
    assertEquals(stack.getValue(), 0.0);
    assertAvgGainLossEquals(stack, 0.0, 1.0);

    stack = RSICalculatingStack.withSize(3);
    stack.add(change(-1));
    stack.add(change(-2));
    stack.add(change(-3));
    assertEquals(stack.getValue(), 0.0);
    assertAvgGainLossEquals(stack, 0.0, 2.0);
  }

  @Test
  public void testWithNonSmoothAverage() {
    RSICalculatingStack stack = RSICalculatingStack.withSize(2);
    stack.add(change(1));
    stack.add(change(-1));
    assertEquals(stack.getValue(), 50.0);

    stack = RSICalculatingStack.withSize(2);
    stack.add(change(100));
    stack.add(change(-20));
    assertEquals(stack.getValue(), 83.33);

    stack = RSICalculatingStack.withSize(priceChanges.length);
    stack.addAll(priceList);
    assertEquals(stack.getValue(), 69.46);
  }

  @Test
  public void testWithSmoothAverage() {
    RSICalculatingStack stack = RSICalculatingStack.withSize(priceChanges.length);

    stack.addAll(priceList);
    assertEquals(stack.getValue(), 69.46);
    assertAvgGainLossEquals(stack, 0.3735714285714286, 0.1642857142857143);

    stack.add(change(-0.87));
    assertEquals(stack.getValue(), 61.77);
    assertAvgGainLossEquals(stack, 0.3468877551020409, 0.21469387755102046);

    stack.add(change(-0.45));
    assertEquals(stack.getValue(), 58.18);
    assertAvgGainLossEquals(stack, 0.322110058309038, 0.23150145772594757);

    stack.add(change(-0.08));
    assertEquals(stack.getValue(), 57.54);
    assertAvgGainLossEquals(stack, 0.29910219700124957, 0.22067992503123704);

    stack.add(change(-0.47));
    assertEquals(stack.getValue(), 53.80);
    assertAvgGainLossEquals(stack, 0.27773775435830317, 0.23848850181472012);

    stack.add(change(1.26));
    assertEquals(stack.getValue(), 61.10);
    assertAvgGainLossEquals(stack, 0.3478993433327101, 0.2214536088279544);
  }

  @Test
  public void testContinueFromValue() {
    RSIValue continueFromValue = new RSIValue(null, 61.77, 0.3468877551020409, 0.21469387755102046);
    RSICalculatingStack stack = RSICalculatingStack.withSizeAndContinueFrom(priceChanges.length, continueFromValue);

    assertEquals(stack.getValue(), 61.77);
    assertAvgGainLossEquals(stack, 0.3468877551020409, 0.21469387755102046);

    stack.add(change(-0.45));
    assertEquals(stack.getValue(), 58.18);
    assertAvgGainLossEquals(stack, 0.322110058309038, 0.23150145772594757);
  }

  private void assertAvgGainLossEquals(RSICalculatingStack stack, double expectedAvgGain, double expectedAvgLoss) {
    assertEquals(stack.getAvgGain(), expectedAvgGain, "Avg Gain");
    assertEquals(stack.getAvgLoss(), expectedAvgLoss, "Avg Loss");
  }

  private Price change(double change) {
    Price price = TestUtil.randomPriceBuilder().change(change).build();
    return price;
  }
}
