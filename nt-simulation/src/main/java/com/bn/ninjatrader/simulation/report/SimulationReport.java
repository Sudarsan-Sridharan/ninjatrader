package com.bn.ninjatrader.simulation.report;

import com.bn.ninjatrader.simulation.model.Mark;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

  @JsonProperty("symbol")
  private final String symbol;

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

  public SimulationReport(@JsonProperty("symbol") final String symbol,
                          @JsonProperty("startingCash") final double startingCash,
                          @JsonProperty("endingCash") final double endingCash,
                          @JsonProperty("txns") final List<Transaction> transactions,
                          @JsonProperty("stats") final TradeStatistic tradeStatistic,
                          @JsonProperty("marks") final List<Mark> marks) {
    this.symbol = symbol;
    this.startingCash = startingCash;
    this.endingCash = endingCash;
    this.transactions = Lists.newArrayList(transactions);
    this.tradeStatistic = tradeStatistic;
    this.marks = Lists.newArrayList(marks);
  }

  public String getSymbol() {
    return symbol;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimulationReport that = (SimulationReport) o;
    return Double.compare(that.startingCash, startingCash) == 0 &&
        Double.compare(that.endingCash, endingCash) == 0 &&
        Objects.equal(symbol, that.symbol) &&
        Objects.equal(transactions, that.transactions) &&
        Objects.equal(tradeStatistic, that.tradeStatistic) &&
        Objects.equal(marks, that.marks);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, startingCash, endingCash, transactions, tradeStatistic, marks);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
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
    private String symbol;
    private double startingCash;
    private double endingCash;
    private List<Transaction> transactions = Lists.newArrayList();
    private TradeStatistic tradeStatistic;
    private List<Mark> marks = Lists.newArrayList();

    public Builder symbol(final String symbol) {
      this.symbol = symbol;
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
      this.marks.add(mark);
      return this;
    }

    public Builder addMarks(final Collection<Mark> marks) {
      this.marks.addAll(marks);
      return this;
    }

    public Builder addMarks(final Mark mark, final Mark ... more) {
      this.marks.addAll(Lists.asList(mark, more));
      return this;
    }

    public SimulationReport build() {
      return new SimulationReport(symbol, startingCash, endingCash, transactions, tradeStatistic, marks);
    }
  }
}
