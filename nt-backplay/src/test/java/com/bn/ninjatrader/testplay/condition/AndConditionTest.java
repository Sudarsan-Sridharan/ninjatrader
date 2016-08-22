package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/5/16.
 */
public class AndConditionTest extends MultiConditionTest {

  private Condition trueCondition = new TrueCondition();
  private Condition falseCondition = new FalseCondition();
  private BarData barParameters = new BarData();

  @Test
  public void testNullCondition() {
    AndCondition condition = new AndCondition();
    assertTrue(condition.isMatch(barParameters));
  }

  @Test
  public void testAllTrue() {
    AndCondition condition = new AndCondition(trueCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));
  }

  @Test
  public void testAllFalse() {
    AndCondition condition = new AndCondition(falseCondition, falseCondition);
    assertFalse(condition.isMatch(barParameters));
  }

  @Test
  public void testMixedTrueAndFalse() {
    AndCondition condition = new AndCondition(trueCondition, trueCondition, trueCondition, falseCondition);
    assertFalse(condition.isMatch(barParameters));

    condition = new AndCondition(falseCondition, trueCondition, trueCondition);
    assertFalse(condition.isMatch(barParameters));
  }

  @Test
  public void testAddCondition() {
    AndCondition condition = new AndCondition();
    condition.add(trueCondition);
    condition.add(trueCondition);
    assertTrue(condition.isMatch(barParameters));

    condition.add(falseCondition);
    assertFalse(condition.isMatch(barParameters));
  }

  @Test
  public void testGetDataTypes() {
    AndCondition condition = new AndCondition();
    testGetDataTypes(condition);
  }
}
