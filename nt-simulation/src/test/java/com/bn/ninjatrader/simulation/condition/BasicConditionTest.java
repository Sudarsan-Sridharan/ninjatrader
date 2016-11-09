package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.UnaryOperation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.type.InequalityOperator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.bn.ninjatrader.simulation.type.InequalityOperator.EQUALS;
import static com.bn.ninjatrader.simulation.type.InequalityOperator.GREATER_THAN;
import static com.bn.ninjatrader.simulation.type.InequalityOperator.LESS_THAN;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 8/2/16.
 */
public class BasicConditionTest {

  private final BarData barParameters = new BarData();
  private final ObjectMapper om = new ObjectMapper();

  @Test
  public void testUnaryEquals() {
    assertConditionMatch(0, EQUALS, -0);
    assertConditionMatch(4.2345, EQUALS, 4.2345);
    assertConditionMatch(4.2345, EQUALS, 4.23450);

    assertConditionNotMatch(4.2345, EQUALS, 4.23456);
  }

  @Test
  public void testUnaryGreaterThan() {
    assertConditionMatch(4.2345, GREATER_THAN, 4.2344);

    assertConditionNotMatch(4.2345, GREATER_THAN, 4.2345);
    assertConditionNotMatch(4.2345, GREATER_THAN, 5.0);
  }

  @Test
  public void testUnaryLessThan() {
    assertConditionMatch(4.2345, LESS_THAN, 4.2346);

    assertConditionNotMatch(4.2345, LESS_THAN, 4.2345);
    assertConditionNotMatch(4.2345, LESS_THAN, 3.0);
  }

  @Test //TODO
  public void testSerializationDeserialization() throws IOException {
//    Condition condition = new BasicCondition(UnaryOperation.of(3.0),
//        InequalityOperator.GREATER_THAN, UnaryOperation.of(1.0));
//    String serialized = om.writeValueAsString(condition);
//
//    Condition deserialised = om.readValue(serialized, Condition.class);
  }

  private void assertConditionMatch(double lhsValue, InequalityOperator operator, double rhsValue) {
    assertConditionMatch(UnaryOperation.of(lhsValue), operator, UnaryOperation.of(rhsValue));
  }

  private void assertConditionMatch(Operation lhsOperation, InequalityOperator operator, Operation rhsOperation) {
    Condition condition = new BasicCondition(lhsOperation, operator, rhsOperation);
    assertTrue(condition.isMatch(barParameters));
  }

  private void assertConditionNotMatch(double lhsValue, InequalityOperator operator, double rhsValue) {
    assertConditionNotMatch(UnaryOperation.of(lhsValue), operator, UnaryOperation.of(rhsValue));
  }

  private void assertConditionNotMatch(Operation lhsOperation, InequalityOperator operator, Operation rhsOperation) {
    Condition condition = new BasicCondition(lhsOperation, operator, rhsOperation);
    assertFalse(condition.isMatch(barParameters));
  }
}
