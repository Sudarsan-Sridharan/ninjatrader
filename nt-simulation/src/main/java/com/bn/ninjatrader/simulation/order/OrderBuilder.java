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
  private Double transactAtPrice;

  public T date(LocalDate orderDate) {
    this.orderDate = orderDate;
    return getThis();
  }

  public T shares(long numOfShares) {
    this.numOfShares = numOfShares;
    return getThis();
  }

  public T barsFromNow(int daysFromNow) {
    this.daysFromNow = daysFromNow;
    return getThis();
  }

  public T at(OrderType orderType) {
    this.orderType = orderType;
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

  abstract T getThis();

  public abstract Order build();
}
