package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Portfolio;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.PendingOrder;
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

  public SellTransaction execute(final PendingOrder pendingOrder, final BarData currentBarData) {
    checkConditions(pendingOrder, currentBarData);

    final World world = currentBarData.getWorld();
    final Account account = world.getAccount();
    final OrderType orderType = pendingOrder.getOrder().getOrderType();
    final BarData submittedBarData = pendingOrder.getSubmittedBarData();
    final Portfolio portfolio = account.getPortfolio();
    final double soldPrice = orderType.getFulfilledPrice(submittedBarData, currentBarData);
    final long numOfShares = portfolio.getTotalShares();
    final double profit = calculateProfit(account, soldPrice);
    final double profitPcnt = NumUtil.divide(profit, portfolio.getEquityValue());

    final SellTransaction sellTransaction = Transaction.sell()
        .date(currentBarData.getPrice().getDate())
        .price(soldPrice)
        .shares(numOfShares)
        .profit(profit)
        .profitPcnt(profitPcnt)
        .barIndex(currentBarData.getIndex())
        .build();

    return sellTransaction;
  }
}
