package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.type.InequalityOperator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

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

  @Override
  @JsonProperty("type")
  public ConditionType getConditionType() {
    return ConditionType.BASIC;
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
}
