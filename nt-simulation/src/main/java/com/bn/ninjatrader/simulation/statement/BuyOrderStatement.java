package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.order.Order;
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

  @JsonProperty("barsFromNow")
  private final int barsFromNow;

  public BuyOrderStatement(@JsonProperty("orderType") final OrderType orderType,
                           @JsonProperty("barsFromNow") final int barsFromNow) {
    this.orderType = orderType;
    this.barsFromNow = barsFromNow;
  }

  @Override
  public void run(final BarData barData) {
    final World world = barData.getWorld();
    final Broker broker = world.getBroker();
    final Account account = world.getAccount();

    if (!account.hasShares() && !broker.hasPendingOrder()) {
      final Price price = barData.getPrice();
      final Order order = Order.buy()
          .date(price.getDate()).cashAmount(account.getCash()).at(orderType).barsFromNow(barsFromNow).build();
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

  public int getBarsFromNow() {
    return barsFromNow;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof  BuyOrderStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final BuyOrderStatement rhs = (BuyOrderStatement) obj;
    return Objects.equal(orderType, rhs.orderType)
        && Objects.equal(barsFromNow, rhs.barsFromNow);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderType, barsFromNow);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("orderType", orderType).add("barsFromNow", barsFromNow).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private OrderType orderType = OrderTypes.marketClose();
    private int barsFromNow;

    public Builder orderType(final OrderType orderType) {
      this.orderType = orderType;
      return this;
    }

    public Builder barsFromNow(final int barsFromNow) {
      this.barsFromNow = barsFromNow;
      return this;
    }

    public BuyOrderStatement build() {
      return new BuyOrderStatement(orderType, barsFromNow);
    }
  }
}
