package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker {

  private static final Logger log = LoggerFactory.getLogger(Broker.class);

  private List<Order> pendingOrders = Lists.newArrayList();
  private List<BuyTransaction> fulfilledBuys = Lists.newArrayList();
  private List<SellTransaction> fulfilledSells = Lists.newArrayList();
  private Account account;

  @Inject
  private BuyOrderExecutor buyOrderExecutor;

  @Inject
  private SellOrderExecutor sellOrderExecutor;

  @Inject
  public Broker(@Assisted Account account) {
    Preconditions.checkNotNull(account);
    this.account = account;
  }

  public void submitOrder(Order order) {
    Preconditions.checkNotNull(order);
    pendingOrders.add(order);
  }

  public void processPendingOrders(BarData barData) {
    Preconditions.checkNotNull(barData);
    if (pendingOrders.isEmpty()) {
      return;
    }

    List<Order> fulfilledOrders = Lists.newArrayList();
    for (Order order : pendingOrders) {
      if (order.isReadyForProcessing()) {
        fulfillOrder(order, barData);
        fulfilledOrders.add(order);
      }
      order.decrementDaysFromNow();
    }
    pendingOrders.removeAll(fulfilledOrders);
  }

  private void fulfillOrder(Order order, BarData barData) {
    switch (order.getTransactionType()) {
      case BUY:
        fulfillBuy(order, barData); break;
      case SELL:
        fulfillSell(order, barData); break;
    }
  }

  private void fulfillBuy(Order order, BarData barData) {
    BuyTransaction buyTransaction = buyOrderExecutor.execute(account, order, barData);
    fulfilledBuys.add(buyTransaction);
  }

  private void fulfillSell(Order order, BarData barData) {
    SellTransaction sellTransaction = sellOrderExecutor.execute(account, order, barData);
    fulfilledSells.add(sellTransaction);
  }

  public boolean hasPendingOrder() {
    return !pendingOrders.isEmpty();
  }

  public Optional<BuyTransaction> getLastFulfilledBuy() {
    return Optional.ofNullable(Iterables.getLast(fulfilledBuys, null));
  }

  public Optional<SellTransaction> getLastFulfilledSell() {
    return Optional.ofNullable(Iterables.getLast(fulfilledSells, null));
  }
}
