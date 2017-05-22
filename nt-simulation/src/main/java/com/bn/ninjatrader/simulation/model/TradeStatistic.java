package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

  @JsonProperty("totalLoss")
  private double totalLoss = 0;

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

  public double getTotalLoss() {
    return totalLoss;
  }

  public double getTotalProfit() {
    return totalProfit;
  }

  public SellTransaction getMaxGainTxn() {
    return maxGainTxn;
  }

  public SellTransaction getMaxLossTxn() {
    return maxLossTxn;
  }

  public SellTransaction getMaxPcntGainTxn() {
    return maxPcntGainTxn;
  }

  public SellTransaction getMaxPcntLossTxn() {
    return maxPcntLossTxn;
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
      totalLoss = NumUtil.plus(totalLoss, transaction.getProfit());
      maxLossTxn = transaction.getProfit() < maxLossTxn.getProfit() ? transaction : maxLossTxn;
      maxPcntLossTxn = transaction.getProfitPcnt() < maxPcntLossTxn.getProfitPcnt() ?
          transaction : maxPcntLossTxn;
    }
    numOfTrades++;
    totalProfit = NumUtil.plus(totalProfit, transaction.getProfit());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TradeStatistic that = (TradeStatistic) o;
    return numOfTrades == that.numOfTrades &&
        numOfWins == that.numOfWins &&
        numOfLosses == that.numOfLosses &&
        Double.compare(that.totalProfit, totalProfit) == 0 &&
        Double.compare(that.totalGain, totalGain) == 0 &&
        Double.compare(that.totalLoss, totalLoss) == 0 &&
        Objects.equal(maxGainTxn, that.maxGainTxn) &&
        Objects.equal(maxPcntGainTxn, that.maxPcntGainTxn) &&
        Objects.equal(maxLossTxn, that.maxLossTxn) &&
        Objects.equal(maxPcntLossTxn, that.maxPcntLossTxn);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(numOfTrades, numOfWins, numOfLosses, totalProfit, totalGain, totalLoss, maxGainTxn, maxPcntGainTxn, maxLossTxn, maxPcntLossTxn);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("numOfTrades", numOfTrades)
        .add("numOfWins", numOfWins)
        .add("numOfLosses", numOfLosses)
        .add("totalProfit", totalProfit)
        .add("totalGain", totalGain)
        .add("totalLoss", totalLoss)
        .add("maxGainTxn", maxGainTxn)
        .add("maxPcntGainTxn", maxPcntGainTxn)
        .add("maxLossTxn", maxLossTxn)
        .add("maxPcntLossTxn", maxPcntLossTxn)
        .toString();
  }
}
