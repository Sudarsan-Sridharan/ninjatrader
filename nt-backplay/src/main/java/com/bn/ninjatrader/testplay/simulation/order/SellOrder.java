package com.bn.ninjatrader.testplay.simulation.order;

import com.bn.ninjatrader.testplay.simulation.type.MarketTime;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class SellOrder extends Order {

  private SellOrder(LocalDate orderDate,
                    MarketTime marketTime,
                    int daysFromNow,
                    long numOfShares) {
    super(orderDate, OrderType.SELL, marketTime, daysFromNow, numOfShares);
  }

  public static class SellOrderBuilder extends OrderBuilder {
    public SellOrder build() {
      SellOrder order = new SellOrder(orderDate, marketTime, daysFromNow, numOfShares);
      return order;
    }
  }
}
