package com.bn.ninjatrader.testplay.simulation.order;

import com.bn.ninjatrader.testplay.simulation.transaction.TransactionType;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public abstract class Order {

  private LocalDate orderDate;
  private long numOfShares;
  private TransactionType transactionType;
  private MarketTime marketTime = MarketTime.CLOSE;
  private int daysFromNow;

  public static BuyOrder.BuyOrderBuilder buy() {
    return new BuyOrder.BuyOrderBuilder();
  }

  public static SellOrder.SellOrderBuilder sell() {
    return new SellOrder.SellOrderBuilder();
  }

  protected Order(LocalDate orderDate,
                  TransactionType transactionType,
                  MarketTime marketTime,
                  int daysFromNow,
                  long numOfShares) {
    this.orderDate = orderDate;
    this.transactionType = transactionType;
    this.marketTime = marketTime;
    this.daysFromNow = daysFromNow;
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

  public MarketTime getMarketTime() {
    return marketTime;
  }

  public void decrementDaysFromNow() {
    daysFromNow--;
  }

  public boolean isReadyForProcessing() {
    return daysFromNow <= 0;
  }
}
