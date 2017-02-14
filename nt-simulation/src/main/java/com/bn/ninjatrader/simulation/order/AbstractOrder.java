package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/12/16.
 */
public abstract class AbstractOrder implements Order {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractOrder.class);

  private final LocalDate orderDate;
  private final String symbol;
  private final long numOfShares;
  private final TransactionType transactionType;
  private final OrderType orderType;
  private final OrderConfig orderConfig;

  protected AbstractOrder(final LocalDate orderDate,
                          final String symbol,
                          final TransactionType transactionType,
                          final OrderType orderType,
                          final OrderConfig orderConfig,
                          final long numOfShares) {
    this.orderDate = orderDate;
    this.symbol = symbol;
    this.transactionType = transactionType;
    this.orderType = orderType;
    this.orderConfig = orderConfig;
    this.numOfShares = numOfShares;
  }

  @Override
  public LocalDate getOrderDate() {
    return orderDate;
  }

  @Override
  public long getNumOfShares() {
    return numOfShares;
  }

  @Override
  public TransactionType getTransactionType() {
    return transactionType;
  }

  @Override
  public OrderType getOrderType() {
    return orderType;
  }

  @Override
  public OrderConfig getOrderConfig() {
    return orderConfig;
  }

  @Override
  public String getSymbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("orderDate", orderDate)
        .add("symbol", symbol)
        .add("numOfShares", numOfShares)
        .add("transactionType", transactionType)
        .add("orderType", orderType)
        .add("orderConfig", orderConfig)
        .toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof AbstractOrder)) { return false; }
    if (obj == this) { return true; }
    final AbstractOrder rhs = (AbstractOrder) obj;
    return Objects.equal(orderDate, rhs.orderDate)
        && Objects.equal(symbol, rhs.symbol)
        && Objects.equal(transactionType, rhs.transactionType)
        && Objects.equal(numOfShares, rhs.numOfShares)
        && Objects.equal(orderType, rhs.orderType)
        && Objects.equal(orderConfig, rhs.orderConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderDate, symbol, transactionType, numOfShares, orderType, orderConfig);
  }

  /**
   * Builder class
   */
  abstract static class OrderBuilder<T extends OrderBuilder> {
    private String symbol;
    private long numOfShares;
    private LocalDate orderDate;
    private OrderType orderType = OrderTypes.marketClose();
    private OrderConfig orderConfig = OrderConfig.defaults();

    public T date(final LocalDate orderDate) {
      this.orderDate = orderDate;
      return getThis();
    }

    public T symbol(final String symbol) {
      this.symbol = symbol;
      return getThis();
    }

    public T shares(final long numOfShares) {
      this.numOfShares = numOfShares;
      return getThis();
    }

    public T type(final OrderType orderType) {
      this.orderType = orderType;
      return getThis();
    }

    public T config(final OrderConfig orderConfig) {
      this.orderConfig = orderConfig;
      return getThis();
    }

    public LocalDate getOrderDate() {
      return orderDate;
    }

    public long getNumOfShares() {
      return numOfShares;
    }

    public OrderType getOrderType() {
      return orderType;
    }

    public OrderConfig getOrderConfig() {
      return orderConfig;
    }

    public String getSymbol() {
      return symbol;
    }

    abstract T getThis();

    public abstract AbstractOrder build();
  }
}
