package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryFunction extends AbstractFunctionWithNumOfBars {

  public static final HistoryFunction withNBarsAgo(final Operation<BarData> operation, final int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }

  public HistoryFunction(@JsonProperty("operation") final Operation<BarData> operation,
                         @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    super(operation, numOfBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    final Optional<BarData> pastBarData = barData.getHistory().getNBarsAgo(getNumOfBarsAgo());
    if (pastBarData.isPresent()) {
      return getOperation().getValue(pastBarData.get());
    }
    return Double.NaN;
  }
}
