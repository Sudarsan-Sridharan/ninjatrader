package com.bn.ninjatrader.simulation.broker;

import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Singleton;

/**
 * Created by Brad on 8/13/16.
 */
@Singleton
public class SellOrderExecutor extends OrderExecutor {

  public SellTransaction execute(Account account, Order order, BarData barData) {
    checkConditions(account, order, barData);

    double soldPrice = getFulfilledPrice(order, barData);
    long numOfShares = account.getNumOfShares();
    double profit = calculateProfit(account, soldPrice);

    SellTransaction sellTransaction = Transaction.sell()
        .date(barData.getPrice().getDate())
        .price(soldPrice)
        .shares(numOfShares)
        .profit(profit)
        .barIndex(barData.getBarIndex())
        .build();

    updateAccount(account, sellTransaction);

    return sellTransaction;
  }

  private void updateAccount(Account account, SellTransaction transaction) {
    account.addCash(transaction.getValue());
    account.clearPortfolio();
    account.onSellSuccess(transaction);
  }
}