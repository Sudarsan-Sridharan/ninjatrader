package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.data.BarData;

/**
 * @author bradwee2000@gmail.com
 */
public class PendingOrder {

  public static final PendingOrder of(final Order order, final BarData barData) {
    return new PendingOrder(order, barData);
  }

  private final Order order;
  private final BarData barData;

  private PendingOrder(final Order order, final BarData barData) {
    this.order = order;
    this.barData = barData;
  }

  public Order getOrder() {
    return order;
  }

  public BarData getBarData() {
    return barData;
  }

  /**
   * Orders are ready to process if today >= submitted day + barsFromNow
   * @param todaysBarData BarData of today.
   * @return true if Order is ready for processing.
   */
  public boolean isReadyToProcess(final BarData todaysBarData) {
    return order.getBarsFromNow() + barData.getIndex() <= todaysBarData.getIndex();
  }
}
