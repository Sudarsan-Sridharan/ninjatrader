package com.bn.ninjatrader.simulation.order;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brad on 8/29/16.
 */
public class BuyOrderParameters extends OrderParameters {

  public BuyOrderParameters(@JsonProperty("marketTime") MarketTime marketTime,
                            @JsonProperty("barsFromNow")int barsFromNow) {
    super(marketTime, barsFromNow);
  }

  /**
   * Builder for BuyOrderParameters.
   */
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
