package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.data.BarData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class PendingOrder {
  private static final Logger LOG = LoggerFactory.getLogger(PendingOrder.class);

  public static final PendingOrder of(final Order order, final BarData barData) {
    return new PendingOrder(order, barData);
  }

  private final Order order;
  private final BarData submittedBarData;

  private PendingOrder(final Order order, final BarData submittedBarData) {
    this.order = order;
    this.submittedBarData = submittedBarData;
  }

  public Order getOrder() {
    return order;
  }

  public BarData getBarData() {
    return submittedBarData;
  }

  /**
   * Orders are ready to process if today >= submitted day + barsFromNow
   * @param currentBarData current BarData
   * @return true if Order is ready for processing.
   */
  public boolean isReadyToProcess(final BarData currentBarData) {
    return order.getBarsFromNow() + submittedBarData.getIndex() <= currentBarData.getIndex();
  }
}
