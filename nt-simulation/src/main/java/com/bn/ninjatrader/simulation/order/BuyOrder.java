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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .appendSuper(super.toString())
        .append("cashAmount", cashAmount)
        .toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof BuyOrder)) { return false; }
    if (obj == this) { return true; }
    final BuyOrder rhs = (BuyOrder) obj;

    return new EqualsBuilder()
        .appendSuper(super.equals(obj))
        .append(cashAmount, rhs.cashAmount)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .appendSuper(super.hashCode())
        .append(cashAmount)
        .toHashCode();
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
      BuyOrder order = new BuyOrder(getOrderDate(), getMarketTime(), getDaysFromNow(), getNumOfShares(), cashAmount);
      order.toString();
      return order;
    }
  }
}
