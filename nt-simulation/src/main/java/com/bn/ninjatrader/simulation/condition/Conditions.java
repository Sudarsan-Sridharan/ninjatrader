package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Constant;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Operations;
import com.bn.ninjatrader.simulation.type.InequalityOperator;

/**
 * Created by Brad on 8/5/16.
 */
public class Conditions {

  private Conditions() {}

  public static AndCondition create() {
    return new AndCondition();
  }
  public static AndCondition create(Condition condition1, Condition condition2) {
    return new AndCondition(condition1, condition2);
  }
  public static AndCondition create(Condition condition1, Condition condition2, Condition ... moreConditions) {
    return new AndCondition(condition1, condition2, moreConditions);
  }

  public static OrCondition or(Condition condition1, Condition condition2) {
    return new OrCondition(condition1, condition2);
  }
  public static OrCondition or(Condition condition1, Condition condition2, Condition ... moreConditions) {
    return new OrCondition(condition1, condition2, moreConditions);
  }

  public static Condition eq(Operation lhs, double rhs) {
    return eq(lhs, Operations.of(rhs));
  }
  public static Condition eq(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.EQ, rhs);
  }

  public static Condition gt(Operation lhs, double rhs) {
    return gt(lhs, Operations.of(rhs));
  }
  public static Condition gt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GT, rhs);
  }

  public static Condition gte(Operation lhs, double rhs) {
    return gte(lhs, Operations.of(rhs));
  }
  public static Condition gte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GTE, rhs);
  }

  public static Condition lt(Operation lhs, double rhs) {
    return lt(lhs, Operations.of(rhs));
  }
  public static Condition lt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LT, rhs);
  }

  public static Condition lte(Operation lhs, double rhs) {
    return lte(lhs, Constant.of(rhs));
  }
  public static Condition lte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LTE, rhs);
  }

  private static Condition condition(Operation lhs, InequalityOperator operator, Operation rhs) {
    return new BasicCondition(lhs, operator, rhs);
  }


}
