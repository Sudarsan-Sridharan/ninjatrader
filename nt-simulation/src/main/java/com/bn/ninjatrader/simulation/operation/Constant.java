package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
public class Constant implements Operation {

  private double value;

  public static final Constant of(final double value) {
    return new Constant(value);
  }

  public Constant(@JsonProperty("value") final double value) {
    this.value = value;
  }

  @Override
  public double getValue(BarData barData) {
    return value;
  }

  @JsonProperty("value")
  public double getValue() {
    return value;
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("value", value).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Constant)) { return false; }
    if (obj == this) { return true; }
    final Constant rhs = (Constant) obj;
    return Objects.equal(value, rhs.value);
  }
}
