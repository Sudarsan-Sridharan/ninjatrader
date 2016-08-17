package com.bn.ninjatrader.process.request;

import com.bn.ninjatrader.common.data.Stock;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 7/28/16.
 */
public class CalcRequest {

  private String symbol;
  private LocalDate fromDate;
  private LocalDate toDate;
  private int[] periods;

  public static CalcRequest forSymbol(String symbol) {
    return new CalcRequest(symbol);
  }

  public static CalcRequest forStock(Stock stock) {
    Preconditions.checkNotNull(stock);
    return new CalcRequest(stock.getSymbol());
  }

  private CalcRequest(String symbol) {
    this.symbol = symbol;
  }

  public CalcRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public CalcRequest from(LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public CalcRequest to(LocalDate toDate) {
    this.toDate = toDate;
    return this;
  }

  public CalcRequest periods(int period) {
    this.periods = new int[] { period };
    return this;
  }

  public CalcRequest periods(int ... periods) {
    this.periods = periods;
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

  public int[] getPeriods() {
    return periods;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("Symbol", symbol)
        .append("From", fromDate)
        .append("To", toDate)
        .build();
  }
}
