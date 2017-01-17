package com.bn.ninjatrader.common.util;

import com.bn.ninjatrader.common.data.Price;

import java.util.Collection;

/**
 * Created by Brad on 6/6/16.
 */
public class PriceUtil {

  private PriceUtil() {}

  public static Price createSummary(final Collection<Price> prices) {
    final Price.Builder summaryPriceBuilder = Price.builder();

    boolean isFirst = true;
    for (final Price price : prices) {

      // Set open
      if (isFirst) {
        isFirst = false;
        summaryPriceBuilder.date(price.getDate());
        summaryPriceBuilder.open(price.getOpen());
        summaryPriceBuilder.high(price.getHigh());
        summaryPriceBuilder.low(price.getLow());
      } else {

      }

      // Set lowest low
      if (price.getLow() < summaryPriceBuilder.getLow()) {
        summaryPriceBuilder.low(price.getLow());
      }

      // Set highest high
      if (price.getHigh() > summaryPriceBuilder.getHigh()) {
        summaryPriceBuilder.high(price.getHigh());
      }

      // Set close
      summaryPriceBuilder.close(price.getClose());
    }
    return summaryPriceBuilder.build();
  }
}
