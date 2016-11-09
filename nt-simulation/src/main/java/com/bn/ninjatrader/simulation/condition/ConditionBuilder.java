package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.type.InequalityOperator;
import com.google.common.base.Preconditions;

/**
 * Created by Brad on 8/6/16.
 */
public class ConditionBuilder {

  private Condition condition;

  private Operation lhsOperation;


  private ConditionBuilder(Operation lhsOperation) {
    this.lhsOperation = lhsOperation;
  }

  public ConditionBuilder equals(Operation rhsOperation) {
    return completeCondition(InequalityOperator.EQUALS, rhsOperation);
  }

  public ConditionBuilder greaterThan(Operation rhsOperation) {
    this.condition = new BasicCondition(this.lhsOperation, InequalityOperator.GREATER_THAN, rhsOperation);
    return this;
  }

  public ConditionBuilder and(Condition andCondition, Condition ... moreAndConditions) {
    Preconditions.checkNotNull(condition, "First condition must not be empty.");
    this.condition = new AndCondition(this.condition, andCondition, moreAndConditions);
    return this;
  }

  private ConditionBuilder completeCondition(InequalityOperator operator, Operation rhsOperation) {
    Preconditions.checkNotNull("left-hand-side operation must not be empty.");
    this.condition = new BasicCondition(this.lhsOperation, InequalityOperator.GREATER_THAN, rhsOperation);
    this.lhsOperation = null;
    return this;
  }


}
