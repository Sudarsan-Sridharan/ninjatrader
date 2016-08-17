package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import com.bn.ninjatrader.testplay.simulation.order.BuyOrder;
import com.bn.ninjatrader.testplay.simulation.order.Order;

/**
 * Created by Brad on 8/13/16.
 */
public class BuyOrderExecutor extends OrderExecutor {

  public void execute(Account account, Order order, Price currentPrice) {
    checkConditions(account, order, currentPrice);

    BuyOrder buyOrder = (BuyOrder) order;
    buyOrder.fulfill(currentPrice);
    double buyPrice = order.getFulfilledPrice();
    long numOfShares = (long)(buyOrder.getCashAmount() / buyPrice / 1000) * 1000;
    double totalValue = NumUtil.multiply(numOfShares, buyPrice);

    account.getPortfolio().add(buyOrder);
    account.addCash(-totalValue);

    BuyTransaction buyTransaction = Transaction.buy()
        .date(currentPrice.getDate())
        .price(buyPrice)
        .shares(numOfShares)
        .build();

    account.onBuySuccess(buyTransaction);
  }
}
