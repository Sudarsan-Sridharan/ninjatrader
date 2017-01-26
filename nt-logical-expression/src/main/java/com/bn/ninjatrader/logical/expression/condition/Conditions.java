package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operator.InequalityOperator;
import com.bn.ninjatrader.logical.expression.operation.Constant;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Operations;

/**
 * Created by Brad on 8/5/16.
 */
public class Conditions {

  private Conditions() {}

  public static AndCondition<Data> create() {
    return new AndCondition();
  }
  public static AndCondition<Data> create(Condition condition1, Condition condition2) {
    return new AndCondition(condition1, condition2);
  }
  public static AndCondition<Data> create(Condition condition1, Condition condition2, Condition ... moreConditions) {
    return new AndCondition(condition1, condition2, moreConditions);
  }

  public static OrCondition<Data> or(Condition condition1, Condition condition2) {
    return new OrCondition(condition1, condition2);
  }
  public static OrCondition<Data> or(Condition condition1, Condition condition2, Condition ... moreConditions) {
    return new OrCondition(condition1, condition2, moreConditions);
  }

  public static Condition<Data> eq(Operation lhs, double rhs) {
    return eq(lhs, Operations.of(rhs));
  }
  public static Condition<Data> eq(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.EQ, rhs);
  }

  public static Condition<Data> gt(Operation lhs, double rhs) {
    return gt(lhs, Operations.of(rhs));
  }
  public static Condition<Data> gt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GT, rhs);
  }

  public static Condition<Data> gte(Operation lhs, double rhs) {
    return gte(lhs, Operations.of(rhs));
  }
  public static Condition<Data> gte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GTE, rhs);
  }

  public static Condition<Data> lt(Operation lhs, double rhs) {
    return lt(lhs, Operations.of(rhs));
  }
  public static Condition<Data> lt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LT, rhs);
  }

  public static Condition<Data> lte(Operation lhs, double rhs) {
    return lte(lhs, Constant.of(rhs));
  }
  public static Condition<Data> lte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.LTE, rhs);
  }

  private static Condition<Data> condition(Operation lhs, InequalityOperator operator, Operation rhs) {
    return new BasicCondition(lhs, operator, rhs);
  }


}
