package com.bn.ninjatrader.simulation.model.stat;

import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultTradeStatistic.class, name = "default"),
    @JsonSubTypes.Type(value = EmptyTradeStatistic.class, name = "empty")
})
public interface TradeStatistic extends Serializable {

  @JsonProperty("trades")
  int getNumOfTrades();

  @JsonProperty("wins")
  int getNumOfWins();

  @JsonProperty("losses")
  int getNumOfLosses();

  @JsonProperty("totalGain")
  double getTotalGain();

  @JsonProperty("totalLoss")
  double getTotalLoss();

  @JsonProperty("totalProfit")
  double getTotalProfit();

  @JsonProperty("maxGainTxn")
  SellTransaction getMaxGainTxn();

  @JsonProperty("maxLossTxn")
  SellTransaction getMaxLossTxn();

  @JsonProperty("maxPcntGainTxn")
  SellTransaction getMaxPcntGainTxn();

  @JsonProperty("maxPcntLossTxn")
  SellTransaction getMaxPcntLossTxn();

  @JsonProperty("profitPerTrade")
  double getProfitPerTrade();

  @JsonProperty("winPcnt")
  double getWinPcnt();

  @JsonProperty("losePcnt")
  double getLossPcnt();

  void collectStats(final Transaction transaction);

  List<Transaction> getTransactions();
}
