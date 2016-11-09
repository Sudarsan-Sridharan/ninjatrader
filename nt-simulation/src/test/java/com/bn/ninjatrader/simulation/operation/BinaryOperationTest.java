package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.type.Operator;
import org.testng.annotations.Test;

import java.util.Set;

import static com.bn.ninjatrader.simulation.data.DataType.*;
import static com.bn.ninjatrader.simulation.type.Operator.MINUS;
import static com.bn.ninjatrader.simulation.type.Operator.MULTIPLY;
import static com.bn.ninjatrader.simulation.type.Operator.PLUS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/2/16.
 */
public class BinaryOperationTest extends AbstractOperationTest {

  @Test
  public void testArithmeticOnConstants() {
    assertOperationEquals(operation(5.6, PLUS, 5.8), 11.4);
    assertOperationEquals(operation(-1.1, PLUS, 1.1), 0);
    assertOperationEquals(operation(0, PLUS, -0), 0);

    assertOperationEquals(operation(5.1, MINUS, 4.1), 1.0);
    assertOperationEquals(operation(0, MINUS, 0), 0);
    assertOperationEquals(operation(1, MINUS, 2), -1);
    assertOperationEquals(operation(1, MINUS, -1), 2);
  }

  @Test
  public void testGetDataTypesForConstants() {
    BinaryOperation operation = operation(1.0, PLUS, 2.0);
    Set<Variable> variables = operation.getVariables();
    assertEquals(variables.size(), 1);
    assertEquals(variables.iterator().next().getDataType(), CONSTANT);
  }

  @Test
  public void testGetDataTypesForVariables() {
    BinaryOperation operation = operation(PRICE_OPEN, PLUS, PRICE_OPEN);
    Set<Variable> variables = operation.getVariables();
    assertEquals(variables.size(), 1);
    assertTrue(variables.contains(Variable.of(PRICE_OPEN)));

    operation = operation(PRICE_OPEN, PLUS, PRICE_CLOSE);
    variables = operation.getVariables();
    assertEquals(variables.size(), 2);
    assertTrue(variables.contains(Variable.of(PRICE_OPEN)));
    assertTrue(variables.contains(Variable.of(PRICE_CLOSE)));
  }

  @Test
  public void testGetNestedDataTypes() {
    BinaryOperation operation = operation(PRICE_OPEN, PLUS, PRICE_CLOSE);
    operation = operation(operation, MINUS, UnaryOperation.of(1.0));
    operation = operation(operation, MULTIPLY, UnaryOperation.of(PRICE_HIGH));

    Set<Variable> variables = operation.getVariables();
    assertEquals(variables.size(), 4);
    assertTrue(variables.contains(Variable.of(PRICE_OPEN)));
    assertTrue(variables.contains(Variable.of(PRICE_CLOSE)));
    assertTrue(variables.contains(Variable.of(PRICE_HIGH)));
    assertTrue(variables.contains(Variable.of(CONSTANT)));
  }

  @Test
  public void testAddVariables() {
    assertOperationEquals(operation(PRICE_CLOSE, PLUS, PRICE_HIGH), 6.0);
  }

  private void assertOperationEquals(Operation operation, double expected) {
    assertEquals(operation.getValue(barData), expected);
  }

  private BinaryOperation operation(double lhs, Operator operator, double rhs) {
    return operation(UnaryOperation.of(lhs), operator, UnaryOperation.of(rhs));
  }

  private BinaryOperation operation(DataType lhs, Operator operator, DataType rhs) {
    return operation(UnaryOperation.of(lhs), operator, UnaryOperation.of(rhs));
  }

  private BinaryOperation operation(Operation lhs, Operator operator, Operation rhs) {
    BinaryOperation operation = new BinaryOperation(lhs, operator, rhs);
    return operation;
  }
}
