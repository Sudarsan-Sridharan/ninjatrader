package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class SavePriceRequest {

  private String symbol;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;
  private List<Price> prices = Lists.newArrayList();

  public static SavePriceRequest forSymbol(final String symbol) {
    return new SavePriceRequest(symbol);
  }

  public SavePriceRequest() {}

  private SavePriceRequest(String symbol) {
    this.symbol = symbol;
  }

  public SavePriceRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public SavePriceRequest timeframe(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  public SavePriceRequest addPrice(final Price price) {
    this.prices.add(price);
    return this;
  }

  public SavePriceRequest addPrices(final Collection<Price> prices) {
    this.prices.addAll(prices);
    return this;
  }

  public SavePriceRequest addPrices(final Price price, final Price ... more) {
    this.prices.addAll(Lists.asList(price, more));
    return this;
  }

  public String getSymbol() {
    return symbol;
  }

  public List<Price> getPrices() {
    return prices;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SavePriceRequest that = (SavePriceRequest) o;
    return Objects.equal(symbol, that.symbol) &&
        timeFrame == that.timeFrame &&
        Objects.equal(prices, that.prices);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, timeFrame, prices);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("timeFrame", timeFrame)
        .add("prices", prices)
        .toString();
  }
}
