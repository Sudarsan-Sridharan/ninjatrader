package com.bn.ninjatrader.common.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 4/27/16.
 */
public class DailyQuote {

  private String symbol;
  private Price price;

  public DailyQuote() { price = new Price(); }

  public DailyQuote(String symbol, LocalDate date, double open, double high, double low, double close, long volume) {
    this.symbol = symbol;
    price = new Price(date, open, high, low, close, volume);
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

  public void setDate(LocalDate date) {
    price.setDate(date);
  }

  public double getOpen() {
    return price.getOpen();
  }

  public void setOpen(double open) {
    price.setOpen(open);
  }

  public double getHigh() {
    return price.getHigh();
  }

  public void setHigh(double high) {
    price.setHigh(high);
  }

  public double getLow() {
    return price.getLow();
  }

  public void setLow(double low) {
    price.setLow(low);
  }

  public double getClose() {
    return price.getClose();
  }

  public void setClose(double close) {
    price.setClose(close);
  }

  public long getVolume() {
    return price.getVolume();
  }

  public void setVolume(long volume) {
    price.setVolume(volume);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("symbol", symbol)
            .append("D", price.getDate())
            .append("O", price.getOpen())
            .append("H", price.getHigh())
            .append("L", price.getLow())
            .append("C", price.getClose())
            .toString();
  }

  public Price getPrice() {
    return price;
  }
}
