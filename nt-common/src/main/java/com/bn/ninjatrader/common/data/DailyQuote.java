package com.bn.ninjatrader.common.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 4/27/16.
 */
public class DailyQuote {

  private String symbol;
  private LocalDate date;
  private double open;
  private double high;
  private double low;
  private double close;
  private long volume;

  public DailyQuote() {}

  public DailyQuote(String symbol, LocalDate date, double open, double high, double low, double close, long volume) {
    this.symbol = symbol;
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getClose() {
    return close;
  }

  public void setClose(double close) {
    this.close = close;
  }

  public long getVolume() {
    return volume;
  }

  public void setVolume(long volume) {
    this.volume = volume;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("symbol", symbol)
            .append("D", date)
            .append("O", open)
            .append("H", high)
            .append("L", low)
            .append("C", close)
            .toString();
  }

  public Price toPrice() {
    return new Price(open, high, low, close, volume, date);
  }
}
