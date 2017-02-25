package com.bn.ninjatrader.simulation.report;

import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.model.Mark;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulationReport {
  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("params")
  private final SimulationParams simulationParams;

  @JsonProperty("startingCash")
  private final double startingCash;

  @JsonProperty("endingCash")
  private final double endingCash;

  @JsonProperty("txns")
  private final List<Transaction> transactions;

  @JsonProperty("stats")
  private final TradeStatistic tradeStatistic;

  @JsonProperty("marks")
  private final List<Mark> marks;

  public SimulationReport(@JsonProperty("params") final SimulationParams simulationParams,
                          @JsonProperty("startingCash") final double startingCash,
                          @JsonProperty("endingCash") final double endingCash,
                          @JsonProperty("txns") final List<Transaction> transactions,
                          @JsonProperty("stats") final TradeStatistic tradeStatistic,
                          @JsonProperty("marks") final List<Mark> marks) {
    this.simulationParams = simulationParams;
    this.startingCash = startingCash;
    this.endingCash = endingCash;
    this.transactions = Lists.newArrayList(transactions);
    this.tradeStatistic = tradeStatistic;
    this.marks = Lists.newArrayList(marks);
  }

  public SimulationParams getSimulationParams() {
    return simulationParams;
  }

  public double getStartingCash() {
    return startingCash;
  }

  public double getEndingCash() {
    return endingCash;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public TradeStatistic getTradeStatistic() {
    return tradeStatistic;
  }

  public List<Mark> getMarks() {
    return marks;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("simulationParams", simulationParams)
        .add("startingCash", startingCash)
        .add("endingCash", endingCash)
        .add("transactions", transactions)
        .add("tradeStatistic", tradeStatistic)
        .add("marks", marks)
        .toString();
  }

  /**
   * Builder
   */
  public static final class Builder {
    private SimulationParams simulationParams;
    private double startingCash;
    private double endingCash;
    private List<Transaction> transactions = Lists.newArrayList();
    private TradeStatistic tradeStatistic;
    private List<Mark> marks = Lists.newArrayList();

    public Builder params(final SimulationParams params) {
      this.simulationParams = params;
      return this;
    }

    public Builder startingCash(final double startingCash) {
      this.startingCash = startingCash;
      return this;
    }

    public Builder endingCash(final double endingCash) {
      this.endingCash = endingCash;
      return this;
    }

    public Builder addTransaction(final Transaction txn) {
      transactions.add(txn);
      return this;
    }

    public Builder addTransactions(final Collection<Transaction> txns) {
      transactions.addAll(txns);
      return this;
    }

    public Builder addTransactions(final Transaction txn, final Transaction ... more) {
      transactions.addAll(Lists.asList(txn, more));
      return this;
    }

    public Builder tradeStatistics(final TradeStatistic tradeStatistic) {
      this.tradeStatistic = tradeStatistic;
      return this;
    }

    public Builder addMark(final Mark mark) {
      marks.add(mark);
      return this;
    }

    public Builder addMarks(final Collection<Mark> marks) {
      marks.addAll(marks);
      return this;
    }

    public Builder addMarks(final Mark mark, final Mark ... more) {
      marks.addAll(Lists.asList(mark, more));
      return this;
    }

    public SimulationReport build() {
      return new SimulationReport(simulationParams, startingCash, endingCash, transactions, tradeStatistic, marks);
    }
  }
}
