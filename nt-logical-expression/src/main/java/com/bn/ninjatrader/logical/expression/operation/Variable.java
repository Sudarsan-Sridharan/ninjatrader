package com.bn.ninjatrader.logical.expression.operation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variable {

  public static Variable of(final String dataType) {
    return new Variable(dataType);
  }

  private final String dataType;

  private final int period;

  public Variable(final String dataType) {
    this(dataType, 0);
  }

  private Variable(final String dataType,
                   final int period) {
    checkNotNull(dataType, "DataType must not be null.");
    this.dataType = dataType;
    this.period = period;
  }

  public String getDataType() {
    return dataType;
  }

  public int getPeriod() {
    return period;
  }

  public Variable withPeriod(final int period) {
    return new Variable(dataType, period);
  }

  public String getName() {
    if (period != 0) {
      return dataType + period;
    }
    return dataType;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Variable)) { return false; }
    if (obj == this) { return true; }
    final Variable rhs = (Variable) obj;
    return Objects.equal(dataType, rhs.dataType)
        && Objects.equal(period, rhs.period);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(dataType, period);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("dataType", dataType).add("period", period).toString();
  }
}
