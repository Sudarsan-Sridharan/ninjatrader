package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
  public void run(final Simulation simulation, final BarData barData) {
    return;// Do nothing
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }
}
