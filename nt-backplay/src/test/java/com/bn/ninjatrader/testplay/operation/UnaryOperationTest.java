package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.common.data.DataType;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/2/16.
 */
public class UnaryOperationTest extends AbstractOperationTest {

  @Test
  public void testWithConstant() {
    UnaryOperation operation = UnaryOperation.of(1.0);
    assertEquals(operation.getValue(barData), 1.0);

    operation = UnaryOperation.of(0.0009);
    assertEquals(operation.getValue(barData), 0.0009);

    operation = UnaryOperation.of(12345678.9);
    assertEquals(operation.getValue(barData), 12345678.9);
  }

  @Test
  public void testWithDataType() {
    UnaryOperation operation = UnaryOperation.of(DataType.PRICE_OPEN);
    assertEquals(operation.getValue(barData), 1.0);

    operation = UnaryOperation.of(DataType.PRICE_HIGH);
    assertEquals(operation.getValue(barData), 2.0);

    operation = UnaryOperation.of(DataType.PRICE_LOW);
    assertEquals(operation.getValue(barData), 3.0);

    operation = UnaryOperation.of(DataType.PRICE_CLOSE);
    assertEquals(operation.getValue(barData), 4.0);
  }

  @Test
  public void testGetDataTypes() {
    UnaryOperation operation = UnaryOperation.of(DataType.PRICE_OPEN);
    Set<DataType> dataTypes = operation.getDataTypes();
    assertEquals(dataTypes.size(), 1);
    assertEquals(dataTypes.iterator().next(), DataType.PRICE_OPEN);

    operation = UnaryOperation.of(1.0);
    dataTypes = operation.getDataTypes();
    assertEquals(dataTypes.size(), 1);
    assertEquals(dataTypes.iterator().next(), DataType.CONSTANT);
  }
}
