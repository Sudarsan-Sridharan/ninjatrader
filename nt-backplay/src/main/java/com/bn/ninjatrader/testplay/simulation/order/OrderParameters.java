package com.bn.ninjatrader.testplay.simulation.order;

/**
 * Created by Brad on 8/29/16.
 */
public abstract class OrderParameters {

  public static BuyOrderParameters.BuyOrderParametersBuilder buy() {
    return new BuyOrderParameters.BuyOrderParametersBuilder();
  }

  public static SellOrderParameters.SellOrderParametersBuilder sell() {
    return new SellOrderParameters.SellOrderParametersBuilder();
  }

  private MarketTime marketTime = MarketTime.CLOSE;
  private int barsFromNow = 0;

  OrderParameters(MarketTime marketTime, int barsFromNow) {
    this.marketTime = marketTime;
    this.barsFromNow = barsFromNow;
  }

  public MarketTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(MarketTime marketTime) {
    this.marketTime = marketTime;
  }

  public int getBarsFromNow() {
    return barsFromNow;
  }

  public void setBarsFromNow(int barsFromNow) {
    this.barsFromNow = barsFromNow;
  }

  public static abstract class OrderParametersBuilder<T> {
    private MarketTime marketTime = MarketTime.CLOSE;
    private int barsFromNow = 0;

    public T at(MarketTime marketTime) {
      this.marketTime = marketTime;
      return getThis();
    }

    public T barsFromNow(int barsFromNow) {
      this.barsFromNow = barsFromNow;
      return getThis();
    }

    public MarketTime getMarketTime() {
      return marketTime;
    }

    public int getBarsFromNow() {
      return barsFromNow;
    }

    public abstract T getThis();

    public abstract OrderParameters build();
  }
}
