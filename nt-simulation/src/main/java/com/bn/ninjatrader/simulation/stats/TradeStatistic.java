package com.bn.ninjatrader.simulation.stats;

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

  private static final Logger log = LoggerFactory.getLogger(TradeStatistic.class);

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

  @JsonProperty("biggestGain")
  private double biggestGain = 0;

  @JsonProperty("biggestLoss")
  private double biggestLoss = 0;

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

  public double getBiggestGain() {
    return biggestGain;
  }

  public double getBiggestLoss() {
    return biggestLoss;
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

  public void collectStats(SellTransaction transaction) {
    if (transaction == null) {
      return;
    }
    if (transaction.getProfit() > 0) {
      numOfWins++;
      totalGain = NumUtil.plus(totalGain, transaction.getProfit());
      biggestGain = Math.max(biggestGain, transaction.getProfit());
    } else {
      numOfLosses++;
      totalLosses = NumUtil.plus(totalLosses, transaction.getProfit());
      biggestLoss = Math.min(biggestLoss, transaction.getProfit());
    }
    numOfTrades++;
    totalProfit = NumUtil.plus(totalProfit, transaction.getProfit());
  }

  public void print() {
    log.info("Number withNBarsAgo Trades: {}", numOfTrades);
    log.info("Number withNBarsAgo Wins: {} ({}%)", numOfWins, getWinPcnt());
    log.info("Number withNBarsAgo Losses: {} ({}%)", numOfLosses, getLossPcnt());
    log.info("Win / Loss Ratio: {}", getWinLoseRatio());
    log.info("Biggest Gain: {}", biggestGain);
    log.info("Biggest Loss: {}", biggestLoss);
    log.info("Total Gain: {}", NumUtil.trimPrice(totalGain));
    log.info("Total Loss: {}", NumUtil.trimPrice(totalLosses));
    log.info("Total Profit: {}", NumUtil.trimPrice(totalProfit));
    log.info("Profit per Trade: {}", getProfitPerTrade());
  }
}
