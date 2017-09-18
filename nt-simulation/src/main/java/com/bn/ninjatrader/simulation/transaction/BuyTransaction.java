package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public class BuyTransaction extends Transaction {

  public static BuyTransactionLogBuilder builder() {
    return new BuyTransactionLogBuilder();
  }

  public BuyTransaction(@JsonProperty("sym") final String symbol,
                        @JsonSerialize(using = NtLocalDateSerializer.class)
                        @JsonDeserialize(using = NtLocalDateDeserializer.class)
                        @JsonProperty("dt") final LocalDate date,
                        @JsonProperty("price") final double price,
                        @JsonProperty("shares") final long numOfShares,
                        @JsonProperty("index") final int barIndex) {
    super(symbol, date, TransactionType.BUY, price, numOfShares, barIndex);
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
        .toString();
  }

  /**
   * Builder for BuyTransaction.
   */
  public static class BuyTransactionLogBuilder extends AbstractTransactionLogBuilder<BuyTransactionLogBuilder> {
    @Override
    public BuyTransaction build() {
      return new BuyTransaction(getSymbol(), getDate(), getPrice(), getNumOfShares(), getBarIndex());
    }

    @Override
    public BuyTransactionLogBuilder getThis() {
      return this;
    }
  }
}
