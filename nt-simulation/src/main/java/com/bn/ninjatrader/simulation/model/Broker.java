package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.annotation.OrderExecutors;
import com.bn.ninjatrader.simulation.annotation.OrderRequestProcessors;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.model.portfolio.Portfolio;
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
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(Broker.class);
  private static final String SUBMIT_ORDER_LOG = "%s - Submit %s order at price [%s]";
  private static final String EXPIRED_ORDER_LOG = "%s - Expired %s order submitted on %s";
  private static final String PROCESSED_ORDER_LOG = "%s - Processed %s order at price [%s]";

  private final List<PendingOrder> pendingOrders = Lists.newArrayList();
  private final Map<TransactionType, Transaction> lastTransactions = Maps.newHashMap();
  private final List<BrokerListener> listeners = Lists.newArrayList();
  private final Map<TransactionType, OrderExecutor> orderExecutors;
  private final Map<TransactionType, OrderRequestProcessor> orderRequestProcessors;
  private final List<String> logs;
  private final boolean isDebug;
  private final BoardLotTable boardLotTable;

  private BarData currentBar;

  private Broker() {
    this.orderExecutors = null;
    this.orderRequestProcessors = null;
    this.logs = null;
    this.isDebug = false;
    this.boardLotTable = null;
  }

  @Inject
  public Broker(@OrderExecutors final Map<TransactionType, OrderExecutor> orderExecutors,
                @OrderRequestProcessors final Map<TransactionType, OrderRequestProcessor> orderRequestProcessors,
                final BoardLotTable boardLotTable,
                @Assisted final SimulationRequest request) {
    this.orderExecutors = orderExecutors;
    this.orderRequestProcessors = orderRequestProcessors;
    this.isDebug = request.isDebug();
    this.logs = isDebug ? Lists.newArrayList() : Collections.emptyList();
    this.boardLotTable = boardLotTable;
  }

  public void submitOrder(final Order order, final BarData barData) {
    checkNotNull(order, "order must not be null.");
    checkNotNull(barData, "barData must not be null.");

    if (isDebug) {
      logs.add(String.format(SUBMIT_ORDER_LOG,
          barData.getPrice().getDate(),
          order.getTransactionType(),
          order.getOrderType().getFulfilledPrice(barData)));
    }

    pendingOrders.add(PendingOrder.of(order, barData));
  }

  public void submitOrder(final OrderRequest req) {
    checkArgument(orderRequestProcessors.containsKey(req.getTxnType()),
        "No order processor found for TransactionType [%s]", req.getTxnType());

    final Order order = orderRequestProcessors.get(req.getTxnType()).process(req, currentBar);

    submitOrder(order, currentBar);
  }

  public void processPendingOrders(final BarData barData) {
    if (pendingOrders.isEmpty()) {
      return;
    }

    final List<PendingOrder> processedOrders = Lists.newArrayList();
    for (final PendingOrder pendingOrder : pendingOrders) {
      switch (pendingOrder.getStatus(barData)) {
        case READY:
          fulfillOrder(pendingOrder, barData);
          processedOrders.add(pendingOrder);
          break;
        case EXPIRED:
          handleExpiredOrder(pendingOrder, barData);
          processedOrders.add(pendingOrder);
          break;
      }
    }
    pendingOrders.removeAll(processedOrders);
  }

  private void handleExpiredOrder(final PendingOrder pendingOrder, final BarData bar) {
    final SimulationContext simulationContext = bar.getSimulationContext();
    final Account account = simulationContext.getAccount();
    final Portfolio portfolio = account.getPortfolio();

    if (isDebug) {
      logs.add(String.format(EXPIRED_ORDER_LOG,
          bar.getPrice().getDate(),
          pendingOrder.getTransactionType(),
          pendingOrder.getSubmittedBarData().getPrice().getDate()));
    }

    if (pendingOrder.getTransactionType() == TransactionType.SELL) {
      final long committedShares = account.getPortfolio().getCommittedShares(pendingOrder.getSymbol());
      portfolio.cancelCommittedShares(pendingOrder.getSymbol(), committedShares);
    }
  }

  private void fulfillOrder(final PendingOrder pendingOrder, final BarData barData) {
    final OrderExecutor orderExecutor = orderExecutors.get(pendingOrder.getTransactionType());
    checkNotNull(orderExecutor, "No OrderExecutor found for TransactionType: %s",
        pendingOrder.getTransactionType());

    final TransactionType txnType = pendingOrder.getTransactionType();
    final OrderType orderType = pendingOrder.getOrderType();

    final Transaction transaction = orderExecutor.execute(pendingOrder, barData);
    lastTransactions.put(transaction.getTransactionType(), transaction);

    publishToListeners(transaction, barData);

    if (isDebug) {
      logs.add(String.format(PROCESSED_ORDER_LOG,
          barData.getPrice().getDate(),
          txnType,
          orderType.getFulfilledPrice(barData)));
    }
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
        case BUY: listener.onFulfilledBuy((BuyTransaction) transaction, barData); break;
        case SELL: listener.onFulfilledSell((SellTransaction) transaction, barData); break;
        case CLEANUP: listener.onCleanup((SellTransaction) transaction, barData); break;
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
    return new BuyOrderRequest(boardLotTable, this);
  }

  public SellOrderRequest sellOrder() {
    return new SellOrderRequest(boardLotTable, this);
  }

  public void setCurrentBar(final BarData currentBar) {
    this.currentBar = currentBar;
  }

  public List<String> getLogs() {
    return logs;
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
