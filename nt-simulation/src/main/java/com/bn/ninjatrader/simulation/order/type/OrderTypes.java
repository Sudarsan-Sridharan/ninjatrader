package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;

/**
 * @author bradwee2000@gmail.com
 */
public class OrderTypes {

  public static final MarketClose marketClose() {
    return MarketClose.instance();
  }

  public static final MarketOpen marketOpen() {
    return MarketOpen.instance();
  }

  public static final AtPrice atPrice(final double price) {
    return AtPrice.of(price);
  }

  public static final AtPrice atPrice(final Operation<BarData> price) {
    return AtPrice.of(price);
  }

  private OrderTypes() {}
}
