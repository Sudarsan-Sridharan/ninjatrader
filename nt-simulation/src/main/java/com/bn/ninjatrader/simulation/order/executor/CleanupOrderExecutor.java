package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.portfolio.Portfolio;
import com.bn.ninjatrader.simulation.model.SimulationContext;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes sell order at the end of the simulation. SellTransaction generated has transaction type CLEANUP.
 *
 * Created by Brad on 8/13/16.
 */
@Singleton
public class CleanupOrderExecutor extends OrderExecutor {
  private static final Logger LOG = LoggerFactory.getLogger(CleanupOrderExecutor.class);

  private final SellOrderExecutor sellOrderExecutor;

  @Inject
  public CleanupOrderExecutor(final BoardLotTable boardLotTable,
                              final SellOrderExecutor sellOrderExecutor) {
    super(boardLotTable);
    this.sellOrderExecutor = sellOrderExecutor;
  }

  public SellTransaction execute(final PendingOrder pendingOrder, final BarData currentBar) {
    final SimulationContext simulationContext = currentBar.getSimulationContext();
    final Account account = simulationContext.getAccount();
    final Portfolio portfolio = account.getPortfolio();
    final long numOfShares = portfolio.getTotalShares();

    final SellOrder sellOrder = SellOrder.builder()
        .copyFrom(pendingOrder.getOrder())
        .symbol(currentBar.getSymbol())
        .date(currentBar.getPrice().getDate())
        .shares(numOfShares)
        .build();

    if (numOfShares > 0) {
      portfolio.commitShares(currentBar.getSymbol(), numOfShares);
    }

    final PendingOrder newOrder = PendingOrder.of(sellOrder, currentBar);
    final SellTransaction txn = sellOrderExecutor.execute(newOrder, currentBar);

    return SellTransaction.builder().copyFrom(txn).isCleanup(true).build();
  }
}
