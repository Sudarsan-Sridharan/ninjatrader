package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataType;

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

    Set<Variable> variables = condition.getVariables();
    assertEquals(variables.size(), 4);
    assertTrue(variables.contains(Variable.of(DataType.PRICE_CLOSE)));
    assertTrue(variables.contains(Variable.of(DataType.PRICE_HIGH)));
    assertTrue(variables.contains(Variable.of(DataType.PRICE_OPEN)));
    assertTrue(variables.contains(Variable.of(DataType.PRICE_LOW)));
  }
}
