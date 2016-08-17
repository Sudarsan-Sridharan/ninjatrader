package com.bn.ninjatrader.testplay.simulation.transaction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public class SellTransaction extends Transaction {

  private final double profit;

  public static SellTransactionBuilder create() {
    return new SellTransactionBuilder();
  }

  private SellTransaction(String symbol, LocalDate date, double price, long numOfShares, double profit) {
    super(symbol, date, Type.SELL, price, numOfShares);
    this.profit = profit;
  }

  public double getProfit() {
    return profit;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", getSymbol())
        .append("date", getDate())
        .append("shares", getNumOfShares())
        .append("price", getPrice())
        .append("profit", getProfit())
        .build();
  }

  public static class SellTransactionBuilder extends AbstractTransactionLogBuilder<SellTransactionBuilder> {
    double profit;

    public SellTransactionBuilder profit(double profit) {
      this.profit = profit;
      return this;
    }

    @Override
    public SellTransaction build() {
      return new SellTransaction(getSymbol(), getDate(), getPrice(), getNumOfShares(), profit);
    }

    @Override
    public SellTransactionBuilder getThis() {
      return this;
    }
  }
}
