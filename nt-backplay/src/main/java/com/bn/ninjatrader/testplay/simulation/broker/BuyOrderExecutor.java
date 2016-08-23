package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.order.BuyOrder;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/13/16.
 */
@Singleton
public class BuyOrderExecutor extends OrderExecutor {

  private static final Logger log = LoggerFactory.getLogger(BuyOrderExecutor.class);

  @Override
  public BuyTransaction execute(Account account, Order order, BarData barData) {
    checkConditions(account, order, barData);

    BuyTransaction buyTransaction = fulfillBuyOrder(order, barData);

    updateAccount(account, buyTransaction);

    return buyTransaction;
  }

  private BuyTransaction fulfillBuyOrder(Order order, BarData barData) {
    BuyOrder buyOrder = (BuyOrder) order;

    double boughtPrice = getFulfilledPrice(order, barData);
    long numOfShares = getNumOfSharesCanBuyWithAmount(buyOrder.getCashAmount(), boughtPrice);

    return Transaction.buy()
        .date(barData.getPrice().getDate())
        .price(boughtPrice)
        .shares(numOfShares)
        .barIndex(barData.getBarIndex())
        .build();
  }

  private void updateAccount(Account account, BuyTransaction transaction) {
    account.addToPortfolio(transaction);
    account.addCash(-transaction.getValue());
    account.onBuySuccess(transaction);
  }
}
