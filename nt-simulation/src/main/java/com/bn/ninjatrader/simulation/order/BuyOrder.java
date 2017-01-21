package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class BuyOrder extends Order {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrder.class);

  private final double cashAmount;

  private BuyOrder(final LocalDate orderDate,
                   final MarketTime marketTime,
                   final int barsFromNow,
                   final long numOfShares,
                   final double cashAmount) {
    super(orderDate, TransactionType.BUY, marketTime, barsFromNow, numOfShares);
    this.cashAmount = cashAmount;
  }

  public double getCashAmount() {
    return cashAmount;
  }

  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("marketTime", getMarketTime())
        .add("barsFromNow", getBarsFromNow())
        .add("numOfShares", getNumOfShares())
        .add("orderDate", getOrderDate())
        .add("transactionType", getTransactionType())
        .add("cashAmount", cashAmount)
        .toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof BuyOrder)) { return false; }
    if (obj == this) { return true; }
    final BuyOrder rhs = (BuyOrder) obj;
    return super.equals(obj)
        && Objects.equal(cashAmount, rhs.cashAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), cashAmount);
  }

  /**
   * Builder class for BuyOrder.
   */
  public static class BuyOrderBuilder extends OrderBuilder<BuyOrderBuilder> {
    private double cashAmount;

    public BuyOrderBuilder cashAmount(double cashAmount) {
      this.cashAmount = cashAmount;
      return this;
    }

    @Override
    BuyOrderBuilder getThis() {
      return this;
    }

    @Override
    public BuyOrder build() {
      return new BuyOrder(getOrderDate(), getMarketTime(), getDaysFromNow(), getNumOfShares(), cashAmount);
    }
  }
}
