package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.simulation.data.BarData;

/**
 * Created by Brad on 8/5/16.
 */
public class OrCondition extends MultiCondition<OrCondition> {

  public OrCondition() {
    super();
  }

  public OrCondition(Condition condition1, Condition condition2) {
    super(condition1, condition2);
  }

  public OrCondition(Condition condition1, Condition condition2, Condition ... moreConditions) {
    super(condition1, condition2, moreConditions);
  }

  @Override
  public boolean isMatch(BarData barParameters) {
    for (Condition condition : getConditions()) {
      if (condition.isMatch(barParameters)) {
        return true;
      }
    }
    return false;
  }

  @Override
  OrCondition getThis() {
    return this;
  }
}
