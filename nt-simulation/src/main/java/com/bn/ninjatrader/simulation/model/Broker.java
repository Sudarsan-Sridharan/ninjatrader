package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.annotation.OrderExecutors;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker {
  private static final Logger LOG = LoggerFactory.getLogger(Broker.class);

  private final List<PendingOrder> pendingOrders = Lists.newArrayList();
  private final Map<TransactionType, Transaction> lastTransactions = Maps.newHashMap();

  private final Map<TransactionType, OrderExecutor> orderExecutors;
  private final Account account;

  @Inject
  public Broker(@Assisted final Account account,
                @OrderExecutors final Map<TransactionType, OrderExecutor> orderExecutors) {
    checkNotNull(account);
    this.account = account;
    this.orderExecutors = orderExecutors;
  }

  public void submitOrder(final Order order, final BarData barData) {
    checkNotNull(order, "order must not be null.");
    checkNotNull(barData, "barData must not be null.");

    LOG.info("{} - Submit {} order at price [{}].", order.getOrderDate(), order.getTransactionType(),
        order.getOrderType().getFulfilledPrice(barData, barData));

    //TODO
//    if (!pendingOrders.isEmpty()) {
//      for (final PendingOrder pendingOrder : pendingOrders) {
//        LOG.info("  -- Pending: {}", pendingOrder.getOrder().)
//      }
//    }

    pendingOrders.add(PendingOrder.of(order, barData));
  }

  public void processPendingOrders(final BarData barData) {
    checkNotNull(barData, "barData must not be null.");

    if (pendingOrders.isEmpty()) {
      return;
    }

    final List<PendingOrder> fulfilledOrders = Lists.newArrayList();
    for (final PendingOrder pendingOrder : pendingOrders) {
      if (pendingOrder.isReadyToProcess(barData)) {
        fulfillOrder(pendingOrder, barData);
        fulfilledOrders.add(pendingOrder);
      } else if (pendingOrder.isExpired(barData)) {
        LOG.info("Expired Order: submitted on {}. Current date is {}",
            pendingOrder.getSubmittedBarData().getPrice().getDate(),
            barData.getPrice().getDate());
        fulfilledOrders.add(pendingOrder);
      }
    }
    pendingOrders.removeAll(fulfilledOrders);
  }

  private void fulfillOrder(final PendingOrder pendingOrder, final BarData barData) {
    final OrderExecutor orderExecutor = orderExecutors.get(pendingOrder.getOrder().getTransactionType());
    checkNotNull(orderExecutor, "No OrderExecutor found for TransactionType: %s",
        pendingOrder.getOrder().getTransactionType());

    final Order order = pendingOrder.getOrder();
    final TransactionType tnxType = order.getTransactionType();
    final OrderType orderType = order.getOrderType();

    final Transaction transaction = orderExecutor.execute(account, pendingOrder, barData);
    lastTransactions.put(transaction.getTransactionType(), transaction);

    LOG.info("{} - Processed {} order at price [{}].", barData.getPrice().getDate(), tnxType,
        orderType.getFulfilledPrice(pendingOrder.getSubmittedBarData(), barData));
  }

  public boolean hasPendingOrder() {
    return !pendingOrders.isEmpty();
  }

  public List<PendingOrder> getPendingOrders() {
    return Lists.newArrayList(pendingOrders);
  }

  public void removePendingOrder(final PendingOrder pendingOrder) {
    pendingOrders.remove(pendingOrder);
  }

  public void removePendingOrders(final Collection<PendingOrder> pendingOrders) {
    this.pendingOrders.removeAll(pendingOrders);
  }

  public Optional<Transaction> getLastTransaction(final TransactionType transactionType) {
    return Optional.ofNullable(lastTransactions.get(transactionType));
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
