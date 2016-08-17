package com.bn.ninjatrader.testplay.simulation.order;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.SellOrder;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public abstract class Order {

  public enum OrderType {
    BUY, SELL
  }

  public enum MarketTime {
    OPEN, CLOSE
  }

  private LocalDate orderDate;
  private double fulfilledPrice;
  private long numOfShares;
  private OrderType orderType;
  private MarketTime marketTime = MarketTime.CLOSE;
  private int daysFromNow;

  public static BuyOrder.BuyOrderBuilder buy() {
    return new BuyOrder.BuyOrderBuilder();
  }

  public static SellOrder.SellOrderBuilder sell() {
    return new SellOrder.SellOrderBuilder();
  }

  protected Order(LocalDate orderDate, OrderType orderType, MarketTime marketTime, int daysFromNow, long numOfShares) {
    this.orderDate = orderDate;
    this.orderType = orderType;
    this.marketTime = marketTime;
    this.daysFromNow = daysFromNow;
    this.numOfShares = numOfShares;
  }

  private Order() {

  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  public double getFulfilledPrice() {
    return fulfilledPrice;
  }

  public void setFulfilledPrice(double fulfilledPrice) {
    this.fulfilledPrice = fulfilledPrice;
  }

  public long getNumOfShares() {
    return numOfShares;
  }

  public void setNumOfShares(long numOfShares) {
    this.numOfShares = numOfShares;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public void setDaysFromNow(int daysFromNow) {
    this.daysFromNow = daysFromNow;
  }

  public MarketTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(MarketTime marketTime) {
    this.marketTime = marketTime;
  }

  public void decrementDaysFromNow() {
    daysFromNow--;
  }

  public boolean isReadyForProcessing() {
    return daysFromNow == 0;
  }

  public double getValue() {
    return NumUtil.multiply(fulfilledPrice, numOfShares);
  }

  public void fulfill(Price price) {
    switch (marketTime) {
      case OPEN: fulfilledPrice = price.getOpen();
      default: fulfilledPrice = price.getClose();
    }
  }

  public static abstract class OrderBuilder {
    protected long numOfShares;
    protected int daysFromNow = 0;
    protected LocalDate orderDate;
    protected MarketTime marketTime = MarketTime.CLOSE;

    public OrderBuilder date(LocalDate orderDate) {
      this.orderDate = orderDate;
      return this;
    }

    public OrderBuilder shares(long numOfShares) {
      this.numOfShares = numOfShares;
      return this;
    }

    public OrderBuilder daysFromNow(int daysFromNow) {
      this.daysFromNow = daysFromNow;
      return this;
    }

    public OrderBuilder at(MarketTime marketTime) {
      this.marketTime = marketTime;
      return this;
    }

    public abstract Order build();
  }
}
