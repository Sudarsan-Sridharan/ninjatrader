package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 7/27/16.
 */
public class FindRequest {

  private String symbol;
  private int period;
  private LocalDate fromDate;
  private LocalDate toDate;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;

  public static FindRequest findSymbol(String symbol) {
    return new FindRequest(symbol);
  }

  public FindRequest() {}

  public FindRequest(String symbol, TimeFrame timeFrame, int period, LocalDate from, LocalDate to) {
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
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("timeframe", timeFrame)
        .append("period", period)
        .append("from", fromDate)
        .append("to", toDate)
        .build();
  }
}
