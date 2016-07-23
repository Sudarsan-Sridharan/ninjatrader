package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 7/11/16.
 */
public class MovingAverageCalculatingStack extends FixedStack<Price> {

  private static final Logger log = LoggerFactory.getLogger(MovingAverageCalculatingStack.class);

  private double totalSum;

  public static MovingAverageCalculatingStack withFixedSize(int fixedSize) {
    return new MovingAverageCalculatingStack(fixedSize);
  }

  private MovingAverageCalculatingStack(int fixedSize) {
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
      return totalSum / getFixedSize();
    }
    return 0;
  }
}
