package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.operation.Constant;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.exception.OrderUnfulfillableException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtPrice implements OrderType {

  public static final AtPrice of(final Operation<BarData> price) {
    return new AtPrice(price);
  }

  public static final AtPrice of(final double price) {
    return new AtPrice(Constant.of(price));
  }

  @JsonProperty("price")
  private final Operation<BarData> price;

  public AtPrice(final double price) {
    this(Constant.of(price));
  }

  public AtPrice(@JsonProperty("price") final Operation<BarData> price) {
    this.price = price;
  }

  public Operation<BarData> getPrice() {
    return price;
  }

  @Override
  public boolean isFulfillable(final BarData barData) {
    final Price barPrice = barData.getPrice();
    final double priceValue = price.getValue(barData);
    return priceValue >= barPrice.getLow() && priceValue <= barPrice.getHigh();
  }

  @Override
  public double getFulfilledPrice(final BarData barData) {
    if (isFulfillable(barData)) {
      return price.getValue(barData);
    }
    throw new OrderUnfulfillableException(this, barData);
  }

  @Override
  public Set<Variable> getVariables() {
    return price.getVariables();
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("price", price).toString();
  }
}
