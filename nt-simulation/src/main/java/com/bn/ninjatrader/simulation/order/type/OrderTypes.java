package com.bn.ninjatrader.simulation.order.type;

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

  private OrderTypes() {}
}
