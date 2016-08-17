package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 7/11/16.
 */
public class SimpleAverageCalculatingStack extends FixedStack<Price> {

  private static final Logger log = LoggerFactory.getLogger(SimpleAverageCalculatingStack.class);

  private double totalSum;

  public static SimpleAverageCalculatingStack withFixedSize(int fixedSize) {
    return new SimpleAverageCalculatingStack(fixedSize);
  }

  private SimpleAverageCalculatingStack(int fixedSize) {
    super(fixedSize);
  }

  @Override
  protected void handleAfterAdd(Price addedPrice) {
    totalSum += addedPrice.getClose();
  }

  @Override
  protected void handleAfterRemove(Price removedPrice) {
    totalSum -= removedPrice.getClose();
  }

  public double getValue() {
    if (size() == getFixedSize()) {
      return NumUtil.divide(totalSum, getFixedSize());
    }
    return 0;
  }
}
