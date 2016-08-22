package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker {

  private static final Logger log = LoggerFactory.getLogger(Broker.class);

  private List<Order> pendingOrders = Lists.newArrayList();

  @Inject
  private Account account;

  private final BuyOrderExecutor buyOrderExecutor = new BuyOrderExecutor();
  private final SellOrderExecutor sellOrderExecutor = new SellOrderExecutor();

  public Broker() {

  }

  @Inject
  public Broker(Account account) {
    Preconditions.checkNotNull(account);
    this.account = account;
  }

  public void submitOrder(Order order) {
    Preconditions.checkNotNull(order);
    pendingOrders.add(order);
  }

  public void processPendingOrders(Price price) {
    Preconditions.checkNotNull(price);
    if (pendingOrders.isEmpty()) {
      return;
    }

    List<Order> fulfilledOrders = Lists.newArrayList();
    for (Order order : pendingOrders) {
      if (order.isReadyForProcessing()) {
        fulfillOrder(order, price);
        fulfilledOrders.add(order);
      }
      order.decrementDaysFromNow();
    }
    pendingOrders.removeAll(fulfilledOrders);
  }

  private void fulfillOrder(Order order, Price price) {
    switch (order.getTransactionType()) {
      case BUY:
        buyOrderExecutor.execute(account, order, price); break;
      case SELL:
        sellOrderExecutor.execute(account, order, price); break;
    }
  }
}
