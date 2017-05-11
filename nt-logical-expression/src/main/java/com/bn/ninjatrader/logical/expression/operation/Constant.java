package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.common.util.NumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
public class Constant<T> implements Operation<T> {

  private double value;

  public static Constant of(final double value) {
    return new Constant(value);
  }

  public Constant(@JsonProperty("value") final double value) {
    this.value = value;
  }

  @Override
  public double getValue(T t) {
    return value;
  }

  @JsonProperty("value")
  public double getValue() {
    return value;
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
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

  @Override
  public String toString() {
    return String.valueOf(NumUtil.trim(value, 4));
  }

  @Override
  public String toString(T t) {
    return toString();
  }
}
