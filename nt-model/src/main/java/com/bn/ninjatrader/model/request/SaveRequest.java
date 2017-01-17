package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created by Brad on 7/27/16.
 */
public class SaveRequest<T> {
  private String symbol;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;
  private int period;
  private List<T> values = Lists.newArrayList();

  public static SaveRequest save(final String symbol) {
    return new SaveRequest(symbol);
  }

  private SaveRequest(final String symbol) {
    this.symbol = symbol;
  }

  public SaveRequest symbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  public SaveRequest timeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  public SaveRequest period(final int period) {
    this.period = period;
    return this;
  }

  public SaveRequest values(final Collection<T> values) {
    this.values.addAll(values);
    return this;
  }

  public SaveRequest values(final T value, final T ... more) {
    this.values.addAll(Lists.asList(value, more));
    return this;
  }

  public String getSymbol() {
    return symbol;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  public int getPeriod() {
    return period;
  }

  public List<T> getValues() {
    return Lists.newArrayList(values);
  }

  public boolean hasValues() {
    return values != null && !values.isEmpty();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof SaveRequest)) { return false; }
    if (obj == this) { return true; }
    final SaveRequest rhs = (SaveRequest) obj;
    return Objects.equal(symbol, rhs.symbol)
        && Objects.equal(timeFrame, rhs.timeFrame)
        && Objects.equal(period, rhs.period)
        && Objects.equal(values, rhs.values);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, timeFrame, period, values);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("timeFrame", timeFrame)
        .add("period", period)
        .add("values", values)
        .toString();
  }
}
