package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.OperationType;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestInLastNBarsFunction implements Operation {

  private static final Logger log = LoggerFactory.getLogger(HighestInLastNBarsFunction.class);

  private final int numOfBarsAgo;
  private final Operation operation;

  public HighestInLastNBarsFunction(Operation operation, int numOfBarsAgo) {
    this.numOfBarsAgo = numOfBarsAgo;
    this.operation = operation;
  }

  @Override
  public double getValue(BarData barData) {
    double highestValue = 0;
    for (int i=1; i <= numOfBarsAgo; i++) {
      Optional<BarData> pastBarData = barData.getNBarsAgo(i);
      if (pastBarData.isPresent()) {
        highestValue = Math.max(operation.getValue(pastBarData.get()), highestValue);
      } else {
        return highestValue;
      }
    }
    return highestValue;
  }

  @Override
  public Set<Variable> getVariables() {
    return operation.getVariables();
  }

  @JsonProperty("type")
  public OperationType getOperationType() {
    return OperationType.HIGHEST_LAST_N_BARS;
  }
}
