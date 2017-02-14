package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.Portfolio;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyOrderStatement implements Statement {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrderStatement.class);

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("orderType")
  private final OrderType orderType;

  @JsonProperty("config")
  private final OrderConfig orderConfig;

  public BuyOrderStatement(@JsonProperty("orderType") final OrderType orderType,
                           @JsonProperty("config") final OrderConfig orderConfig) {
    this.orderType = orderType;
    this.orderConfig = orderConfig;
  }

  @Override
  public void run(final BarData barData) {
    final World world = barData.getWorld();
    final Broker broker = world.getBroker();
    final Account account = world.getAccount();
    final Portfolio portfolio = account.getPortfolio();

    if (portfolio.isEmpty() && !broker.hasPendingOrder()) {
      final Price price = barData.getPrice();
      final Order order = BuyOrder.builder()
          .symbol(barData.getSymbol())
          .date(price.getDate())
          .cashAmount(account.getLiquidCash())
          .type(orderType)
          .config(orderConfig)
          .build();
      broker.submitOrder(order, barData);
    }
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public OrderConfig getOrderConfig() {
    return orderConfig;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || !(obj instanceof  BuyOrderStatement)) {
      return false;
    }
    final BuyOrderStatement rhs = (BuyOrderStatement) obj;
    return Objects.equal(orderType, rhs.orderType)
        && Objects.equal(orderConfig, rhs.orderConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderType, orderConfig);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("type", orderType).add("orderConfig", orderConfig).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private OrderType orderType = OrderTypes.marketClose();
    private OrderConfig orderConfig = OrderConfig.defaults();

    public Builder orderType(final OrderType orderType) {
      this.orderType = orderType;
      return this;
    }

    public Builder orderConfig(final OrderConfig orderConfig) {
      this.orderConfig = orderConfig;
      return this;
    }

    public BuyOrderStatement build() {
      return new BuyOrderStatement(orderType, orderConfig);
    }
  }
}
