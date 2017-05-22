package com.bn.ninjatrader.simulation.printer;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimulationReportPrinter {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationReportPrinter.class);

  public void printReport(final SimulationReport report) {
    for (final Transaction txn : report.getTransactions()) {
      switch (txn.getTransactionType()) {
        case BUY:
          LOG.info("{} - Buy {} {} shares at price {}",
              txn.getDate(), txn.getSymbol(), txn.getNumOfShares(), txn.getPrice()); break;
        case SELL:
          SellTransaction sellTxn = (SellTransaction) txn;
          LOG.info("{} - Sell {} {} shares at price {} with profit {} ({}%)",
              sellTxn.getDate(), sellTxn.getSymbol(), sellTxn.getNumOfShares(), sellTxn.getPrice(), sellTxn.getProfit(),
              NumUtil.toPercent(sellTxn.getProfitPcnt()));
      }
    }

    final TradeStatistic stats = report.getTradeStatistic();
    LOG.info("# of Trades: {}", stats.getNumOfTrades());
    LOG.info("# of Wins: {} ({}%)", stats.getNumOfWins(), stats.getWinPcnt());
    LOG.info("# of Losses: {} ({}%)", stats.getNumOfLosses(), stats.getLossPcnt());
    LOG.info("Win / Loss Ratio: {}", stats.getWinLoseRatio());
    LOG.info("Biggest Gain Amt: {} ({}%)", stats.getMaxGainTxn().getProfit(), NumUtil.toPercent(stats.getMaxGainTxn().getProfitPcnt()));
    LOG.info("Biggest Loss Amt: {} ({}%)", stats.getMaxLossTxn().getProfit(), NumUtil.toPercent(stats.getMaxLossTxn().getProfitPcnt()));
    LOG.info("Biggest % Gain: {} ({}%)", stats.getMaxPcntGainTxn().getProfit(), NumUtil.toPercent(stats.getMaxPcntGainTxn().getProfitPcnt()));
    LOG.info("Biggest % Loss: {} ({}%)", stats.getMaxPcntLossTxn().getProfit(), NumUtil.toPercent(stats.getMaxPcntLossTxn().getProfitPcnt()));
    LOG.info("Total Gain: {}", NumUtil.trimPrice(stats.getTotalGain()));
    LOG.info("Total Loss: {}", NumUtil.trimPrice(stats.getTotalLoss()));
    LOG.info("Total Profit: {}", NumUtil.trimPrice(stats.getTotalProfit()));
    LOG.info("Profit per Trade: {}", stats.getProfitPerTrade());
    LOG.info("Starting Cash: {}", report.getStartingCash());
    LOG.info("Ending Cash: {}", (long) report.getEndingCash());
    LOG.info("% Gain: {}%", NumUtil.toPercent((report.getEndingCash() - report.getStartingCash()) / report.getStartingCash()));
  }
}
