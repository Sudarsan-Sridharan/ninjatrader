package com.bn.ninjatrader.simulation.report;

import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.stats.TradeStatistic;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulationReport {

  @JsonProperty("params")
  private SimulationParams simulationParams;

  @JsonProperty("startingCash")
  private double startingCash;

  @JsonProperty("endingCash")
  private double endingCash;

  @JsonProperty("transactions")
  private List<Transaction> transactions;

  @JsonProperty("stats")
  private TradeStatistic tradeStatistic;

  public SimulationParams getSimulationParams() {
    return simulationParams;
  }

  public void setSimulationParams(SimulationParams simulationParams) {
    this.simulationParams = simulationParams;
  }

  public double getStartingCash() {
    return startingCash;
  }

  public void setStartingCash(double startingCash) {
    this.startingCash = startingCash;
  }

  public double getEndingCash() {
    return endingCash;
  }

  public void setEndingCash(double endingCash) {
    this.endingCash = endingCash;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  public TradeStatistic getTradeStatistic() {
    return tradeStatistic;
  }

  public void setTradeStatistic(TradeStatistic tradeStatistic) {
    this.tradeStatistic = tradeStatistic;
  }
}
