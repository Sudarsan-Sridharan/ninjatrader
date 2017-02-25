package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * Created by Brad on 7/27/16.
 */
@Deprecated
public class FindRequest {

  private String symbol;
  private int period;
  private LocalDate fromDate;
  private LocalDate toDate;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;

  public static FindRequest findSymbol(final String symbol) {
    return new FindRequest(symbol);
  }

  public FindRequest() {}

  public FindRequest(final String symbol,
                     final TimeFrame timeFrame,
                     final int period,
                     final LocalDate from,
                     final LocalDate to) {
    this.symbol = symbol;
    this.timeFrame = timeFrame;
    this.period = period;
    this.fromDate = from;
    this.toDate = to;
  }

  private FindRequest(String symbol) {
    this.symbol = symbol;
  }

  public FindRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public FindRequest timeframe(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  public FindRequest period(int period) {
    this.period = period;
    return this;
  }

  public FindRequest from(LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public FindRequest to(LocalDate toDate) {
    this.toDate = toDate;
    return this;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getPeriod() {
    return period;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FindRequest that = (FindRequest) o;
    return period == that.period &&
        Objects.equal(symbol, that.symbol) &&
        Objects.equal(fromDate, that.fromDate) &&
        Objects.equal(toDate, that.toDate) &&
        timeFrame == that.timeFrame;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, period, fromDate, toDate, timeFrame);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("period", period)
        .add("fromDate", fromDate)
        .add("toDate", toDate)
        .add("timeFrame", timeFrame)
        .toString();
  }
}
