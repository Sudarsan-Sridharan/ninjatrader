package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;

/**
 * Created by Brad on 7/11/16.
 */
public class MeanCalculatingStack extends FixedStack<Price> {

  private static final int PRECISION = 10000000;

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
  public void add(Price price) {
    double oldHighest = highest;
    double oldLowest = lowest;

    super.add(price);

    if (oldHighest != highest || oldLowest != lowest) {
      calculateMean();
    }
  }

  @Override
  protected void handleAfterAdd(Price addedPrice) {
    updateHighestAndLowest(addedPrice);
  }

  private void updateHighestAndLowest(Price price) {
    highest = Math.max(price.getHigh(), highest);
    if (lowest == 0) {
      lowest = price.getLow();
    } else {
      lowest = Math.min(price.getLow(), lowest);
    }
  }

  @Override
  protected void handleAfterRemove(Price removedPrice) {
    if (removedPrice.getHigh() == highest) {
      findHighestFromPriceList();
    }
    if (removedPrice.getLow() == lowest) {
      findLowestFromPriceList();
    }
  }

  private void findHighestFromPriceList() {
    highest = 0;
    for (Price price : list) {
      highest = Math.max(price.getHigh(), highest);
    }
  }

  private void findLowestFromPriceList() {
    lowest = 0;
    for (Price price : list) {
      if (lowest == 0) {
        lowest = price.getLow();
      } else {
        lowest = Math.min(price.getLow(), lowest);
      }
    }
  }

  private void calculateMean() {
    if (list.size() != getFixedSize()) {
      mean = 0;
    } else {
      long highestWholeNum = (long) (highest * PRECISION);
      long lowestWholeNum = (long) (lowest * PRECISION);
      mean = ((highestWholeNum + lowestWholeNum) * 1.0) / (2d * PRECISION);
    }
  }

  public double getMean() {
    return mean;
  }
}
