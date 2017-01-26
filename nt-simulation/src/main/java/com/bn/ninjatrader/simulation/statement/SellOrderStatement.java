package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.order.MarketTime;
import com.bn.ninjatrader.simulation.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellOrderStatement implements Statement {
  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("marketTime")
  private final MarketTime marketTime;

  @JsonProperty("barsFromNow")
  private final int barsFromNow;

  public SellOrderStatement(@JsonProperty("marketTime") final MarketTime marketTime,
                            @JsonProperty("barsFromNow") final int barsFromNow) {
    this.marketTime = marketTime;
    this.barsFromNow = barsFromNow;
  }

  @Override
  public void run(final World world, final BarData barData) {
    final Broker broker = world.getBroker();
    final Account account = world.getAccount();

    if (account.hasShares()) {
      final Price price = barData.getPrice();
      final Order order = Order.sell().date(price.getDate()).at(marketTime).barsFromNow(barsFromNow).build();
      broker.submitOrder(order, barData);
    }
  }

  public MarketTime getMarketTime() {
    return marketTime;
  }

  public int getBarsFromNow() {
    return barsFromNow;
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof SellOrderStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final SellOrderStatement rhs = (SellOrderStatement) obj;
    return Objects.equal(marketTime, rhs.marketTime)
        && Objects.equal(barsFromNow, rhs.barsFromNow);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(marketTime, barsFromNow);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("marketTime", marketTime).add("barsFromNow", barsFromNow).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private MarketTime marketTime = MarketTime.CLOSE;
    private int barsFromNow;

    public Builder marketTime(final MarketTime marketTime) {
      this.marketTime = marketTime;
      return this;
    }

    public Builder barsFromNow(final int barsFromNow) {
      this.barsFromNow = barsFromNow;
      return this;
    }
    public SellOrderStatement build() {
      return new SellOrderStatement(marketTime, barsFromNow);
    }
  }
}
