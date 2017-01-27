package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestValue extends AbstractFunctionWithNumOfBars {
  private static final Logger LOG = LoggerFactory.getLogger(HighestValue.class);

  public static final HighestValue of(final Operation<BarData> operation, final int nBarsAgo) {
    return new HighestValue(operation, nBarsAgo);
  }

  public static final HighestValue of(final Operation<BarData> operation) {
    return new HighestValue(operation, 0);
  }

  public HighestValue(@JsonProperty("operation") final Operation<BarData> operation,
                      @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    super(operation, numOfBarsAgo);
  }

  public HighestValue inNumOfBarsAgo(final int numOfBarsAgo) {
    return HighestValue.of(getOperation(), numOfBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    final History history = barData.getWorld().getHistory();
    double highestValue = Double.MIN_VALUE;
    for (int i=1; i <= getNumOfBarsAgo(); i++) {
      final Optional<BarData> pastBarData = history.getNBarsAgo(i);
      if (pastBarData.isPresent()) {
        highestValue = Math.max(getOperation().getValue(pastBarData.get()), highestValue);
      } else {
        return highestValue;
      }
    }
    return highestValue;
  }
}
