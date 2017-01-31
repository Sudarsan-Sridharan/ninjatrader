package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

  public BarData getSubmittedBarData() {
    return submittedBarData;
  }

  /**
   * Orders are ready to process if today >= submitted day + barsFromNow
   * @param currentBarData current BarData
   * @return true if Order is ready for processing.
   */
  public boolean isReadyToProcess(final BarData currentBarData) {
    final OrderType orderType = order.getOrderType();
    final OrderConfig orderConfig = order.getOrderConfig();

    return orderType.isFulfillable(submittedBarData, currentBarData)
        && orderConfig.getBarsFromNow() + submittedBarData.getIndex() <= currentBarData.getIndex();
  }

  /**
   * Expired if current Index > submitted index + expireAfterNumOfBars.
   * @param currentBarData
   * @return true if order is expired.
   */
  public boolean isExpired(final BarData currentBarData) {
    final OrderConfig orderConfig = order.getOrderConfig();
    return currentBarData.getIndex() > submittedBarData.getIndex() + (long) orderConfig.getExpireAfterNumOfBars();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PendingOrder)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final PendingOrder rhs = (PendingOrder) obj;
    return Objects.equal(order, rhs.order)
        && Objects.equal(submittedBarData, rhs.submittedBarData);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(order, submittedBarData);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("order", order).add("submittedBarData", submittedBarData).toString();
  }
}
