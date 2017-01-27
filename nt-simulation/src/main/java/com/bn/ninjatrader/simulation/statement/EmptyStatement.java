package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmptyStatement implements Statement {
  private static final EmptyStatement INSTANCE = new EmptyStatement();

  public static final EmptyStatement instance() {
    return INSTANCE;
  }

  @Override
  public void run(final World world, final BarData barData) {
    return;// Do nothing
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof EmptyStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }
}
