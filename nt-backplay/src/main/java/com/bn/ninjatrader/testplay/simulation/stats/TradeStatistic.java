package com.bn.ninjatrader.testplay.simulation.stats;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
public class TradeStatistic {

  private static final Logger log = LoggerFactory.getLogger(TradeStatistic.class);

  private int numOfTrades = 0;
  private int numOfWins = 0;
  private int numOfLosses = 0;
  private double totalProfit = 0;

  public int getNumOfTrades() {
    return numOfTrades;
  }

  public int getNumOfWins() {
    return numOfWins;
  }

  public int getNumOfLosses() {
    return numOfLosses;
  }

  public double getWinLoseRatio() {
    return NumUtil.trim((double) numOfWins / numOfLosses, 5);
  }

  public double getProfitPerTrade() {
    double profitPerTrade = totalProfit / numOfTrades;
    return NumUtil.trim(profitPerTrade, 2);
  }

  public double getWinPcnt() {
    return NumUtil.toPercent((double) numOfWins / numOfTrades);
  }

  public double getLossPcnt() {
    return NumUtil.toPercent((double) numOfLosses / numOfTrades);
  }

  public void collectStats(SellTransaction transaction) {
    if (transaction == null) {
      return;
    }
    if (transaction.getProfit() > 0) {
      numOfWins++;
    } else {
      numOfLosses++;
    }
    numOfTrades++;
    totalProfit = NumUtil.plus(totalProfit, transaction.getProfit());
  }

  public void print() {
    log.info("Number of Trades: {}", numOfTrades);
    log.info("Number of Wins: {} ({}%)", numOfWins, getWinPcnt());
    log.info("Number of Losses: {} ({}%)", numOfLosses, getLossPcnt());
    log.info("Win / Loss Ratio: {}", getWinLoseRatio());
    log.info("Total Profit: {}", NumUtil.trimPrice(totalProfit));
    log.info("Profit per Trade: {}", getProfitPerTrade());
  }
}
