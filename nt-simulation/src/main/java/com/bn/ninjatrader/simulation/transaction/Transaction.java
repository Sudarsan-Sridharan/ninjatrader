package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.bn.ninjatrader.common.util.NumUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BuyTransaction.class, name = "buy"),
    @JsonSubTypes.Type(value = SellTransaction.class, name = "sell")
})
public abstract class Transaction implements Serializable {

  @JsonProperty("index")
  private final int barIndex; // Bar number. Used to calculate how many bars since last buy / sell.

  @JsonProperty("sym")
  private final String symbol;

  @JsonProperty("dt")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private final LocalDate date;

  @JsonProperty("tnxType")
  private final TransactionType transactionType;

  @JsonProperty("price")
  private final double price;

  @JsonProperty("shares")
  private final long numOfShares;

  public static BuyTransaction.BuyTransactionLogBuilder buy() {
    return BuyTransaction.create();
  }

  public static SellTransaction.SellTransactionBuilder sell() {
    return SellTransaction.create();
  }

  public Transaction(@JsonProperty("sym") String symbol,
                     @JsonProperty("dt")
                     @JsonSerialize(using = NtLocalDateSerializer.class)
                     @JsonDeserialize(using = NtLocalDateDeserializer.class) LocalDate date,
                     @JsonProperty("tnxType") TransactionType transactionType,
                     @JsonProperty("price") double price,
                     @JsonProperty("shares") long numOfShares,
                     @JsonProperty("index") int barIndex) {
    this.symbol = symbol;
    this.date = date;
    this.transactionType = transactionType;
    this.price = price;
    this.numOfShares = numOfShares;
    this.barIndex = barIndex;
  }

  public String getSymbol() {
    return symbol;
  }

  public double getPrice() {
    return price;
  }

  public long getNumOfShares() {
    return numOfShares;
  }

  public double getValue() {
    return NumUtil.multiply(price, numOfShares);
  }

  public LocalDate getDate() {
    return date;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public int getBarIndex() {
    return barIndex;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("date", date)
        .add("action", transactionType)
        .add("shares", numOfShares)
        .add("price", price)
        .add("barIndex", barIndex)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, barIndex, date, transactionType, price, numOfShares);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Transaction)) { return false; }
    if (obj == this) { return true; }
    final Transaction rhs = (Transaction) obj;
    return Objects.equal(symbol, rhs.symbol)
        && Objects.equal(barIndex, rhs.barIndex)
        && Objects.equal(date, rhs.date)
        && Objects.equal(transactionType, rhs.transactionType)
        && Objects.equal(price, rhs.price)
        && Objects.equal(numOfShares, rhs.numOfShares);
  }

  /**
   * Abstract builder for Transaction.
   */
  public static abstract class AbstractTransactionLogBuilder<T extends AbstractTransactionLogBuilder> {
    private LocalDate date;
    private double price;
    private long numOfShares;
    private String symbol;
    private int barIndex;

    public T symbol(String symbol) {
      this.symbol = symbol;
      return getThis();
    }

    public T date(LocalDate date) {
      this.date = date;
      return getThis();
    }

    public T price(double price) {
      this.price = price;
      return getThis();
    }

    public T shares(long numOfShares) {
      this.numOfShares = numOfShares;
      return getThis();
    }

    public T barIndex(int barIndex) {
      this.barIndex = barIndex;
      return getThis();
    }

    public String getSymbol() {
      return symbol;
    }

    public LocalDate getDate() {
      return date;
    }

    public double getPrice() {
      return price;
    }

    public long getNumOfShares() {
      return numOfShares;
    }

    public int getBarIndex() {
      return barIndex;
    }

    public abstract Transaction build();

    public abstract T getThis();
  }
}
