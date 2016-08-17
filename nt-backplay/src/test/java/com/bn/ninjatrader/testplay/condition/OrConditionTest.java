package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/5/16.
 */
public class OrConditionTest {

  private Condition trueCondition = new TrueCondition();
  private Condition falseCondition = new FalseCondition();
  private Parameters parameters = new Parameters();

  @Test
  public void testAllTrue() {
    AndCondition condition = new AndCondition(trueCondition, trueCondition);
    assertTrue(condition.isMatch(parameters));
  }

  @Test
  public void testAllFalse() {
    AndCondition condition = new AndCondition(falseCondition, falseCondition);
    assertFalse(condition.isMatch(parameters));
  }

  @Test
  public void testMixedTrueAndFalse() {
    AndCondition condition = new AndCondition(trueCondition, trueCondition, falseCondition);
    assertFalse(condition.isMatch(parameters));

    condition = new AndCondition(falseCondition, trueCondition, trueCondition);
    assertFalse(condition.isMatch(parameters));
  }
}
