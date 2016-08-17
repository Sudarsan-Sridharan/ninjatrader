package com.bn.ninjatrader.testplay.operation;

import org.testng.annotations.Test;

import static com.bn.ninjatrader.testplay.type.DataType.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/2/16.
 */
public class OperationsTest extends AbstractOperationTest {

  @Test
  public void testWithSingleOperand() {
    Operation operation = Operations.startWith(1.0).build();
    assertEquals(operation.getValue(parameters), 1.0);

    operation = Operations.startWith(PRICE_OPEN).build();
    assertEquals(operation.getValue(parameters), 1.0);
  }

  @Test
  public void testWithConstants() {
    Operation operation = Operations.startWith(1.0).plus(2.1).plus(3.2).build();
    assertEquals(operation.getValue(parameters), 6.3);

    operation = Operations.startWith(10).minus(4).minus(1).build();
    assertEquals(operation.getValue(parameters), 5.0);

    operation = Operations.startWith(10).plus(4).minus(2).build();
    assertEquals(operation.getValue(parameters), 12.0);

    operation = Operations.startWith(10).minus(4).plus(9).build();
    assertEquals(operation.getValue(parameters), 15.0);
  }

  @Test
  public void testWithDataTypes() {
    Operation operation = Operations.startWith(PRICE_OPEN).plus(PRICE_HIGH).plus(PRICE_LOW).build();
    assertEquals(operation.getValue(parameters), 6.0);

    operation = Operations.startWith(PRICE_CLOSE).minus(PRICE_OPEN).minus(PRICE_HIGH).build();
    assertEquals(operation.getValue(parameters), 1.0);
  }

  @Test
  public void testWithMixedTypes() {
    Operation operation = Operations.startWith(10.0).plus(PRICE_HIGH).build();
    assertEquals(operation.getValue(parameters), 12.0);

    operation = Operations.startWith(PRICE_HIGH).minus(3).build();
    assertEquals(operation.getValue(parameters), -1.0);
  }
}
