package com.bn.ninjatrader.model.dao.period;

import com.bn.ninjatrader.common.data.Value;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import java.util.List;

/**
 * Created by Brad on 7/27/16.
 */
public class SaveRequest {
  private String symbol;
  private int period;
  private List<Value> values;

  public static SaveRequest save(String symbol) {
    return new SaveRequest(symbol);
  }

  private SaveRequest(String symbol) {
    this.symbol = symbol;
  }

  public SaveRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public SaveRequest period(int period) {
    this.period = period;
    return this;
  }

  public SaveRequest values(List<Value> values) {
    this.values = values;
    return this;
  }

  public SaveRequest values(Value ... values) {
    this.values = Lists.newArrayList(values);
    return this;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getPeriod() {
    return period;
  }

  public List<Value> getValues() {
    return values;
  }

  public boolean hasValues() {
    return values != null && !values.isEmpty();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("period", period)
        .append("values", values)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(symbol)
        .append(period)
        .append(values)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof SaveRequest)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    SaveRequest rhs = (SaveRequest) obj;
    return new EqualsBuilder()
        .append(symbol, rhs.getSymbol())
        .append(period, rhs.getPeriod())
        .append(values, rhs.getValues())
        .isEquals();
  }
}
