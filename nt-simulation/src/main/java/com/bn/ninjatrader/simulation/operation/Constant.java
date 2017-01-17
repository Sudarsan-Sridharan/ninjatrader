package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("value", value)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(value)
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Constant)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Constant rhs = (Constant) obj;

    return new EqualsBuilder()
        .append(value, rhs.value)
        .build();
  }
}
