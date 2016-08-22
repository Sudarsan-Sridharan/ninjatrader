package com.bn.ninjatrader.testplay.simulation.order;

import com.bn.ninjatrader.testplay.simulation.transaction.TransactionType;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class BuyOrder extends Order {

  private double cashAmount;

  private BuyOrder(LocalDate orderDate,
                   MarketTime marketTime,
                   int daysFromNow,
                   long numOfShares,
                   double cashAmount) {
    super(orderDate, TransactionType.BUY, marketTime, daysFromNow, numOfShares);
    this.cashAmount = cashAmount;
  }

  public double getCashAmount() {
    return cashAmount;
  }

  public static class BuyOrderBuilder extends OrderBuilder<BuyOrderBuilder> {
    private double cashAmount;

    public BuyOrderBuilder cashAmount(double cashAmount) {
      this.cashAmount = cashAmount;
      return this;
    }

    @Override
    BuyOrderBuilder getThis() {
      return this;
    }

    @Override
    public BuyOrder build() {
      BuyOrder order = new BuyOrder(getOrderDate(), getMarketTime(), getDaysFromNow(), getNumOfShares(), cashAmount);
      return order;
    }
  }
}
