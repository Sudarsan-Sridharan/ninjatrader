package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.cancel.CancelType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelOrderStatement implements Statement {
  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("cancelType")
  private final CancelType cancelType;

  public CancelOrderStatement(@JsonProperty("cancelType") final CancelType cancelType) {
    checkNotNull(cancelType, "cancelType must not be null.");
    this.cancelType = cancelType;
  }

  @Override
  public void run(final BarData barData) {
    final List<PendingOrder> pendingOrdersToRemove = cancelType.findPendingOrdersToCancel(barData);
    if (!pendingOrdersToRemove.isEmpty()) {
      cancelOrders(barData, pendingOrdersToRemove);
    }
  }

  private void cancelOrders(final BarData barData,
                            final Collection<PendingOrder> pendingOrdersToRemove) {
    final World world = barData.getWorld();
    final Broker broker = world.getBroker();
    broker.removePendingOrders(pendingOrdersToRemove);
  }

  public CancelType getCancelType() {
    return cancelType;
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof CancelOrderStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final CancelOrderStatement rhs = (CancelOrderStatement) obj;
    return Objects.equal(cancelType, rhs.cancelType);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(cancelType);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("cancelType", cancelType).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private CancelType cancelType;

    public Builder cancelType(final CancelType cancelType) {
      this.cancelType = cancelType;
      return this;
    }

    public CancelOrderStatement build() {
      return new CancelOrderStatement(cancelType);
    }
  }

}
