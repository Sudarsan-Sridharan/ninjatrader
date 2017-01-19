package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public abstract class Order {
  private static final Logger LOG = LoggerFactory.getLogger(Order.class);

  private LocalDate orderDate;
  private long numOfShares;
  private TransactionType transactionType;
  private MarketTime marketTime = MarketTime.CLOSE;
  private int barsFromNow;

  public static BuyOrder.BuyOrderBuilder buy() {
    return new BuyOrder.BuyOrderBuilder();
  }

  public static SellOrder.SellOrderBuilder sell() {
    return new SellOrder.SellOrderBuilder();
  }

  protected Order(LocalDate orderDate,
                  TransactionType transactionType,
                  MarketTime marketTime,
                  int barsFromNow,
                  long numOfShares) {
    this.orderDate = orderDate;
    this.transactionType = transactionType;
    this.marketTime = marketTime;
    this.barsFromNow = barsFromNow;
    this.numOfShares = numOfShares;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public long getNumOfShares() {
    return numOfShares;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public MarketTime getMarketTime() {
    return marketTime;
  }

  public void decrementBarsFromNow() {
    barsFromNow--;
  }

  public boolean isReadyForProcessing() {
    return barsFromNow <= 0;
  }

  public int getBarsFromNow() {
    return barsFromNow;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("orderDate", orderDate)
        .append("numOfShares", numOfShares)
        .append("transactionType", transactionType)
        .append("marketTime", marketTime)
        .append("barsFromNow", barsFromNow)
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Order)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Order rhs = (Order) obj;

    return new EqualsBuilder()
        .append(orderDate, rhs.orderDate)
        .append(transactionType, rhs.transactionType)
        .append(numOfShares, rhs.numOfShares)
        .append(marketTime, rhs.marketTime)
        .append(barsFromNow, rhs.barsFromNow)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(orderDate)
        .append(transactionType)
        .append(numOfShares)
        .append(marketTime)
        .append(barsFromNow)
        .toHashCode();
  }
}
