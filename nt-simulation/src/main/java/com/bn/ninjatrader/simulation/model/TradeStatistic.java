package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeStatistic {
  private static final Logger LOG = LoggerFactory.getLogger(TradeStatistic.class);

  @JsonProperty("trades")
  private int numOfTrades = 0;

  @JsonProperty("wins")
  private int numOfWins = 0;

  @JsonProperty("losses")
  private int numOfLosses = 0;

  @JsonProperty("totalProfit")
  private double totalProfit = 0;

  @JsonProperty("totalGain")
  private double totalGain = 0;

  @JsonProperty("totalLosses")
  private double totalLosses = 0;

  @JsonProperty("maxGainTxn")
  private SellTransaction maxGainTxn = SellTransaction.builder().build();

  @JsonProperty("maxPcntGainTxn")
  private SellTransaction maxPcntGainTxn = SellTransaction.builder().build();

  @JsonProperty("maxLossTxn")
  private SellTransaction maxLossTxn = SellTransaction.builder().build();

  @JsonProperty("maxPcntLossTxn")
  private SellTransaction maxPcntLossTxn = SellTransaction.builder().build();

  public int getNumOfTrades() {
    return numOfTrades;
  }

  public int getNumOfWins() {
    return numOfWins;
  }

  public int getNumOfLosses() {
    return numOfLosses;
  }

  public double getTotalGain() {
    return totalGain;
  }

  public double getTotalLosses() {
    return totalLosses;
  }

  @JsonProperty("winLoseRatio")
  public double getWinLoseRatio() {
    return NumUtil.trim((double) numOfWins / numOfLosses, 5);
  }

  @JsonProperty("profitPerTrade")
  public double getProfitPerTrade() {
    double profitPerTrade = totalProfit / numOfTrades;
    return NumUtil.trim(profitPerTrade, 2);
  }

  @JsonProperty("winPcnt")
  public double getWinPcnt() {
    return NumUtil.toPercent((double) numOfWins / numOfTrades);
  }

  @JsonProperty("losePcnt")
  public double getLossPcnt() {
    return NumUtil.toPercent((double) numOfLosses / numOfTrades);
  }

  public void collectStats(final SellTransaction transaction) {
    if (transaction == null) {
      return;
    }
    if (transaction.getProfit() > 0) {
      numOfWins++;
      totalGain = NumUtil.plus(totalGain, transaction.getProfit());
      maxGainTxn = transaction.getProfit() > maxGainTxn.getProfit() ? transaction : maxGainTxn;
      maxPcntGainTxn = transaction.getProfitPcnt() > maxPcntGainTxn.getProfitPcnt() ?
          transaction : maxPcntGainTxn;
    } else {
      numOfLosses++;
      totalLosses = NumUtil.plus(totalLosses, transaction.getProfit());
      maxLossTxn = transaction.getProfit() < maxLossTxn.getProfit() ? transaction : maxLossTxn;
      maxPcntLossTxn = transaction.getProfitPcnt() < maxPcntLossTxn.getProfitPcnt() ?
          transaction : maxPcntLossTxn;
    }
    numOfTrades++;
    totalProfit = NumUtil.plus(totalProfit, transaction.getProfit());
  }

  public void print() {
    LOG.info("# of Trades: {}", numOfTrades);
    LOG.info("# of Wins: {} ({}%)", numOfWins, getWinPcnt());
    LOG.info("# of Losses: {} ({}%)", numOfLosses, getLossPcnt());
    LOG.info("Win / Loss Ratio: {}", getWinLoseRatio());
    LOG.info("Biggest Gain Amt: {} ({}%)", maxGainTxn.getProfit(), NumUtil.toPercent(maxGainTxn.getProfitPcnt()));
    LOG.info("Biggest Loss Amt: {} ({}%)", maxLossTxn.getProfit(), NumUtil.toPercent(maxLossTxn.getProfitPcnt()));
    LOG.info("Biggest % Gain: {} ({}%)", maxPcntGainTxn.getProfit(), NumUtil.toPercent(maxPcntGainTxn.getProfitPcnt()));
    LOG.info("Biggest % Loss: {} ({}%)", maxPcntLossTxn.getProfit(), NumUtil.toPercent(maxPcntLossTxn.getProfitPcnt()));
    LOG.info("Total Gain: {}", NumUtil.trimPrice(totalGain));
    LOG.info("Total Loss: {}", NumUtil.trimPrice(totalLosses));
    LOG.info("Total Profit: {}", NumUtil.trimPrice(totalProfit));
    LOG.info("Profit per Trade: {}", getProfitPerTrade());
  }
}
