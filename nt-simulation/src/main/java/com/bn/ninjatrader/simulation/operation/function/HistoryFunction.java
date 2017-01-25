package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryFunction implements Operation {

  public static final HistoryFunction withNBarsAgo(final Operation operation, final int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }

  private final int numOfBarsAgo;
  private final Operation operation;

  public HistoryFunction(final Operation operation,
                         final int numOfBarsAgo) {
    this.numOfBarsAgo = numOfBarsAgo;
    this.operation = operation;
  }

  @Override
  public double getValue(final BarData barData) {
    final Optional<BarData> pastBarData = barData.getHistory().getNBarsAgo(numOfBarsAgo);
    if (pastBarData.isPresent()) {
      return operation.getValue(pastBarData.get());
    }
    return Double.NaN;
  }

  @Override
  public Set<Variable> getVariables() {
    return operation.getVariables();
  }
}
