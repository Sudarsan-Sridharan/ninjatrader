package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.operation.Operations;
import com.bn.ninjatrader.testplay.type.DataType;
import com.bn.ninjatrader.testplay.type.InequalityOperator;

/**
 * Created by Brad on 8/5/16.
 */
public class Conditions {

  private Conditions() {}

  public static AndCondition create() {
    return new AndCondition();
  }

  public static Condition eq(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.EQUALS, rhs);
  }

  public static Condition eq(DataType lhs, DataType rhs) {
    return condition(Operations.of(lhs), InequalityOperator.EQUALS, Operations.of(rhs));
  }

  public static Condition eq(DataType lhs, double rhs) {
    return condition(Operations.of(lhs), InequalityOperator.EQUALS, Operations.of(rhs));
  }

  public static Condition gt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GREATER_THAN, rhs);
  }

  public static Condition gt(DataType lhs, DataType rhs) {
    return condition(Operations.of(lhs), InequalityOperator.GREATER_THAN, Operations.of(rhs));
  }

  public static Condition gt(DataType lhs, double rhs) {
    return condition(Operations.of(lhs), InequalityOperator.GREATER_THAN, Operations.of(rhs));
  }

  public static Condition gte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GREATER_THAN_EQUALS, rhs);
  }

  public static Condition gte(DataType lhs, DataType rhs) {
    return condition(Operations.of(lhs), InequalityOperator.GREATER_THAN_EQUALS, Operations.of(rhs));
  }

  public static Condition gte(DataType lhs, double rhs) {
    return condition(Operations.of(lhs), InequalityOperator.GREATER_THAN_EQUALS, Operations.of(rhs));
  }

  public static Condition lt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LESS_THAN, rhs);
  }

  public static Condition lt(DataType lhs, DataType rhs) {
    return condition(Operations.of(lhs), InequalityOperator.LESS_THAN, Operations.of(rhs));
  }

  public static Condition lt(DataType lhs, double rhs) {
    return condition(Operations.of(lhs), InequalityOperator.LESS_THAN, Operations.of(rhs));
  }

  public static Condition lte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LESS_THAN_OR_EQUALS, rhs);
  }

  public static Condition lte(DataType lhs, DataType rhs) {
    return condition(Operations.of(lhs), InequalityOperator.LESS_THAN_OR_EQUALS, Operations.of(rhs));
  }

  private static Condition condition(Operation lhs, InequalityOperator operator, Operation rhs) {
    return new BasicCondition(lhs, operator, rhs);
  }


}
