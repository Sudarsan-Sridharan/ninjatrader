package com.bn.ninjatrader.testplay.simulation.order;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/29/16.
 */
public class SellOrderParametersTest {

  @Test
  public void testBuild() {
    SellOrderParameters params = OrderParameters.sell().at(MarketTime.OPEN).barsFromNow(100).build();
    assertEquals(params.getBarsFromNow(), 100);
    assertEquals(params.getMarketTime(), MarketTime.OPEN);
  }
}
