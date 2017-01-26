package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestFunction extends AbstractFunctionWithNumOfBars {
  private static final Logger LOG = LoggerFactory.getLogger(HighestFunction.class);

  public static final HighestFunction of(final Operation<BarData> operation, final int nBarsAgo) {
    return new HighestFunction(operation, nBarsAgo);
  }

  public static final HighestFunction of(final Operation<BarData> operation) {
    return new HighestFunction(operation, 0);
  }

  public HighestFunction(@JsonProperty("operation") final Operation<BarData> operation,
                         @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    super(operation, numOfBarsAgo);
  }

  public HighestFunction withinNumOfBarsAgo(final int numOfBarsAgo) {
    return HighestFunction.of(getOperation(), numOfBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    double highestValue = 0;
    for (int i=1; i <= getNumOfBarsAgo(); i++) {
      final Optional<BarData> pastBarData = barData.getHistory().getNBarsAgo(i);
      if (pastBarData.isPresent()) {
        highestValue = Math.max(getOperation().getValue(pastBarData.get()), highestValue);
      } else {
        return highestValue;
      }
    }
    return highestValue;
  }
}
