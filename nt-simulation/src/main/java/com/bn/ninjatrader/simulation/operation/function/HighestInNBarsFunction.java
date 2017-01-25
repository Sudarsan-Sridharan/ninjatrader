package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestInNBarsFunction implements Operation {
  private static final Logger LOG = LoggerFactory.getLogger(HighestInNBarsFunction.class);

  public static final HighestInNBarsFunction of(final Operation operation, final int nBarsAgo) {
    return new HighestInNBarsFunction(operation, nBarsAgo);
  }

  public static final HighestInNBarsFunction of(final Operation operation) {
    return new HighestInNBarsFunction(operation, 0);
  }

  @JsonProperty("numOfBarsAgo")
  private int numOfBarsAgo;

  @JsonProperty("operation")
  private final Operation operation;

  public HighestInNBarsFunction(@JsonProperty("operation") final Operation operation,
                                @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    this.numOfBarsAgo = numOfBarsAgo;
    this.operation = operation;
  }

  public HighestInNBarsFunction withinNumOfBarsAgo(final int numOfBarsAgo) {
    return HighestInNBarsFunction.of(operation, numOfBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    double highestValue = 0;
    for (int i=1; i <= numOfBarsAgo; i++) {
      final Optional<BarData> pastBarData = barData.getHistory().getNBarsAgo(i);
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

  public int getNumOfBarsAgo() {
    return numOfBarsAgo;
  }

  public Operation getOperation() {
    return operation;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof HighestInNBarsFunction)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final HighestInNBarsFunction rhs = (HighestInNBarsFunction) obj;
    return Objects.equal(operation, rhs.operation)
        && Objects.equal(numOfBarsAgo, rhs.numOfBarsAgo);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(operation, numOfBarsAgo);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("operation", operation).add("numOfBarsAgo", numOfBarsAgo).toString();
  }
}
