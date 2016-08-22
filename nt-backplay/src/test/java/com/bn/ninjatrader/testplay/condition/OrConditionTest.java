package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/5/16.
 */
public class OrConditionTest extends MultiConditionTest {

  private Condition trueCondition = new TrueCondition();
  private Condition falseCondition = new FalseCondition();
  private BarData barParameters = new BarData();

  @Test
  public void testAllTrue() {
    OrCondition condition = new OrCondition(trueCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));
  }

  @Test
  public void testAllFalse() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition);
    assertFalse(condition.isMatch(barParameters));
  }

  @Test
  public void testMixedTrueAndFalse() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition, falseCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));

    condition = new OrCondition(falseCondition, trueCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));
  }

  @Test
  public void testGetDataTypes() {
    OrCondition condition = new OrCondition();
    testGetDataTypes(condition);
  }
}
