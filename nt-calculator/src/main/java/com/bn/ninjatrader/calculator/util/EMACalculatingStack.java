package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stack that calculates for Simple Moving Average on each insert.
 *
 * Created by Brad on 7/11/16.
 */
public class EMACalculatingStack extends FixedStack<Price> implements CalculatingStack<Price> {

  private static final Logger log = LoggerFactory.getLogger(EMACalculatingStack.class);

  private double totalSum;

  public static EMACalculatingStack withFixedSize(int fixedSize) {
    return new EMACalculatingStack(fixedSize);
  }

  private EMACalculatingStack(int fixedSize) {
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

  @Override
  public double getValue() {
    if (size() == getFixedSize()) {
      return NumUtil.divide(totalSum, getFixedSize());
    }
    return Double.NaN;
  }
}
