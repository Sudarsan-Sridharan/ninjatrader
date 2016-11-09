package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.DataType;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
public class Constant implements Operation {

  private static final Constant CONSTANT = new Constant(DataType.CONSTANT);

  public static final Constant constant() { return CONSTANT; }

  public static final Constant of(DataType dataType) {
    return new Constant(dataType);
  }

  public static final Constant withPeriod(DataType dataType, int period) {
    return new Constant(dataType, period);
  }

  private DataType dataType;
  private int period;

  public Constant() {}

  public Constant(DataType dataType) {
    this.dataType = dataType;
  }

  public Constant(DataType dataType, int period) {
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

  public Constant period(int period) {
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
  public int hashCode() {
    return new HashCodeBuilder()
        .append(dataType)
        .append(period)
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
        .append(dataType, rhs.dataType)
        .append(period, rhs.period)
        .build();
  }

  @Override
  public double getValue(BarData barData) {
    return barData.get(this);
  }

  @Override
  public Set<Constant> getVariables() {
    return Sets.newHashSet(this);
  }

  @Override
  public OperationType getOperationType() {
    return OperationType.VARIABLE;
  }
}
