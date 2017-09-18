package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/13/16.
 */
@Singleton
public class BuyOrderExecutor extends OrderExecutor {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrderExecutor.class);

  private BuyOrderExecutor() {}

  @Inject
  public BuyOrderExecutor(final BoardLotTable boardLotTable) {
    super(boardLotTable);
  }

  @Override
  public BuyTransaction execute(final PendingOrder order, final BarData barData) {
    checkConditions(order, barData);

    return fulfillBuyOrder(order, barData);
  }

  private BuyTransaction fulfillBuyOrder(final PendingOrder pendingOrder, final BarData barData) {
    final BuyOrder buyOrder = (BuyOrder) pendingOrder.getOrder();
    final OrderType orderType = buyOrder.getOrderType();
    final double boughtPrice = orderType.getFulfilledPrice(barData);
    final long numOfShares = getNumOfSharesCanBuyWithAmount(buyOrder.getCashAmount(), boughtPrice);

    final BuyTransaction txn = Transaction.buy()
        .symbol(buyOrder.getSymbol())
        .date(barData.getPrice().getDate())
        .price(boughtPrice)
        .shares(numOfShares)
        .barIndex(barData.getIndex())
        .build();

    return txn;
  }
}
