package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Calculate for changes in Price (e.g. price difference) between previous price and current price.
 *
 * Created by Brad on 7/11/16.
 */
@Singleton
public class PriceChangeCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(PriceChangeCalculator.class);

  public List<Price> calc(final List<Price> priceList) {
    boolean isFirst = true;
    double prevClose = 0;

    for (final Price price : priceList) {
      if (isFirst) {
        isFirst = false;
      } else {
        price.setChange(NumUtil.minus(price.getClose(), prevClose));
      }
      prevClose = price.getClose();
    }
    return priceList;
  }
}

