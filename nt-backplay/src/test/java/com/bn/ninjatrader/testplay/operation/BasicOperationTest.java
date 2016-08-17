package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.type.DataType;
import com.bn.ninjatrader.testplay.type.Operator;
import org.testng.annotations.Test;

import static com.bn.ninjatrader.testplay.type.DataType.PRICE_CLOSE;
import static com.bn.ninjatrader.testplay.type.DataType.PRICE_HIGH;
import static com.bn.ninjatrader.testplay.type.Operator.MINUS;
import static com.bn.ninjatrader.testplay.type.Operator.PLUS;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/2/16.
 */
public class BasicOperationTest extends AbstractOperationTest {

  @Test
  public void testArithmeticOnConstants() {
    assertOperationEquals(operation(3.1, PLUS, 4.2), 7.3);
    assertOperationEquals(operation(-1.1, PLUS, 1.1), 0);
    assertOperationEquals(operation(0, PLUS, -0), 0);

    assertOperationEquals(operation(5.1, MINUS, 4.1), 1.0);
    assertOperationEquals(operation(0, MINUS, 0), 0);
    assertOperationEquals(operation(1, MINUS, 2), -1);
    assertOperationEquals(operation(1, MINUS, -1), 2);
  }

  @Test
  public void testAddVariables() {
    assertOperationEquals(operation(PRICE_CLOSE, PLUS, PRICE_HIGH), 6.0);
  }

  private void assertOperationEquals(Operation operation, double expected) {
    assertEquals(operation.getValue(parameters), expected);
  }

  private Operation operation(double lhs, Operator operator, double rhs) {
    return operation(UnaryOperation.of(lhs), operator, UnaryOperation.of(rhs));
  }

  private Operation operation(DataType lhs, Operator operator, DataType rhs) {
    return operation(UnaryOperation.of(lhs), operator, UnaryOperation.of(rhs));
  }

  private Operation operation(Operation lhs, Operator operator, Operation rhs) {
    Operation operation = new BasicOperation(lhs, operator, rhs);
    return operation;
  }
}
