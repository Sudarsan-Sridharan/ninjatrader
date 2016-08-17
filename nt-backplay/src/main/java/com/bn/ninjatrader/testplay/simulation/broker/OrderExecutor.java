package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.account.Portfolio;
import com.bn.ninjatrader.testplay.simulation.order.BuyOrder;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.base.Preconditions;

/**
 * Created by Brad on 8/13/16.
 */
public class OrderExecutor {

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

    account.getTradeLogger().logSell(order.getOrderDate(), sellPrice, totalShares);
    account.getTradeStatistic().trade();
    if (profit > 0) {
      account.getTradeStatistic().win();
    } else {
      account.getTradeStatistic().lose();
    }
  }

  private void checkConditions(Account account, Order order, Price currentPrice) {
    Preconditions.checkNotNull(account);
    Preconditions.checkNotNull(account.getPortfolio());
    Preconditions.checkNotNull(account.getTradeLogger());
    Preconditions.checkNotNull(order);
    Preconditions.checkNotNull(currentPrice);
    Preconditions.checkArgument(order instanceof BuyOrder);
  }
}
