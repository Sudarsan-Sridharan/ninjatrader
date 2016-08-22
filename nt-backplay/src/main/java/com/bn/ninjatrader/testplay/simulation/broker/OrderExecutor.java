package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.base.Preconditions;

/**
 * Created by Brad on 8/13/16.
 */
public abstract class OrderExecutor {

  public abstract void execute(Account account, Order order, Price currentPrice);

  protected void checkConditions(Account account, Order order, Price currentPrice) {
    Preconditions.checkNotNull(account);
    Preconditions.checkNotNull(order);
    Preconditions.checkNotNull(currentPrice);
  }
}
