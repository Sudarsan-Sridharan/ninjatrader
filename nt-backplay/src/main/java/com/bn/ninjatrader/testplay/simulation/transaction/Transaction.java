package com.bn.ninjatrader.testplay.simulation.transaction;

import com.bn.ninjatrader.common.util.NumUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public abstract class Transaction {

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

  public Transaction(String symbol, LocalDate date, TransactionType transactionType, double price, long numOfShares) {
    this.symbol = symbol;
    this.date = date;
    this.transactionType = transactionType;
    this.price = price;
    this.numOfShares = numOfShares;
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

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("date", date)
        .append("action", transactionType)
        .append("shares", numOfShares)
        .append("price", price)
        .build();
  }

  public static abstract class AbstractTransactionLogBuilder<T extends AbstractTransactionLogBuilder> {
    private LocalDate date;
    private double price;
    private long numOfShares;
    private String symbol;

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

    public abstract Transaction build();

    public abstract T getThis();
  }
}
