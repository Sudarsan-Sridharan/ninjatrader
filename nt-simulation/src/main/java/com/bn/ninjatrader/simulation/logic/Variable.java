package com.bn.ninjatrader.simulation.logic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variable implements Serializable {

  public static Variable of(final String dataType) {
    return new Variable(dataType);
  }

  private final String dataType;

  private final int period;

  private Variable() {
    this.dataType = null;
    this.period = 0;
  }

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
    return new Variable(this.dataType, period);
  }

  public String getName() {
    return period == 0 ? dataType : dataType + period;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable variable = (Variable) o;
    return period == variable.period &&
        Objects.equal(dataType, variable.dataType);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(dataType, period);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("dataType", dataType)
        .add("period", period)
        .toString();
  }
}
