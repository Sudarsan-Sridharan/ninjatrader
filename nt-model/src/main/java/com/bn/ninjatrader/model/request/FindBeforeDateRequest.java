package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class FindBeforeDateRequest {

  public static final FindBeforeDateRequestBuilder builder() {
    return new FindBeforeDateRequestBuilder();
  }

  private final String symbol;
  private final TimeFrame timeFrame;
  private final int period;
  private final LocalDate beforeDate;
  private final int numOfValues;

  private FindBeforeDateRequest(final String symbol,
                                final TimeFrame timeFrame,
                                final int period,
                                final LocalDate beforeDate,
                                final int numOfValues) {
    this.symbol = symbol;
    this.timeFrame = timeFrame;
    this.period = period;
    this.beforeDate = beforeDate;
    this.numOfValues = numOfValues;
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

  public LocalDate getBeforeDate() {
    return beforeDate;
  }

  public int getNumOfValues() {
    return numOfValues;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("timeFrame", timeFrame)
        .append("period", period)
        .append("beforeDate", beforeDate)
        .append("numOfValues", numOfValues)
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
        .append(beforeDate, rhs.beforeDate)
        .append(numOfValues, rhs.numOfValues)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(symbol)
        .append(timeFrame)
        .append(period)
        .append(beforeDate)
        .append(numOfValues)
        .toHashCode();
  }

  /**
   * Builder for EntityId.
   */
  public static final class FindBeforeDateRequestBuilder {
    private String symbol;
    private TimeFrame timeFrame;
    private int period;
    private LocalDate beforeDate;
    private int numOfValues;

    public FindBeforeDateRequestBuilder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public FindBeforeDateRequestBuilder timeFrame(final TimeFrame timeFrame) {
      this.timeFrame = timeFrame;
      return this;
    }

    public FindBeforeDateRequestBuilder period(final int period) {
      this.period = period;
      return this;
    }

    public FindBeforeDateRequestBuilder beforeDate(final LocalDate beforeDate) {
      this.beforeDate = beforeDate;
      return this;
    }

    public FindBeforeDateRequestBuilder numOfValues(final int numOfValues) {
      this.numOfValues = numOfValues;
      return this;
    }

    public FindBeforeDateRequest build() {
      return new FindBeforeDateRequest(symbol, timeFrame, period, beforeDate, numOfValues);
    }
  }
}
