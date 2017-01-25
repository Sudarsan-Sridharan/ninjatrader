package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.type.InequalityOperator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/5/16.
 */
public class BasicCondition implements Condition {

  @JsonProperty("lhs")
  private final Operation lhsOperation;

  @JsonProperty("rhs")
  private final Operation rhsOperation;

  @JsonProperty("operator")
  private final InequalityOperator operator;

  public BasicCondition(@JsonProperty("lhs") Operation lhsOperation,
                        @JsonProperty("operator") InequalityOperator operator,
                        @JsonProperty("rhs") Operation rhsOperation) {
    checkNotNull(lhsOperation, "LHS operation must not be null.");
    checkNotNull(operator, "Operator must not be null.");
    checkNotNull(rhsOperation, "RHS operation must not be null.");

    this.lhsOperation = lhsOperation;
    this.rhsOperation = rhsOperation;
    this.operator = operator;
  }

  @Override
  public boolean isMatch(final BarData barData) {
    double lhsValue = lhsOperation.getValue(barData);
    double rhsValue = rhsOperation.getValue(barData);
    return operator.isMatch(lhsValue, rhsValue);
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> variables = lhsOperation.getVariables();
    variables.addAll(rhsOperation.getVariables());
    return variables;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("lhs", lhsOperation).add("operator", operator).add("rhs", rhsOperation)
        .toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof BasicCondition)) { return false; }
    if (obj == this) { return true; }
    final BasicCondition rhs = (BasicCondition) obj;
    return Objects.equal(lhsOperation, rhs.lhsOperation)
        && Objects.equal(rhsOperation, rhs.rhsOperation)
        && Objects.equal(operator, rhs.operator);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(lhsOperation, rhsOperation, operator);
  }
}
