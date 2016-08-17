package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.parameter.Parameters;
import com.bn.ninjatrader.testplay.type.InequalityOperator;

/**
 * Created by Brad on 8/5/16.
 */
public class BasicCondition implements Condition {

  private final Operation lhsOperation;
  private final Operation rhsOperation;
  private final InequalityOperator operator;

  public BasicCondition(Operation lhsOperation, Operation rhsOperation, InequalityOperator operator) {
    this.lhsOperation = lhsOperation;
    this.rhsOperation = rhsOperation;
    this.operator = operator;
  }

  @Override
  public boolean isMatch(Parameters parameters) {
    double lhsValue = lhsOperation.getValue(parameters);
    double rhsValue = rhsOperation.getValue(parameters);
    return operator.isMatch(lhsValue, rhsValue);
  }
}
