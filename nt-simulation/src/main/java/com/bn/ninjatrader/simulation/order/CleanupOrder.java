package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;

/**
 * Sells everything in user's portfolio based to the last bar's closing price.
 * Since this order is not triggered by an algorithm but is triggered at the end of a simulation,
 * the sell transaction is not added to the simulation's list of buy/sell actions.
 *
 * Created by Brad on 8/12/16.
 */
public class CleanupOrder extends AbstractOrder {

  private static CleanupOrder INSTANCE;

  public static final CleanupOrder instance() {
    if (INSTANCE == null) {
      INSTANCE = new CleanupOrder();
    }
    return INSTANCE;
  }

  private CleanupOrder() {
    super(null, null, TransactionType.CLEANUP, OrderTypes.marketClose(), OrderConfig.defaults(), 0);
  }
}
