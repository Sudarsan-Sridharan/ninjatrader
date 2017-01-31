package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Portfolio;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.transaction.Transaction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/13/16.
 */
public abstract class OrderExecutor {

  private BoardLotTable boardLotTable;

  public OrderExecutor(final BoardLotTable boardLotTable) {
    this.boardLotTable = boardLotTable;
  }

  public abstract Transaction execute(Account account, PendingOrder order, BarData barData);

  protected void checkConditions(final Account account, final PendingOrder order, final BarData barData) {
    checkNotNull(account, "account must not be null.");
    checkNotNull(order, "order must not be null.");
    checkNotNull(barData, "barData must not be null.");
  }

  public double calculateProfit(final Account account, final double sellPrice) {
    final Portfolio portfolio = account.getPortfolio();
    final long totalShares = portfolio.getTotalShares();
    final double priceDiff = sellPrice - portfolio.getAvgPrice();
    return NumUtil.multiply(priceDiff, totalShares);
  }

  public long getNumOfSharesCanBuyWithAmount(final double cashAmount, final double price) {
    BoardLot boardLot = boardLotTable.getBoardLot(price);
    int lotSize = boardLot.getLotSize();
    long numOfShares = (long)(cashAmount / price / lotSize) * lotSize;
    return numOfShares;
  }
}
