package com.bn.ninjatrader.model.datastore.builder;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilder;
import com.bn.ninjatrader.model.datastore.entity.PriceDatastore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceBuilderDatastore implements PriceBuilder {
  public static final PriceBuilderDatastore newInstance() {
    return new PriceBuilderDatastore();
  }

  private LocalDate date;
  private double open;
  private double high;
  private double low;
  private double close;
  private long volume;
  private double change;

  public PriceBuilderDatastore copyOf(final Price price) {
    this.date = price.getDate();
    this.open = price.getOpen();
    this.high = price.getHigh();
    this.low = price.getLow();
    this.close = price.getClose();
    this.change = price.getChange();
    this.volume = price.getVolume();
    return this;
  }

  public PriceBuilderDatastore date(final LocalDate date) {
    this.date = date;
    return this;
  }

  public PriceBuilderDatastore open(final double open) {
    this.open = open;
    return this;
  }

  public PriceBuilderDatastore high(final double high) {
    this.high = high;
    return this;
  }
  public PriceBuilderDatastore low(final double low) {
    this.low = low;
    return this;
  }

  public PriceBuilderDatastore close(final double close) {
    this.close = close;
    return this;
  }

  public PriceBuilderDatastore change(final double change) {
    this.change = change;
    return this;
  }

  public PriceBuilderDatastore volume(final long volume) {
    this.volume = volume;
    return this;
  }

  public PriceBuilderDatastore addVolume(final long volume) {
    this.volume += volume;
    return this;
  }

  public LocalDate getDate() {
    return date;
  }

  public double getOpen() {
    return open;
  }

  public double getHigh() { return high; }

  public double getLow() {
    return low;
  }

  public double getClose() {
    return close;
  }

  public double getChange() {
    return change;
  }

  public long getVolume() {
    return volume;
  }

  public Price build() {
    return new PriceDatastore(date.format(DateTimeFormatter.BASIC_ISO_DATE), open, high, low, close, change, volume);
  }
}
