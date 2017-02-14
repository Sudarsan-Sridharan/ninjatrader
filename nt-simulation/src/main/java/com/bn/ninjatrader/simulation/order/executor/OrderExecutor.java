package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.data.BarData;
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

  public abstract Transaction execute(final PendingOrder order, final BarData barData);

  protected void checkConditions(final PendingOrder order, final BarData barData) {
    checkNotNull(order, "order must not be null.");
    checkNotNull(barData, "barData must not be null.");
  }

  public long getNumOfSharesCanBuyWithAmount(final double cashAmount, final double price) {
    BoardLot boardLot = boardLotTable.getBoardLot(price);
    int lotSize = boardLot.getLotSize();
    long numOfShares = (long)(cashAmount / price / lotSize) * lotSize;
    return numOfShares;
  }
}
