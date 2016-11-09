package com.bn.ninjatrader.simulation.broker;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by Brad on 8/13/16.
 */
@Singleton
public abstract class OrderExecutor {

  @Inject
  private BoardLotTable boardLotTable;

  public abstract Transaction execute(Account account, Order order, BarData barData);

  protected void checkConditions(Account account, Order order, BarData barData) {
    Preconditions.checkNotNull(account);
    Preconditions.checkNotNull(order);
    Preconditions.checkNotNull(barData);
  }

  public double getFulfilledPrice(Order order, BarData barData) {
    Price currentPrice = barData.getPrice();
    switch (order.getMarketTime()) {
      case OPEN: return currentPrice.getOpen();
      default: return currentPrice.getClose();
    }
  }

  public double calculateProfit(Account account, double sellPrice) {
    double avgBoughtPrice = account.getAvgPrice();
    long totalShares = account.getNumOfShares();
    double priceDiff = sellPrice - avgBoughtPrice;
    double profit = NumUtil.multiply(priceDiff, totalShares);
    return profit;
  }

  public long getNumOfSharesCanBuyWithAmount(double cashAmount, double price) {
    BoardLot boardLot = boardLotTable.getBoardLot(price);
    int lotSize = boardLot.getLotSize();
    long numOfShares = (long)(cashAmount / price / lotSize) * lotSize;
    return numOfShares;
  }
}
