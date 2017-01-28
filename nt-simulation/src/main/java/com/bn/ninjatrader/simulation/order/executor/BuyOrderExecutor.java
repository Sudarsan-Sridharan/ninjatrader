package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.Order;
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

  @Inject
  public BuyOrderExecutor(final BoardLotTable boardLotTable) {
    super(boardLotTable);
  }

  @Override
  public BuyTransaction execute(final Account account, final Order order, final BarData barData) {
    checkConditions(account, order, barData);

    final BuyTransaction buyTransaction = fulfillBuyOrder(order, barData);

    updateAccount(account, buyTransaction);

    return buyTransaction;
  }

  private BuyTransaction fulfillBuyOrder(final Order order, final BarData barData) {
    final BuyOrder buyOrder = (BuyOrder) order;

    final OrderType orderType = order.getOrderType();
    final double boughtPrice = orderType.getFulfilledPrice(barData);
    final long numOfShares = getNumOfSharesCanBuyWithAmount(buyOrder.getCashAmount(), boughtPrice);

    return Transaction.buy()
        .date(barData.getPrice().getDate())
        .price(boughtPrice)
        .shares(numOfShares)
        .barIndex(barData.getIndex())
        .build();
  }

  private void updateAccount(final Account account, final BuyTransaction transaction) {
    account.addToPortfolio(transaction);
    account.addCash(-transaction.getValue());
    account.onBuySuccess(transaction);
  }
}
