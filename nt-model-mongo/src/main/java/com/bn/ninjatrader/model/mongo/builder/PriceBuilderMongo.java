package com.bn.ninjatrader.model.mongo.builder;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilder;
import com.bn.ninjatrader.model.mongo.entity.PriceMongo;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceBuilderMongo implements PriceBuilder {
  public static final PriceBuilderMongo newInstance() {
    return new PriceBuilderMongo();
  }

  private LocalDate date;
  private double open;
  private double high;
  private double low;
  private double close;
  private long volume;
  private double change;

  public PriceBuilderMongo copyOf(final Price price) {
    this.date = price.getDate();
    this.open = price.getOpen();
    this.high = price.getHigh();
    this.low = price.getLow();
    this.close = price.getClose();
    this.change = price.getChange();
    this.volume = price.getVolume();
    return this;
  }

  @Override
  public PriceBuilderMongo date(final LocalDate date) {
    this.date = date;
    return this;
  }

  @Override
  public PriceBuilderMongo open(final double open) {
    this.open = open;
    return this;
  }

  @Override
  public PriceBuilderMongo high(final double high) {
    this.high = high;
    return this;
  }

  @Override
  public PriceBuilderMongo low(final double low) {
    this.low = low;
    return this;
  }

  @Override
  public PriceBuilderMongo close(final double close) {
    this.close = close;
    return this;
  }

  @Override
  public PriceBuilderMongo change(final double change) {
    this.change = change;
    return this;
  }

  @Override
  public PriceBuilderMongo volume(final long volume) {
    this.volume = volume;
    return this;
  }

  @Override
  public PriceBuilderMongo addVolume(final long volume) {
    this.volume += volume;
    return this;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public double getOpen() {
    return open;
  }

  @Override
  public double getHigh() { return high; }

  @Override
  public double getLow() {
    return low;
  }

  @Override
  public double getClose() {
    return close;
  }

  @Override
  public double getChange() {
    return change;
  }

  @Override
  public long getVolume() {
    return volume;
  }

  @Override
  public Price build() {
    return new PriceMongo(date, open, high, low, close, change, volume);
  } 
}
