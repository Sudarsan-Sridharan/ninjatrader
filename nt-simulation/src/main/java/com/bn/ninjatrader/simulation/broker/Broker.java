package com.bn.ninjatrader.simulation.broker;

import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker {
  private static final Logger LOG = LoggerFactory.getLogger(Broker.class);

  private final List<Order> pendingOrders = Lists.newArrayList();
  private final List<BuyTransaction> fulfilledBuys = Lists.newArrayList();
  private final List<SellTransaction> fulfilledSells = Lists.newArrayList();
  private Account account;

  @Inject
  private BuyOrderExecutor buyOrderExecutor;

  @Inject
  private SellOrderExecutor sellOrderExecutor;

  @Inject
  public Broker(@Assisted final Account account) {
    checkNotNull(account);
    this.account = account;
  }

  public void submitOrder(final Order order) {
    checkNotNull(order, "order must not be null.");
    pendingOrders.add(order);
  }

  public void processPendingOrders(final BarData barData) {
    checkNotNull(barData, "barData must not be null.");
    if (pendingOrders.isEmpty()) {
      return;
    }

    final List<Order> fulfilledOrders = Lists.newArrayList();
    for (final Order order : pendingOrders) {
      if (order.isReadyForProcessing()) {
        fulfillOrder(order, barData);
        fulfilledOrders.add(order);
      }
      order.decrementBarsFromNow();
    }
    pendingOrders.removeAll(fulfilledOrders);
  }

  private void fulfillOrder(final Order order, final BarData barData) {
    switch (order.getTransactionType()) {
      case BUY:
        fulfillBuy(order, barData); break;
      case SELL:
        fulfillSell(order, barData); break;
    }
  }

  private void fulfillBuy(final Order order, final BarData barData) {
    final BuyTransaction buyTransaction = buyOrderExecutor.execute(account, order, barData);
    fulfilledBuys.add(buyTransaction);
  }

  private void fulfillSell(final Order order, final BarData barData) {
    final SellTransaction sellTransaction = sellOrderExecutor.execute(account, order, barData);
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
