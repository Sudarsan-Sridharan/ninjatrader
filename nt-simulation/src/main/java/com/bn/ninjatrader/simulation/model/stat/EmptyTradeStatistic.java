package com.bn.ninjatrader.simulation.model.stat;

import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmptyTradeStatistic implements TradeStatistic {

  private static EmptyTradeStatistic INSTANCE;

  public static final EmptyTradeStatistic instance() {
    if (INSTANCE == null) {
      INSTANCE = new EmptyTradeStatistic();
    }
    return INSTANCE;
  }

  @Override
  public int getNumOfTrades() {
    return 0;
  }

  @Override
  public int getNumOfWins() {
    return 0;
  }

  @Override
  public int getNumOfLosses() {
    return 0;
  }

  @Override
  public double getTotalGain() {
    return 0;
  }

  @Override
  public double getTotalLoss() {
    return 0;
  }

  @Override
  public double getTotalProfit() {
    return 0;
  }

  @Override
  public SellTransaction getMaxGainTxn() {
    return null;
  }

  @Override
  public SellTransaction getMaxLossTxn() {
    return null;
  }

  @Override
  public SellTransaction getMaxPcntGainTxn() {
    return null;
  }

  @Override
  public SellTransaction getMaxPcntLossTxn() {
    return null;
  }

  @Override
  public double getProfitPerTrade() {
    return 0;
  }

  @Override
  public double getWinPcnt() {
    return 0;
  }

  @Override
  public double getLossPcnt() {
    return 0;
  }

  @Override
  public void collectStats(Transaction transaction) {
  }

  @Override
  public List<Transaction> getTransactions() {
    return Collections.emptyList();
  }
}
