package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

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
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }
}
