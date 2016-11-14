package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author bradwee2000@gmail.com
 */
public class FindBeforeDateRequest {

  public static final EntityIdBuilder builder() {
    return new EntityIdBuilder();
  }

  private final String symbol;
  private final TimeFrame timeFrame;
  private final int period;

  private FindBeforeDateRequest(final String symbol, final TimeFrame timeFrame, final int period) {
    this.symbol = symbol;
    this.timeFrame = timeFrame;
    this.period = period;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getPeriod() {
    return period;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("timeFrame", timeFrame)
        .append("period", period)
        .toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof FindBeforeDateRequest)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    FindBeforeDateRequest rhs = (FindBeforeDateRequest) obj;
    return new EqualsBuilder()
        .append(symbol, rhs.symbol)
        .append(timeFrame, rhs.timeFrame)
        .append(period, rhs.period)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(symbol).append(timeFrame).append(period).toHashCode();
  }

  /**
   * Builder for EntityId.
   */
  public static final class EntityIdBuilder {
    private String symbol;
    private TimeFrame timeFrame;
    private int period;

    public EntityIdBuilder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public EntityIdBuilder timeFrame(final TimeFrame timeFrame) {
      this.timeFrame = timeFrame;
      return this;
    }

    public EntityIdBuilder symbol(final int period) {
      this.period = period;
      return this;
    }

    public FindBeforeDateRequest build() {
      return new FindBeforeDateRequest(symbol, timeFrame, period);
    }
  }
}
