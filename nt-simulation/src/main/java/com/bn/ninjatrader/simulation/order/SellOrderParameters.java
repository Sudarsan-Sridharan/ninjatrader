package com.bn.ninjatrader.simulation.order;

/**
 * Created by Brad on 8/29/16.
 */
public class SellOrderParameters extends OrderParameters {

  private SellOrderParameters(MarketTime marketTime, int barsFromNow) {
    super(marketTime, barsFromNow);
  }

  public static class SellOrderParametersBuilder extends OrderParametersBuilder<SellOrderParametersBuilder> {
    @Override
    public SellOrderParametersBuilder getThis() {
      return this;
    }

    @Override
    public SellOrderParameters build() {
      return new SellOrderParameters(getMarketTime(), getBarsFromNow());
    }
  }
}
