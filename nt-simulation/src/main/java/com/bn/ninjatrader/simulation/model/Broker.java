package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.annotation.OrderExecutors;
import com.bn.ninjatrader.simulation.annotation.OrderRequestProcessors;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.processor.OrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.request.BuyOrderRequest;
import com.bn.ninjatrader.simulation.order.request.OrderRequest;
import com.bn.ninjatrader.simulation.order.request.SellOrderRequest;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker {
  private static final Logger LOG = LoggerFactory.getLogger(Broker.class);

  private final List<PendingOrder> pendingOrders = Lists.newArrayList();
  private final Map<TransactionType, Transaction> lastTransactions = Maps.newHashMap();
  private final List<BrokerListener> listeners = Lists.newArrayList();
  private final Map<TransactionType, OrderExecutor> orderExecutors;
  private final Map<TransactionType, OrderRequestProcessor> orderRequestProcessors;

  private BarData currentBar;

  @Inject
  public Broker(@OrderExecutors final Map<TransactionType, OrderExecutor> orderExecutors,
                @OrderRequestProcessors final Map<TransactionType, OrderRequestProcessor> orderRequestProcessors) {
    this.orderExecutors = orderExecutors;
    this.orderRequestProcessors = orderRequestProcessors;
  }

  public void submitOrder(final Order order, final BarData barData) {
    checkNotNull(order, "order must not be null.");
    checkNotNull(barData, "barData must not be null.");

//    LOG.info("{} - Submit {} order at price [{}]", order.getOrderDate(), order.getTransactionType(),
//        order.getOrderType().getFulfilledPrice(barData, barData));

    pendingOrders.add(PendingOrder.of(order, barData));
  }

  public void submitOrder(final OrderRequest req) {
    checkArgument(orderRequestProcessors.containsKey(req.getTxnType()),
        "No processor found for TransactionType [%s]", req.getTxnType());
    final OrderRequestProcessor processor = orderRequestProcessors.get(req.getTxnType());
    final Order order = processor.process(req, currentBar);
    pendingOrders.add(PendingOrder.of(order, currentBar));
  }

  public void processPendingOrders(final BarData barData) {

    if (pendingOrders.isEmpty()) {
      return;
    }

    final List<PendingOrder> fulfilledOrders = Lists.newArrayList();
    for (final PendingOrder pendingOrder : pendingOrders) {
      if (pendingOrder.isReadyToProcess(barData)) {
        fulfillOrder(pendingOrder, barData);
        fulfilledOrders.add(pendingOrder);
      } else if (pendingOrder.isExpired(barData)) {
//        LOG.info("{} - Expired {} order submitted on {}",
//            barData.getPrice().getDate(),
//            pendingOrder.getTransactionType(),
//            pendingOrder.getSubmittedBarData().getPrice().getDate());
        fulfilledOrders.add(pendingOrder);
      }
    }
    pendingOrders.removeAll(fulfilledOrders);
  }

  private void fulfillOrder(final PendingOrder pendingOrder, final BarData barData) {
    final OrderExecutor orderExecutor = orderExecutors.get(pendingOrder.getTransactionType());
    checkNotNull(orderExecutor, "No OrderExecutor found for TransactionType: %s",
        pendingOrder.getTransactionType());

    final TransactionType tnxType = pendingOrder.getTransactionType();
    final OrderType orderType = pendingOrder.getOrderType();

    final Transaction transaction = orderExecutor.execute(pendingOrder, barData);
    lastTransactions.put(transaction.getTransactionType(), transaction);

    publishToListeners(transaction, barData);

//    LOG.info("{} - Processed {} order at price [{}]", barData.getPrice().getDate(), tnxType,
//        orderType.getFulfilledPrice(pendingOrder.getSubmittedBarData(), barData));
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

  private void publishToListeners(final Transaction transaction, final BarData barData) {
    for (final BrokerListener listener : listeners) {
      switch(transaction.getTransactionType()) {
        case SELL: listener.onFulfilledSell((SellTransaction) transaction, barData); break;
        case BUY: listener.onFulfilledBuy((BuyTransaction) transaction, barData); break;
      }
    }
  }

  public void addListener(final BrokerListener listener) {
    this.listeners.add(listener);
  }

  public void addListeners(final BrokerListener listener1,
                           final BrokerListener listener2,
                           final BrokerListener ... more) {
    this.listeners.addAll(Lists.asList(listener1, listener2, more));
  }

  public BuyOrderRequest buyOrder() {
    return new BuyOrderRequest(this);
  }

  public SellOrderRequest sellOrder() {
    return new SellOrderRequest(this);
  }

  public void setCurrentBar(final BarData currentBar) {
    this.currentBar = currentBar;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("pendingOrders", pendingOrders)
        .add("lastTransactions", lastTransactions)
        .add("listeners", listeners)
        .add("orderExecutors", orderExecutors)
        .toString();
  }
}
