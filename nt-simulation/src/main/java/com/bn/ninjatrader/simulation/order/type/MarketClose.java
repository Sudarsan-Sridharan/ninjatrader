package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import java.util.Collections;
import java.util.Set;

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
  public boolean isFulfillable(final BarData onSubmitBarData, final BarData currentBarData) {
    return true;
  }

  @Override
  public double getFulfilledPrice(final BarData onSubmitBarData, final BarData currentBarData) {
    final Price price = currentBarData.getPrice();
    return price.getClose();
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
