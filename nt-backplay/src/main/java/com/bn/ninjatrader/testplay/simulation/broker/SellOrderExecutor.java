package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.account.Portfolio;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import com.bn.ninjatrader.testplay.simulation.order.Order;

/**
 * Created by Brad on 8/13/16.
 */
public class SellOrderExecutor extends OrderExecutor {

  public void execute(Account account, Order order, Price currentPrice) {
    checkConditions(account, order, currentPrice);

    Portfolio portfolio = account.getPortfolio();

    order.fulfill(currentPrice);
    double sellPrice = order.getFulfilledPrice();
    double avgBoughtPrice = portfolio.getAvgPrice();
    long totalShares = portfolio.getTotalShares();
    double totalValue = NumUtil.multiply(sellPrice, totalShares);
    double priceDiff = sellPrice - avgBoughtPrice;
    double profit = NumUtil.multiply(priceDiff, totalShares);

    account.addCash(totalValue);
    portfolio.clear();

    SellTransaction sellTransaction = Transaction.sell().price(sellPrice).shares(totalShares).profit(profit).build();

    account.onSellSuccess(sellTransaction);
  }
}
