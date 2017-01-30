package com.bn.ninjatrader.simulation.exception;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.type.OrderType;

/**
 * @author bradwee2000@gmail.com
 */
public class OrderUnfulfillableException extends RuntimeException {

  private static final String MSG = "Order with type [%s] is not fulfillable by bar [%s].";

  private final OrderType orderType;
  private final BarData barData;

  public OrderUnfulfillableException(final OrderType orderType, final BarData barData) {
    super(String.format(MSG, orderType, barData));
    this.orderType = orderType;
    this.barData = barData;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public BarData getBarData() {
    return barData;
  }
}
