package com.bn.ninjatrader.simulation.order.cancel;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.SimContext;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelAll implements CancelType {
  private static final CancelAll INSTANCE = new CancelAll();

  public static final CancelAll instance() {
    return INSTANCE;
  }

  @Override
  public List<PendingOrder> findPendingOrdersToCancel(final BarData barData) {
    final SimContext simContext = barData.getSimContext();
    final Broker broker = simContext.getBroker();
    return broker.getPendingOrders();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof CancelAll)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
