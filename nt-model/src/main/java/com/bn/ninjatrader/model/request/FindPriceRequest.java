package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class FindPriceRequest {

  private String symbol;
  private LocalDate fromDate;
  private LocalDate toDate;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;

  public static FindPriceRequest forSymbol(String symbol) {
    return new FindPriceRequest(symbol);
  }

  public FindPriceRequest() {}

  public FindPriceRequest(String symbol, TimeFrame timeFrame, LocalDate from, LocalDate to) {
    this.symbol = symbol;
    this.timeFrame = timeFrame;
    this.fromDate = from;
    this.toDate = to;
  }

  private FindPriceRequest(String symbol) {
    this.symbol = symbol;
  }

  public FindPriceRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public FindPriceRequest timeframe(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  public FindPriceRequest from(LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public FindPriceRequest to(LocalDate toDate) {
    this.toDate = toDate;
    return this;
  }

  public String getSymbol() {
    return symbol;
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
        .append("from", fromDate)
        .append("to", toDate)
        .build();
  }
}
