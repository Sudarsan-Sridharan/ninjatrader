package com.bn.ninjatrader.testplay.simulation.order;

import com.bn.ninjatrader.testplay.simulation.transaction.TransactionType;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class SellOrder extends Order {

  private SellOrder(LocalDate orderDate,
                    MarketTime marketTime,
                    int daysFromNow,
                    long numOfShares) {
    super(orderDate, TransactionType.SELL, marketTime, daysFromNow, numOfShares);
  }

  public static class SellOrderBuilder extends OrderBuilder<SellOrderBuilder> {

    public SellOrderBuilder params(OrderParameters orderParams) {
      return getThis()
          .at(orderParams.getMarketTime())
          .barsFromNow(orderParams.getBarsFromNow());
    }

    @Override
    SellOrderBuilder getThis() {
      return this;
    }

    @Override
    public SellOrder build() {
      SellOrder order = new SellOrder(getOrderDate(), getMarketTime(), getDaysFromNow(), getNumOfShares());
      return order;
    }
  }
}
