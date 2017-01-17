package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;

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
  public boolean isMatch(BarData barData) {
    if (getConditions().isEmpty()) {
      return true;
    }

    for (Condition condition : getConditions()) {
      if (!condition.isMatch(barData)) {
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
