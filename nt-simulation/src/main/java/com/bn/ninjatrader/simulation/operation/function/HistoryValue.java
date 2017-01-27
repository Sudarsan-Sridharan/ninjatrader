package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryValue extends AbstractFunctionWithNumOfBars {

  public static final HistoryValue of(final Operation<BarData> operation, final int numOfBarsAgo) {
    return new HistoryValue(operation, numOfBarsAgo);
  }

  public static final HistoryValue of(final Operation<BarData> operation) {
    return new HistoryValue(operation, 0);
  }

  public HistoryValue(@JsonProperty("operation") final Operation<BarData> operation,
                      @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    super(operation, numOfBarsAgo);
  }

  public HistoryValue inNumOfBarsAgo(final int numOfBarsAgo) {
    return HistoryValue.of(getOperation(), numOfBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    final History history = barData.getWorld().getHistory();
    final Optional<BarData> pastBarData = history.getNBarsAgo(getNumOfBarsAgo());
    if (pastBarData.isPresent()) {
      return getOperation().getValue(pastBarData.get());
    }
    return Double.NaN;
  }
}
