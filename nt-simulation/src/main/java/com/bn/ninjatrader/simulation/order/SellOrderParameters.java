package com.bn.ninjatrader.simulation.order;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brad on 8/29/16.
 */
public class SellOrderParameters extends OrderParameters {

  public SellOrderParameters(@JsonProperty("marketTime") MarketTime marketTime,
                             @JsonProperty("barsFromNow") int barsFromNow) {
    super(marketTime, barsFromNow);
  }

  /**
   * Builder for SellOrderParameters.
   */
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
