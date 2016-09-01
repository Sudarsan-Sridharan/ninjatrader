package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.operation.Operations;
import com.bn.ninjatrader.testplay.operation.UnaryOperation;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.type.InequalityOperator;

/**
 * Created by Brad on 8/5/16.
 */
public class Conditions {

  private Conditions() {}

  public static AndCondition create() {
    return new AndCondition();
  }

  public static Condition eq(DataType lhs, double rhs) {
    return eq(Operations.of(lhs), Operations.of(rhs));
  }

  public static Condition eq(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.EQUALS, rhs);
  }

  public static Condition gt(DataType lhs, double rhs) {
    return gt(Operations.of(lhs), Operations.of(rhs));
  }

  public static Condition gt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GREATER_THAN, rhs);
  }

  public static Condition gte(DataType lhs, double rhs) {
    return gte(Operations.of(lhs), Operations.of(rhs));
  }

  public static Condition gte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GREATER_THAN_EQUALS, rhs);
  }

  public static Condition lt(DataType lhs, double rhs) {
    return lt(lhs, Operations.of(rhs));
  }

  public static Condition lt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LESS_THAN, rhs);
  }

  public static Condition lte(Operation lhs, double rhs) {
    return lte(lhs, UnaryOperation.of(rhs));
  }

  public static Condition lte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LESS_THAN_OR_EQUALS, rhs);
  }

  private static Condition condition(Operation lhs, InequalityOperator operator, Operation rhs) {
    return new BasicCondition(lhs, operator, rhs);
  }


}
