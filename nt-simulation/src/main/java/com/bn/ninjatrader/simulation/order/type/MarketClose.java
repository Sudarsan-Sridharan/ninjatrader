package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketClose implements OrderType {

  private static final MarketClose INSTANCE = new MarketClose();

  public static final MarketClose instance() {
    return INSTANCE;
  }

  @Override
  public boolean isFulfillable(final BarData barData) {
    return true;
  }

  @Override
  public double getFulfilledPrice(final BarData barData) {
    final Price price = barData.getPrice();
    return price.getClose();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof MarketClose)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
