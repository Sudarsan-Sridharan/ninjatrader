package com.bn.ninjatrader.testplay.simulation.transaction;

import com.bn.ninjatrader.common.util.NumUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public abstract class Transaction {

  private final int barIndex; // Bar number. Used to calculate how many bars since last buy / sell.
  private final String symbol;
  private final LocalDate date;
  private final TransactionType transactionType;
  private final double price;
  private final long numOfShares;

  public static BuyTransaction.BuyTransactionLogBuilder buy() {
    return BuyTransaction.create();
  }

  public static SellTransaction.SellTransactionBuilder sell() {
    return SellTransaction.create();
  }

  public Transaction(String symbol, LocalDate date, TransactionType transactionType,
                     double price, long numOfShares, int barIndex) {
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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("date", date)
        .append("action", transactionType)
        .append("shares", numOfShares)
        .append("price", price)
        .append("barIndex", barIndex)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(symbol)
        .append(barIndex)
        .append(date)
        .append(transactionType)
        .append(price)
        .append(numOfShares)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Transaction)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Transaction rhs = (Transaction) obj;
    return new EqualsBuilder()
        .append(symbol, rhs.symbol)
        .append(barIndex, rhs.barIndex)
        .append(date, rhs.date)
        .append(transactionType, rhs.transactionType)
        .append(price, rhs.price)
        .append(numOfShares, rhs.numOfShares)
        .isEquals();
  }

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
