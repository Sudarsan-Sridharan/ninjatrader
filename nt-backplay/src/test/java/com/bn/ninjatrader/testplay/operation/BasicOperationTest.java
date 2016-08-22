package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.testplay.type.Operator;
import org.testng.annotations.Test;

import java.util.Set;

import static com.bn.ninjatrader.common.data.DataType.*;
import static com.bn.ninjatrader.testplay.type.Operator.MINUS;
import static com.bn.ninjatrader.testplay.type.Operator.MULTIPLY;
import static com.bn.ninjatrader.testplay.type.Operator.PLUS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/2/16.
 */
public class BasicOperationTest extends AbstractOperationTest {

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
    BasicOperation operation = operation(1.0, PLUS, 2.0);
    Set<DataType> dataTypes = operation.getDataTypes();
    assertEquals(dataTypes.size(), 1);
    assertEquals(dataTypes.iterator().next(), CONSTANT);
  }

  @Test
  public void testGetDataTypesForVariables() {
    BasicOperation operation = operation(PRICE_OPEN, PLUS, PRICE_OPEN);
    Set<DataType> dataTypes = operation.getDataTypes();
    assertEquals(dataTypes.size(), 1);
    assertTrue(dataTypes.contains(PRICE_OPEN));

    operation = operation(PRICE_OPEN, PLUS, PRICE_CLOSE);
    dataTypes = operation.getDataTypes();
    assertEquals(dataTypes.size(), 2);
    assertTrue(dataTypes.contains(PRICE_OPEN));
    assertTrue(dataTypes.contains(PRICE_CLOSE));
  }

  @Test
  public void testGetNestedDataTypes() {
    BasicOperation operation = operation(PRICE_OPEN, PLUS, PRICE_CLOSE);
    operation = operation(operation, MINUS, UnaryOperation.of(1.0));
    operation = operation(operation, MULTIPLY, UnaryOperation.of(PRICE_HIGH));

    Set<DataType> dataTypes = operation.getDataTypes();
    assertEquals(dataTypes.size(), 4);
    assertTrue(dataTypes.contains(PRICE_OPEN));
    assertTrue(dataTypes.contains(PRICE_CLOSE));
    assertTrue(dataTypes.contains(PRICE_HIGH));
    assertTrue(dataTypes.contains(CONSTANT));
  }

  @Test
  public void testAddVariables() {
    assertOperationEquals(operation(PRICE_CLOSE, PLUS, PRICE_HIGH), 6.0);
  }

  private void assertOperationEquals(Operation operation, double expected) {
    assertEquals(operation.getValue(barData), expected);
  }

  private BasicOperation operation(double lhs, Operator operator, double rhs) {
    return operation(UnaryOperation.of(lhs), operator, UnaryOperation.of(rhs));
  }

  private BasicOperation operation(DataType lhs, Operator operator, DataType rhs) {
    return operation(UnaryOperation.of(lhs), operator, UnaryOperation.of(rhs));
  }

  private BasicOperation operation(Operation lhs, Operator operator, Operation rhs) {
    BasicOperation operation = new BasicOperation(lhs, operator, rhs);
    return operation;
  }
}
