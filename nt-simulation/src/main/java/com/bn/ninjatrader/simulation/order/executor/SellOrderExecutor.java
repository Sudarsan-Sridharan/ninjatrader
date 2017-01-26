package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by Brad on 8/13/16.
 */
@Singleton
public class SellOrderExecutor extends OrderExecutor {

  @Inject
  public SellOrderExecutor(final BoardLotTable boardLotTable) {
    super(boardLotTable);
  }

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
        .barIndex(barData.getIndex())
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
