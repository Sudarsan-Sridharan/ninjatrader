package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public abstract class Order {
  private static final Logger LOG = LoggerFactory.getLogger(Order.class);

  private final LocalDate orderDate;
  private final long numOfShares;
  private final TransactionType transactionType;
  private final OrderType orderType;
  private final OrderConfig orderConfig;
  private final int barsFromNow; // wait for number of bars before attempting to fulfill order.

  public static BuyOrder.BuyOrderBuilder buy() {
    return new BuyOrder.BuyOrderBuilder();
  }

  public static SellOrder.SellOrderBuilder sell() {
    return new SellOrder.SellOrderBuilder();
  }

  protected Order(final LocalDate orderDate,
                  final TransactionType transactionType,
                  final OrderType orderType,
                  final OrderConfig orderConfig,
                  final int barsFromNow,
                  final long numOfShares) {
    this.orderDate = orderDate;
    this.transactionType = transactionType;
    this.orderType = orderType;
    this.barsFromNow = barsFromNow;
    this.orderConfig = orderConfig;
    this.numOfShares = numOfShares;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public long getNumOfShares() {
    return numOfShares;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public int getBarsFromNow() {
    return barsFromNow;
  }

  public OrderConfig getOrderConfig() {
    return orderConfig;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("orderDate", orderDate)
        .add("numOfShares", numOfShares)
        .add("transactionType", transactionType)
        .add("orderType", orderType)
        .add("orderConfig", orderConfig)
        .toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Order)) { return false; }
    if (obj == this) { return true; }
    final Order rhs = (Order) obj;
    return Objects.equal(orderDate, rhs.orderDate)
        && Objects.equal(transactionType, rhs.transactionType)
        && Objects.equal(numOfShares, rhs.numOfShares)
        && Objects.equal(orderType, rhs.orderType)
        && Objects.equal(orderConfig, rhs.orderConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderDate, transactionType, numOfShares, orderType, orderConfig);
  }
}
