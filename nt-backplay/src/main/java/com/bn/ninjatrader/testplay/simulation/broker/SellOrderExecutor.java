package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import com.google.inject.Singleton;

/**
 * Created by Brad on 8/13/16.
 */
@Singleton
public class SellOrderExecutor extends OrderExecutor {

  public void execute(Account account, Order order, Price currentPrice) {
    checkConditions(account, order, currentPrice);

    order.fulfill(currentPrice);

    double sellPrice = order.getFulfilledPrice();
    long numOfShares = account.getNumOfShares();
    double profit = calculateProfit(account, sellPrice);

    SellTransaction sellTransaction = Transaction.sell()
        .date(currentPrice.getDate())
        .price(sellPrice)
        .shares(numOfShares)
        .profit(profit)
        .build();

    updateAccount(account, sellTransaction);
  }

  private double calculateProfit(Account account, double sellPrice) {
    double avgBoughtPrice = account.getAvgPrice();
    long totalShares = account.getNumOfShares();
    double priceDiff = sellPrice - avgBoughtPrice;
    double profit = NumUtil.multiply(priceDiff, totalShares);

    return profit;
  }

  private void updateAccount(Account account, SellTransaction transaction) {
    account.addCash(transaction.getValue());
    account.clearPortfolio();
    account.onSellSuccess(transaction);
  }

}
