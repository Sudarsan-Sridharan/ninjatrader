package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;

import java.time.LocalDate;

/**
 * Created by Brad on 8/13/16.
 */
abstract class OrderBuilder<T extends OrderBuilder> {
  private long numOfShares;
  private int daysFromNow = 0;
  private LocalDate orderDate;
  private OrderType orderType = OrderTypes.marketClose();
  private OrderConfig orderConfig = OrderConfig.defaults();

  public T date(final LocalDate orderDate) {
    this.orderDate = orderDate;
    return getThis();
  }

  public T shares(final long numOfShares) {
    this.numOfShares = numOfShares;
    return getThis();
  }

  public T barsFromNow(final int daysFromNow) {
    this.daysFromNow = daysFromNow;
    return getThis();
  }

  public T type(final OrderType orderType) {
    this.orderType = orderType;
    return getThis();
  }

  public T config(final OrderConfig orderConfig) {
    this.orderConfig = orderConfig;
    return getThis();
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public long getNumOfShares() {
    return numOfShares;
  }

  public int getDaysFromNow() {
    return daysFromNow;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public OrderConfig getOrderConfig() {
    return orderConfig;
  }

  abstract T getThis();

  public abstract Order build();
}
