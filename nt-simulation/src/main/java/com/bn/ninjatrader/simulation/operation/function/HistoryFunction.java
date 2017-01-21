package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataHistory;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryFunction implements Operation {

  private final BarDataHistory history;
  private final int numOfBarsAgo;
  private final Operation operation;

  public HistoryFunction(final BarDataHistory history,
                         final Operation operation,
                         final int numOfBarsAgo) {
    this.history = history;
    this.numOfBarsAgo = numOfBarsAgo;
    this.operation = operation;
  }

  @Override
  public double getValue(final BarData barData) {
    final Optional<BarData> pastBarData = history.getNBarsAgo(numOfBarsAgo);
    if (pastBarData.isPresent()) {
      return operation.getValue(pastBarData.get());
    }
    return 0;
  }

  @Override
  public Set<Variable> getVariables() {
    return operation.getVariables();
  }
}
