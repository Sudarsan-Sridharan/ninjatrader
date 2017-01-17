package com.bn.ninjatrader.common.data;

import com.google.common.base.MoreObjects;

import java.time.LocalDate;

/**
 * Created by Brad on 4/27/16.
 */
public class DailyQuote {

  private String symbol;
  private final Price.Builder price;

  public DailyQuote() { price = Price.builder(); }

  public DailyQuote(String symbol, LocalDate date, double open, double high, double low, double close, long volume) {
    this.symbol = symbol;
    price = Price.builder().date(date).open(open).high(high).low(low).close(close).volume(volume);
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public LocalDate getDate() {
    return price.getDate();
  }

  public void setDate(final LocalDate date) {
    price.date(date);
  }

  public double getOpen() {
    return price.getOpen();
  }

  public void setOpen(double open) {
    price.open(open);
  }

  public double getHigh() {
    return price.getHigh();
  }

  public void setHigh(double high) {
    price.high(high);
  }

  public double getLow() {
    return price.getLow();
  }

  public void setLow(double low) {
    price.low(low);
  }

  public double getClose() {
    return price.getClose();
  }

  public void setClose(double close) {
    price.close(close);
  }

  public long getVolume() {
    return price.getVolume();
  }

  public void setVolume(long volume) {
    price.volume(volume);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("symbol", symbol)
            .add("D", price.getDate())
            .add("O", price.getOpen())
            .add("H", price.getHigh())
            .add("L", price.getLow())
            .add("C", price.getClose())
            .add("V", price.getVolume())
            .toString();
  }

  public Price getPrice() {
    return price.build();
  }
}
