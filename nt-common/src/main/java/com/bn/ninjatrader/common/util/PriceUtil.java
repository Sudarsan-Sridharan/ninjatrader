package com.bn.ninjatrader.common.util;

import com.bn.ninjatrader.common.data.Price;

import java.util.Collection;

/**
 * Created by Brad on 6/6/16.
 */
public class PriceUtil {

  private PriceUtil() {}

  public static Price createSummary(Collection<Price> prices) {
    Price summaryPrice = new Price();

    boolean isFirst = true;
    for (Price price : prices) {

      // Set open
      if (isFirst) {
        isFirst = false;
        summaryPrice.setDate(price.getDate());
        summaryPrice.setOpen(price.getOpen());
        summaryPrice.setLow(price.getLow());
        summaryPrice.setHigh(price.getHigh());
      } else {

      }

      // Set lowest low
      if (price.getLow() < summaryPrice.getLow()) {
        summaryPrice.setLow(price.getLow());
      }

      // Set highest high
      if (price.getHigh() > summaryPrice.getHigh()) {
        summaryPrice.setHigh(price.getHigh());
      }

      // Set close
      summaryPrice.setClose(price.getClose());
    }
    return summaryPrice;
  }
}
