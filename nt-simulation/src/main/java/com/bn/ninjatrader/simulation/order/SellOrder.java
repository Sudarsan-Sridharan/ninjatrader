package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.TransactionType;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class SellOrder extends Order {

  private SellOrder(final LocalDate orderDate,
                    final OrderType orderType,
                    final OrderConfig orderConfig,
                    final int daysFromNow,
                    final long numOfShares) {
    super(orderDate, TransactionType.SELL, orderType, orderConfig, daysFromNow, numOfShares);
  }

  public static class SellOrderBuilder extends OrderBuilder<SellOrderBuilder> {

    @Override
    SellOrderBuilder getThis() {
      return this;
    }

    @Override
    public SellOrder build() {
      return new SellOrder(getOrderDate(), getOrderType(), getOrderConfig(), getDaysFromNow(), getNumOfShares());
    }
  }
}
