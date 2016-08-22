package com.bn.ninjatrader.testplay.simulation.transaction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public class BuyTransaction extends Transaction {

  static BuyTransactionLogBuilder create() {
    return new BuyTransactionLogBuilder();
  }

  private BuyTransaction(String symbol, LocalDate date, double price, long numOfShares) {
    super(symbol, date, TransactionType.BUY, price, numOfShares);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("date", getDate())
        .append("shares", getNumOfShares())
        .append("price", getPrice())
        .build();
  }

  public static class BuyTransactionLogBuilder extends AbstractTransactionLogBuilder<BuyTransactionLogBuilder> {
    @Override
    public BuyTransaction build() {
      return new BuyTransaction(getSymbol(), getDate(), getPrice(), getNumOfShares());
    }

    @Override
    public BuyTransactionLogBuilder getThis() {
      return this;
    }
  }
}
