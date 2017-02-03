package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.operation.Constant;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtPrice implements OrderType {
  private static final Logger LOG = LoggerFactory.getLogger(AtPrice.class);

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
  public boolean isFulfillable(final BarData onSubmitBarData, final BarData currentBarData) {
    final Price currentPrice = currentBarData.getPrice();
    final double expectedPrice = price.getValue(onSubmitBarData);
    if (expectedPrice == 0.0)
    LOG.info("{} {} between {} and {} -- {}", currentPrice.getDate(), expectedPrice, currentPrice.getLow(), currentPrice.getHigh(), onSubmitBarData.getWorld().getProperties());
    return expectedPrice >= currentPrice.getLow() && expectedPrice <= currentPrice.getHigh();
  }

  @Override
  public double getFulfilledPrice(final BarData onSubmitBarData, final BarData currentBarData) {
    return price.getValue(onSubmitBarData);
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
