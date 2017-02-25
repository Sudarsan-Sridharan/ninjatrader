package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.common.util.NumUtil;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
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

  private final PriceBuilderFactory priceBuilderFactory;

  @Inject
  public PriceChangeCalculator(final PriceBuilderFactory priceBuilderFactory) {
    this.priceBuilderFactory = priceBuilderFactory;
  }

  public List<Price> calc(final List<Price> priceList) {
    boolean isFirst = true;
    double prevClose = 0;

    final List<Price> calculatedPrices = Lists.newArrayList();

    for (final Price price : priceList) {
      if (isFirst) {
        isFirst = false;
        calculatedPrices.add(price);
      } else {
        calculatedPrices.add(priceBuilderFactory.builder()
            .copyOf(price)
            .change(NumUtil.minus(price.getClose(), prevClose))
            .build());
      }
      prevClose = price.getClose();
    }
    return priceList;
  }
}

