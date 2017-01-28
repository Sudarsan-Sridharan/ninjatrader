package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.type.OrderType;
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

  public SellTransaction execute(final Account account, final Order order, final BarData barData) {
    checkConditions(account, order, barData);

    final OrderType orderType = order.getOrderType();
    final double soldPrice = orderType.getFulfilledPrice(barData);
    final long numOfShares = account.getNumOfShares();
    final double profit = calculateProfit(account, soldPrice);

    final SellTransaction sellTransaction = Transaction.sell()
        .date(barData.getPrice().getDate())
        .price(soldPrice)
        .shares(numOfShares)
        .profit(profit)
        .barIndex(barData.getIndex())
        .build();

    updateAccount(account, sellTransaction);

    return sellTransaction;
  }

  private void updateAccount(final Account account, final SellTransaction transaction) {
    account.addCash(transaction.getValue());
    account.clearPortfolio();
    account.onSellSuccess(transaction);
  }
}
