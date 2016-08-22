package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.simulation.data.BarData;

/**
 * Created by Brad on 8/5/16.
 */
public class AndCondition extends MultiCondition<AndCondition> {

  public AndCondition() {
    super();
  }

  public AndCondition(Condition condition1, Condition condition2) {
    super(condition1, condition2);
  }

  public AndCondition(Condition condition1, Condition condition2, Condition ... moreConditions) {
    super(condition1, condition2, moreConditions);
  }

  @Override
  public boolean isMatch(BarData barParameters) {
    if (getConditions().isEmpty()) {
      return true;
    }

    for (Condition condition : getConditions()) {
      if (!condition.isMatch(barParameters)) {
        return false;
      }
    }
    return true;
  }

  @Override
  AndCondition getThis() {
    return this;
  }
}
