package com.bn.ninjatrader.simulation.order;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/29/16.
 */
public class BuyOrderParametersTest {

  @Test
  public void testBuild() {
    BuyOrderParameters params = OrderParameters.buy().at(MarketTime.OPEN).barsFromNow(100).build();
    assertEquals(params.getBarsFromNow(), 100);
    assertEquals(params.getMarketTime(), MarketTime.OPEN);
  }
}