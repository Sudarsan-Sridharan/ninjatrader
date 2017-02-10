package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public class SellTransaction extends Transaction {

  public static SellTransactionBuilder builder() {
    return new SellTransactionBuilder();
  }

  @JsonProperty("profit")
  private final double profit;

  @JsonProperty("profitPcnt")
  private final double profitPcnt;

  static SellTransactionBuilder create() {
    return new SellTransactionBuilder();
  }

  public SellTransaction(@JsonProperty("sym") final String symbol,
                         @JsonSerialize(using = NtLocalDateSerializer.class)
                         @JsonDeserialize(using = NtLocalDateDeserializer.class)
                         @JsonProperty("dt") final LocalDate date,
                         @JsonProperty("price") final double price,
                         @JsonProperty("shares") final long numOfShares,
                         @JsonProperty("index") final int barIndex,
                         @JsonProperty("profit") final double profit,
                         @JsonProperty("profitPcnt") final double profitPcnt) {
    super(symbol, date, TransactionType.SELL, price, numOfShares, barIndex);
    this.profit = profit;
    this.profitPcnt = profitPcnt;
  }

  public double getProfit() {
    return profit;
  }

  public double getProfitPcnt() {
    return profitPcnt;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), profit, profitPcnt);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof SellTransaction)) { return false; }
    if (obj == this) { return true; }
    final SellTransaction rhs = (SellTransaction) obj;
    return super.equals(obj)
        && Objects.equal(profit, rhs.profit)
        && Objects.equal(profitPcnt, rhs.profitPcnt);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", getSymbol())
        .add("date", getDate())
        .add("transactionType", getTransactionType())
        .add("shares", getNumOfShares())
        .add("price", getPrice())
        .add("barIndex", getBarIndex())
        .add("profit", profit)
        .add("profitPcnt", profitPcnt)
        .toString();
  }

  /**
   * Builder for SellTransaction.
   */
  public static class SellTransactionBuilder extends AbstractTransactionLogBuilder<SellTransactionBuilder> {
    private double profit;
    private double profitPcnt;

    public SellTransactionBuilder profit(final double profit) {
      this.profit = profit;
      return this;
    }

    public SellTransactionBuilder profitPcnt(double profitPcnt) {
      this.profitPcnt = profitPcnt;
      return this;
    }

    @Override
    public SellTransaction build() {
      return new SellTransaction(getSymbol(), getDate(), getPrice(), getNumOfShares(),
          getBarIndex(), profit, profitPcnt);
    }

    @Override
    public SellTransactionBuilder getThis() {
      return this;
    }


  }
}
