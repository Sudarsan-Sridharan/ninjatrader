package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtPrice implements OrderType {

  public static final AtPrice of(final double price) {
    return new AtPrice(price);
  }

  @JsonProperty("price")
  private final double price;

  public AtPrice(@JsonProperty("price") final double price) {
    this.price = price;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public boolean isFulfillable(final BarData barData) {
    final Price barPrice = barData.getPrice();
    return price >= barPrice.getLow() && price <= barPrice.getHigh();
  }

  @Override
  public double getFulfilledPrice(final BarData barData) {
    if (isFulfillable(barData)) {
      return price;
    }
    return Double.NaN;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof AtPrice)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final AtPrice rhs = (AtPrice) obj;
    return Objects.equal(price, rhs.price);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(price);
  }


}
