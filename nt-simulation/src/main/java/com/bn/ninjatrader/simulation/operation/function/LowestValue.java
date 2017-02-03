package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.common.util.NumUtil;
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
public class LowestValue extends AbstractFunctionWithNumOfBars {
  private static final Logger LOG = LoggerFactory.getLogger(LowestValue.class);
  private static final String TO_STRING_FORMAT = "lowestOf%sBarsAgo(%s)";

  public static final LowestValue of(final Operation<BarData> operation, final int numOfBarsAgo) {
    return new LowestValue(operation, numOfBarsAgo);
  }

  public static final LowestValue of(final Operation<BarData> operation) {
    return new LowestValue(operation, 0);
  }

  public LowestValue(@JsonProperty("operation") final Operation<BarData> operation,
                     @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    super(operation, numOfBarsAgo);
  }

  public LowestValue inNumOfBarsAgo(final int numOfBarsAgo) {
    return LowestValue.of(getOperation(), numOfBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    final History history = barData.getWorld().getHistory();
    double lowestValue = Double.MAX_VALUE;
    for (int i=1; i <= getNumOfBarsAgo(); i++) {
      final Optional<BarData> pastBarData = history.getNBarsAgo(i);
      if (pastBarData.isPresent()) {
        lowestValue = Math.min(getOperation().getValue(pastBarData.get()), lowestValue);
      } else {
        return lowestValue;
      }
    }
    return lowestValue;
  }

  @Override
  public String toString(final BarData barData) {
    return String.format(TO_STRING_FORMAT, getNumOfBarsAgo(), NumUtil.trim(getValue(barData), 4));
  }
}
