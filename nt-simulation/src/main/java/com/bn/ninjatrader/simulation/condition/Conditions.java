package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Operations;
import com.bn.ninjatrader.simulation.operation.UnaryOperation;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataType;
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

  public static Condition eq(DataType lhs, double rhs) {
    return eq(Variable.of(lhs), Operations.of(rhs));
  }
  public static Condition eq(Operation lhs, double rhs) {
    return eq(lhs, Operations.of(rhs));
  }
  public static Condition eq(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.EQUALS, rhs);
  }


  public static Condition gt(Operation lhs, double rhs) {
    return gt(lhs, Operations.of(rhs));
  }
  public static Condition gt(DataType lhs, Operation rhs) {
    return gt(Variable.of(lhs), rhs);
  }
  public static Condition gt(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GREATER_THAN, rhs);
  }


  public static Condition gte(DataType lhs, double rhs) {
    return gte(Variable.of(lhs), Operations.of(rhs));
  }
  public static Condition gte(Operation lhs, double rhs) {
    return gte(lhs, Operations.of(rhs));
  }
  public static Condition gte(Operation lhs, Operation rhs) {
    return condition(lhs, InequalityOperator.GREATER_THAN_EQUALS, rhs);
  }

  public static Condition lt(DataType lhs, double rhs) {
    return lt(Variable.of(lhs), Operations.of(rhs));
  }
  public static Condition lt(DataType lhs, Operation rhs) {
    return lt(Variable.of(lhs), rhs);
  }
  public static Condition lt(Operation lhs, double rhs) {
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
