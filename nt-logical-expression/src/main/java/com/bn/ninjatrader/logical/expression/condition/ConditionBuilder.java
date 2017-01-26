package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.operator.InequalityOperator;
import com.bn.ninjatrader.logical.expression.operation.Operation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/6/16.
 */
public class ConditionBuilder {

  private Condition condition;

  private Operation lhs;

  private ConditionBuilder(Operation lhs) {
    this.lhs = lhs;
  }

  public ConditionBuilder equals(final Operation rhsOperation) {
    return completeCondition(InequalityOperator.EQ, rhsOperation);
  }

  public ConditionBuilder greaterThan(final Operation rhsOperation) {
    this.condition = new BasicCondition(this.lhs, InequalityOperator.GT, rhsOperation);
    return this;
  }

  public ConditionBuilder and(final Condition andCondition, final Condition ... more) {
    checkNotNull(condition, "First condition must not be empty.");
    this.condition = new AndCondition(this.condition, andCondition, more);
    return this;
  }

  private ConditionBuilder completeCondition(final InequalityOperator operator, final Operation rhsOperation) {
    checkNotNull("left-hand-side operation must not be empty.");
    this.condition = new BasicCondition(this.lhs, operator, rhsOperation);
    this.lhs = null;
    return this;
  }


}
