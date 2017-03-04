package com.bn.ninjatrader.simulation.logicexpression.operation;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
public abstract class AbstractFunctionWithNumOfBars implements Operation<BarData> {

  @JsonProperty("operation")
  private final Operation<BarData> operation;

  @JsonProperty("numOfBarsAgo")
  private final int numOfBarsAgo;

  public AbstractFunctionWithNumOfBars(@JsonProperty("operation") final Operation<BarData> operation,
                                       @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    this.numOfBarsAgo = numOfBarsAgo;
    this.operation = operation;
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return operation.getVariables();
  }

  protected int getNumOfBarsAgo() {
    return numOfBarsAgo;
  }

  protected Operation<BarData> getOperation() {
    return operation;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof AbstractFunctionWithNumOfBars)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final AbstractFunctionWithNumOfBars rhs = (AbstractFunctionWithNumOfBars) obj;
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
