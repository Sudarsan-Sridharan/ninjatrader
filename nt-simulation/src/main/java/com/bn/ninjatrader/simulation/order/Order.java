package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.TransactionType;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public interface Order {
  String getSymbol();

  LocalDate getOrderDate();

  long getNumOfShares();

  TransactionType getTransactionType();

  OrderType getOrderType();

  OrderConfig getOrderConfig();
}
