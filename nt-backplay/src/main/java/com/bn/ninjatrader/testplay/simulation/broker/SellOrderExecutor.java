package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.order.BuyOrder;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.base.Preconditions;

/**
 * Created by Brad on 8/13/16.
 */
public class SellOrderExecutor {

  public void execute(Account account, Order order, Price currentPrice) {
    checkConditions(account, order, currentPrice);

    BuyOrder buyOrder = (BuyOrder) order;
    buyOrder.fulfill(currentPrice);
    double buyPrice = order.getFulfilledPrice();
    long numOfShares = (long)(buyOrder.getBuyAmount() / buyPrice / 1000) * 1000;
    double totalValue = NumUtil.multiply(numOfShares, buyPrice);

    account.getPortfolio().add(buyOrder);
    account.addCash(-totalValue);
    account.getTradeLogger().logBuy(order.getOrderDate(), buyPrice, numOfShares);
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
