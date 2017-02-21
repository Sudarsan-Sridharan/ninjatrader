package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 7/11/16.
 */
public class MeanCalculatingStack extends FixedStack<Price> implements CalculatingStack<Price> {

  private static final Logger log = LoggerFactory.getLogger(MeanCalculatingStack.class);

  private double highest;
  private double lowest;
  private double mean;

  public static MeanCalculatingStack withFixedSize(int fixedSize) {
    return new MeanCalculatingStack(fixedSize);
  }

  private MeanCalculatingStack(int fixedSize) {
    super(fixedSize);
  }

  @Override
  protected void handleAfterAdd(Price addedPrice) {
    updateHighestAndLowest(addedPrice);
  }

  private void updateHighestAndLowest(final Price price) {
    highest = Math.max(price.getHigh(), highest);
    if (lowest == 0) {
      lowest = price.getLow();
    } else {
      lowest = Math.min(price.getLow(), lowest);
    }
    calculateMean();
  }

  @Override
  protected void handleAfterRemove(final Price removedPrice) {
    if (removedPrice.getHigh() == highest) {
      findHighestFromPriceList();
    }
    if (removedPrice.getLow() == lowest) {
      findLowestFromPriceList();
    }
  }

  private void findHighestFromPriceList() {
    highest = 0;
    for (final Price price : getList()) {
      highest = Math.max(price.getHigh(), highest);
    }
  }

  private void findLowestFromPriceList() {
    lowest = 0;
    for (final Price price : getList()) {
      if (lowest == 0) {
        lowest = price.getLow();
      } else {
        lowest = Math.min(price.getLow(), lowest);
      }
    }
  }

  private void calculateMean() {
    if (size() != getFixedSize()) {
      mean = Double.NaN;
    } else {
      double sum = NumUtil.plus(highest, lowest);
      mean = NumUtil.divide(sum, 2);
    }
  }

  @Override
  public double getValue() {
    if (size() != getFixedSize()) {
      return Double.NaN;
    }
    return mean;
  }
}
