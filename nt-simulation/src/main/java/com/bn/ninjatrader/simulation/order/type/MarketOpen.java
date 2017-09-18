package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.google.common.base.MoreObjects;

/**
 * @author bradwee2000@gmail.com
 */
public class MarketOpen implements OrderType {

  private static final MarketOpen INSTANCE = new MarketOpen();

  public static final MarketOpen instance() {
    return INSTANCE;
  }

  @Override
  public boolean isFulfillable(final BarData onSubmitBarData, final BarData currentBarData) {
    return true;
  }

  @Override
  public double getFulfilledPrice(final BarData currentBarData) {
    final Price price = currentBarData.getPrice();
    return price.getOpen();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof MarketOpen)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
