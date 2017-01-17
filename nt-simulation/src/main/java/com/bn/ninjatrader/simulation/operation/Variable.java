package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.DataType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
public class Variable implements Operation {

  public static final Variable of(DataType dataType) {
    return new Variable(dataType);
  }

  @JsonProperty("dataType")
  private DataType dataType;

  @JsonProperty("period")
  private int period;

  public Variable(DataType dataType) {
    this(dataType, 0);
  }

  public Variable(@JsonProperty("dataType") final DataType dataType,
                  @JsonProperty("period") final int period) {
    checkNotNull(dataType, "DataType must not be null.");

    this.dataType = dataType;
    this.period = period;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public Variable period(int period) {
    this.period = period;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("dataType", dataType)
        .append("period", period)
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Variable)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Variable rhs = (Variable) obj;

    return new EqualsBuilder()
        .append(dataType, rhs.dataType)
        .append(period, rhs.period)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(dataType)
        .append(period)
        .build();
  }

  @Override
  public double getValue(final BarData barData) {
    return barData.get(this);
  }

  @Override
  public Set<Variable> getVariables() {
    return Sets.newHashSet(this);
  }
}
