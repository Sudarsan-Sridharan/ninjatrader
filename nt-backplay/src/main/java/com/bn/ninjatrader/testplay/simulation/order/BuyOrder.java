package com.bn.ninjatrader.testplay.simulation.order;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class BuyOrder extends Order {

  private double buyAmount;

  private BuyOrder(LocalDate orderDate,
                   MarketTime marketTime,
                   int daysFromNow,
                   long numOfShares,
                   double buyAmount) {
    super(orderDate, OrderType.BUY, marketTime, daysFromNow, numOfShares);
    this.buyAmount = buyAmount;
  }

  public double getBuyAmount() {
    return buyAmount;
  }

  public static class BuyOrderBuilder extends OrderBuilder {
    private double cashAmount;

    public BuyOrderBuilder cashAmount(double cashAmount) {
      this.cashAmount = cashAmount;
      return this;
    }
    public BuyOrder build() {
      BuyOrder order = new BuyOrder(orderDate, marketTime, daysFromNow, numOfShares, cashAmount);
      return order;
    }
  }
}
