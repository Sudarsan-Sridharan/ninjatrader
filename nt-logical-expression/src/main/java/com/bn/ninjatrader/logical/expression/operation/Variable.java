package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variable<T extends Data> implements Operation<T> {

  public static final Variable of(final String dataType) {
    return new Variable(dataType);
  }

  @JsonProperty("dataType")
  private final String dataType;

  @JsonProperty("period")
  private final int period;

  private Variable(final String dataType) {
    this(dataType, 0);
  }

  private Variable(@JsonProperty("dataType") final String dataType,
                   @JsonProperty("period") final int period) {
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("dataType", dataType).add("period", period).toString();
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
  public double getValue(final T t) {
    return t.get(this);
  }

  @Override
  public Set<Variable> getVariables() {
    return Sets.newHashSet(this);
  }
}
