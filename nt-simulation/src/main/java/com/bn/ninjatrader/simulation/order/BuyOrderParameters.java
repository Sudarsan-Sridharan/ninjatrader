package com.bn.ninjatrader.simulation.order;

/**
 * Created by Brad on 8/29/16.
 */
public class BuyOrderParameters extends OrderParameters {

  private BuyOrderParameters(MarketTime marketTime, int barsFromNow) {
    super(marketTime, barsFromNow);
  }

  public static class BuyOrderParametersBuilder extends OrderParametersBuilder<BuyOrderParametersBuilder> {
    @Override
    public BuyOrderParametersBuilder getThis() {
      return this;
    }

    @Override
    public BuyOrderParameters build() {
      return new BuyOrderParameters(getMarketTime(), getBarsFromNow());
    }
  }
}
