package com.bn.ninjatrader.simulation.operation;

import org.testng.annotations.Test;

import static com.bn.ninjatrader.simulation.data.DataType.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/2/16.
 */
public class OperationsTest extends AbstractOperationTest {

  @Test
  public void testWithSingleOperand() {
    Operation operation = Operations.create(1.0);
    assertOperationEquals(operation, 1.0);

    operation = Operations.create(PRICE_OPEN);
    assertOperationEquals(operation, 1.0);
  }

  @Test
  public void testWithConstants() {
    Operation operation = Operations.create(1.0).plus(2.1).plus(3.2);
    assertOperationEquals(operation, 6.3);

    operation = Operations.create(10).minus(4).minus(1);
    assertOperationEquals(operation, 5.0);

    operation = Operations.create(10).plus(4).minus(2);
    assertOperationEquals(operation, 12.0);

    operation = Operations.create(10).minus(4).plus(9);
    assertOperationEquals(operation, 15.0);
  }

  @Test
  public void testWithVariables() {
    Operation operation = Operations.create(PRICE_OPEN).plus(PRICE_HIGH).plus(PRICE_LOW);
    assertOperationEquals(operation, 6.0);

    operation = Operations.create(PRICE_CLOSE).minus(PRICE_OPEN).minus(PRICE_HIGH);
    assertOperationEquals(operation, 1.0);
  }

  @Test
  public void testWithConstantsAndVaraibles() {
    Operation operation = Operations.create(10.0).plus(PRICE_HIGH);
    assertOperationEquals(operation, 12.0);

    operation = Operations.create(PRICE_HIGH).minus(3);
    assertOperationEquals(operation, -1.0);
    assertEquals(operation.getVariables().size(), 2);
    assertTrue(operation.getVariables().contains(Variable.of(PRICE_HIGH)));
    assertTrue(operation.getVariables().contains(Variable.of(CONSTANT)));
  }

  @Test
  public void testGetVariables() {
    Operation operation = Operations.create(Variable.of(PRICE_HIGH)).minus(3);
    assertOperationEquals(operation, -1.0);
    assertEquals(operation.getVariables().size(), 2);
    assertTrue(operation.getVariables().contains(Variable.of(PRICE_HIGH)));
    assertTrue(operation.getVariables().contains(Variable.of(CONSTANT)));
  }

  private void assertOperationEquals(Operation operation, double expected) {
    assertEquals(operation.getValue(barData), expected);
  }
}
