package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public class BuyOrder extends AbstractOrder {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrder.class);

  public static final Builder builder() {
    return new Builder();
  }

  private final double cashAmount;

  private BuyOrder(final LocalDate orderDate,
                   final OrderType orderType,
                   final OrderConfig orderConfig,
                   final long numOfShares,
                   final double cashAmount) {
    super(orderDate, TransactionType.BUY, orderType, orderConfig, numOfShares);
    this.cashAmount = cashAmount;
  }

  public double getCashAmount() {
    return cashAmount;
  }

  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("orderType", getOrderType())
        .add("numOfShares", getNumOfShares())
        .add("orderDate", getOrderDate())
        .add("transactionType", getTransactionType())
        .add("cashAmount", cashAmount)
        .toString();
  }

  @Override
  public boolean equals(final Object obj) {
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
  public static class Builder extends OrderBuilder<Builder> {
    private double cashAmount;

    public Builder cashAmount(double cashAmount) {
      this.cashAmount = cashAmount;
      return this;
    }

    @Override
    Builder getThis() {
      return this;
    }

    @Override
    public BuyOrder build() {
      return new BuyOrder(getOrderDate(), getOrderType(), getOrderConfig(), getNumOfShares(), cashAmount);
    }
  }
}
