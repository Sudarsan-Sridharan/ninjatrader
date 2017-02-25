package com.bn.ninjatrader.model.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

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

  public DailyQuote() { }

  public DailyQuote(final String symbol, LocalDate date, double open, double high, double low, double close, long volume) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DailyQuote that = (DailyQuote) o;
    return Double.compare(that.open, open) == 0 &&
        Double.compare(that.high, high) == 0 &&
        Double.compare(that.low, low) == 0 &&
        Double.compare(that.close, close) == 0 &&
        volume == that.volume &&
        Objects.equal(symbol, that.symbol) &&
        Objects.equal(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, date, open, high, low, close, volume);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("date", date)
        .add("open", open)
        .add("high", high)
        .add("low", low)
        .add("close", close)
        .add("volume", volume)
        .toString();
  }

  public Price getPrice(final PriceBuilderFactory priceBuilderFactory) {
    return priceBuilderFactory.builder()
        .date(date).open(open).high(high).low(low).close(close).volume(volume).build();
  }
}
