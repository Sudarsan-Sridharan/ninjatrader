package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.operation.UnaryOperation;
import com.bn.ninjatrader.testplay.parameter.Parameters;
import com.bn.ninjatrader.testplay.rule.BasicCondition;
import com.bn.ninjatrader.testplay.rule.Condition;
import com.bn.ninjatrader.testplay.type.InequalityOperator;
import org.testng.annotations.Test;

import static com.bn.ninjatrader.testplay.type.InequalityOperator.EQUALS;
import static com.bn.ninjatrader.testplay.type.InequalityOperator.GREATER_THAN;
import static com.bn.ninjatrader.testplay.type.InequalityOperator.LESS_THAN;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 8/2/16.
 */
public class BasicConditionTest {

  private Parameters parameters = new Parameters();

  @Test
  public void testUnaryEquals() {
    assertRuleMatch(0, EQUALS, -0);
    assertRuleMatch(4.2345, EQUALS, 4.2345);
    assertRuleMatch(4.2345, EQUALS, 4.23450);

    assertRuleNotMatch(4.2345, EQUALS, 4.23456);
  }

  @Test
  public void testUnaryGreaterThan() {
    assertRuleMatch(4.2345, GREATER_THAN, 4.2344);

    assertRuleNotMatch(4.2345, GREATER_THAN, 4.2345);
    assertRuleNotMatch(4.2345, GREATER_THAN, 5.0);
  }

  @Test
  public void testUnaryLessThan() {
    assertRuleMatch(4.2345, LESS_THAN, 4.2346);

    assertRuleNotMatch(4.2345, LESS_THAN, 4.2345);
    assertRuleNotMatch(4.2345, LESS_THAN, 3.0);
  }

  private void assertRuleMatch(double lhsValue, InequalityOperator operator, double rhsValue) {
    assertRuleMatch(UnaryOperation.of(lhsValue), operator, UnaryOperation.of(rhsValue));
  }

  private void assertRuleMatch(Operation lhsOperation, InequalityOperator operator, Operation rhsOperation) {
    Condition rule = new BasicCondition(lhsOperation, rhsOperation, operator);
    assertTrue(rule.isMatch(parameters));
  }

  private void assertRuleNotMatch(double lhsValue, InequalityOperator operator, double rhsValue) {
    assertRuleNotMatch(UnaryOperation.of(lhsValue), operator, UnaryOperation.of(rhsValue));
  }

  private void assertRuleNotMatch(Operation lhsOperation, InequalityOperator operator, Operation rhsOperation) {
    Condition rule = new BasicCondition(lhsOperation, rhsOperation, operator);
    assertFalse(rule.isMatch(parameters));
  }
}
