package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanResult {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("symbol")
  private final String symbol;

  @JsonProperty("profit")
  private final double profit;

  @JsonProperty("profitPcnt")
  private final double profitPcnt;

  @JsonProperty("lastTxn")
  private final Transaction lastTransaction;

  public ScanResult(@JsonProperty("symbol") final String symbol,
                    @JsonProperty("profit") final double profit,
                    @JsonProperty("profitPcnt") final double profitPcnt,
                    @JsonProperty("lastTxn") final Transaction lastTransaction) {
    this.symbol = symbol;
    this.profit = profit;
    this.profitPcnt = profitPcnt;
    this.lastTransaction = lastTransaction;
  }

  public String getSymbol() {
    return symbol;
  }

  public double getProfit() {
    return profit;
  }

  public double getProfitPcnt() {
    return profitPcnt;
  }

  public Transaction getLastTransaction() {
    return lastTransaction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScanResult that = (ScanResult) o;
    return Double.compare(that.profit, profit) == 0 &&
        Double.compare(that.profitPcnt, profitPcnt) == 0 &&
        Objects.equal(symbol, that.symbol) &&
        Objects.equal(lastTransaction, that.lastTransaction);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, profit, profitPcnt, lastTransaction);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("profit", profit)
        .add("profitPcnt", profitPcnt)
        .add("lastTransaction", lastTransaction)
        .toString();
  }

  /**
   * Builder
   */
  public static final class Builder {
    private String symbol;
    private double profit;
    private double profitPcnt;
    private Transaction lastTransaction;

    public Builder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public Builder profit(final double profit) {
      this.profit = profit;
      return this;
    }

    public Builder profitPcnt(final double profitPcnt) {
      this.profitPcnt = profitPcnt;
      return this;
    }

    public Builder lastTransaction(final Transaction lastTransaction) {
      this.lastTransaction = lastTransaction;
      return this;
    }

    public ScanResult build() {
      return new ScanResult(symbol, profit, profitPcnt, lastTransaction);
    }
  }
}
