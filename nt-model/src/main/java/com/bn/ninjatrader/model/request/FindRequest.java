package com.bn.ninjatrader.model.request;

import java.time.LocalDate;

/**
 * Created by Brad on 7/27/16.
 */
public class FindRequest {
  private String symbol;
  private int period;
  private LocalDate fromDate;
  private LocalDate toDate;

  public static FindRequest forSymbol(String symbol) {
    return new FindRequest(symbol);
  }

  private FindRequest(String symbol) {
    this.symbol = symbol;
  }

  public FindRequest symbol(String symbol) {
    this.symbol = symbol;
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
}
