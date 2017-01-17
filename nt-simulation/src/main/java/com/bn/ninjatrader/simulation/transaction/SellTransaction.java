package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public class SellTransaction extends Transaction {

  @JsonProperty("profit")
  private final double profit;

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
                         @JsonProperty("profit") final double profit) {
    super(symbol, date, TransactionType.SELL, price, numOfShares, barIndex);
    this.profit = profit;
  }

  public double getProfit() {
    return profit;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .appendSuper(super.hashCode())
        .append(profit)
        .hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof SellTransaction)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    SellTransaction rhs = (SellTransaction) obj;
    return new EqualsBuilder()
        .appendSuper(super.equals(obj))
        .append(profit, rhs.profit)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .appendSuper(super.toString())
        .append("profit", getProfit())
        .build();
  }

  /**
   * Builder for SellTransaction.
   */
  public static class SellTransactionBuilder extends AbstractTransactionLogBuilder<SellTransactionBuilder> {
    double profit;

    public SellTransactionBuilder profit(double profit) {
      this.profit = profit;
      return this;
    }

    @Override
    public SellTransaction build() {
      return new SellTransaction(getSymbol(), getDate(), getPrice(), getNumOfShares(), getBarIndex(), profit);
    }

    @Override
    public SellTransactionBuilder getThis() {
      return this;
    }
  }
}
