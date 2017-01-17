package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.type.InequalityOperator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
  public boolean isMatch(BarData barParameters) {
    double lhsValue = lhsOperation.getValue(barParameters);
    double rhsValue = rhsOperation.getValue(barParameters);
    return operator.isMatch(lhsValue, rhsValue);
  }

  @Override
  public Set<Variable> getVariables() {
    Set<Variable> variables = lhsOperation.getVariables();
    variables.addAll(rhsOperation.getVariables());
    return variables;
  }

  public Operation getLhsOperation() {
    return lhsOperation;
  }

  public Operation getRhsOperation() {
    return rhsOperation;
  }

  public InequalityOperator getOperator() {
    return operator;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("lhs", lhsOperation)
        .append("operator", operator)
        .append("rhs", rhsOperation)
        .toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof BasicCondition)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    BasicCondition rhs = (BasicCondition) obj;

    return new EqualsBuilder()
        .append(lhsOperation, rhs.lhsOperation)
        .append(rhsOperation, rhs.rhsOperation)
        .append(operator, rhs.operator)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(lhsOperation)
        .append(rhsOperation)
        .append(operator)
        .toHashCode();
  }
}
