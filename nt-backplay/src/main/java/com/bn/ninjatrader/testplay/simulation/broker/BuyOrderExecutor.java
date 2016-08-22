package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.account.Account;
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

  private BoardLotTable boardLotTable = new BoardLotTable();

  public void execute(Account account, Order order, Price currentPrice) {
    checkConditions(account, order, currentPrice);

    BuyOrder buyOrder = (BuyOrder) order;
    buyOrder.fulfill(currentPrice);
    account.removePendingOrder(buyOrder);

    double buyPrice = order.getFulfilledPrice();
    long numOfShares = getNumOfSharesCanBuyWithAmount(buyOrder.getCashAmount(), buyPrice);

    BuyTransaction buyTransaction = Transaction.buy()
        .date(currentPrice.getDate())
        .price(buyPrice)
        .shares(numOfShares)
        .build();

    updateAccount(account, buyTransaction);
  }

  private long getNumOfSharesCanBuyWithAmount(double cashAmount, double price) {
    BoardLot boardLot = boardLotTable.getBoardLot(price);
    int lotSize = boardLot.getLotSize();
    long numOfShares = (long)(cashAmount / price / lotSize) * lotSize;
    return numOfShares;
  }

  private void updateAccount(Account account, BuyTransaction transaction) {
    account.addToPortfolio(transaction);
    account.addCash(-transaction.getValue());
    account.onBuySuccess(transaction);
  }
}
