package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.common.data.DataType;

import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/5/16.
 */
public abstract class MultiConditionTest {

  public void testGetDataTypes(MultiCondition<? extends MultiCondition> condition) {
    condition.add(Conditions.eq(DataType.PRICE_CLOSE, DataType.PRICE_HIGH));
    condition.add(Conditions.eq(DataType.PRICE_OPEN, DataType.PRICE_LOW));

    Set<DataType> dataTypes = condition.getDataTypes();
    assertEquals(dataTypes.size(), 4);
    assertTrue(dataTypes.contains(DataType.PRICE_CLOSE));
    assertTrue(dataTypes.contains(DataType.PRICE_HIGH));
    assertTrue(dataTypes.contains(DataType.PRICE_OPEN));
    assertTrue(dataTypes.contains(DataType.PRICE_LOW));
  }
}
