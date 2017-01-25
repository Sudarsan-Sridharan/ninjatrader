package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 8/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AndCondition extends MultiCondition<AndCondition> {

  public AndCondition() {
    super();
  }

  public AndCondition(final Condition condition1, final Condition condition2) {
    super(condition1, condition2);
  }

  public AndCondition(final Condition condition1, final Condition condition2, final Condition ... moreConditions) {
    super(condition1, condition2, moreConditions);
  }

  @Override
  public boolean isMatch(final BarData barData) {
    if (getConditions().isEmpty()) {
      return true;
    }

    for (final Condition condition : getConditions()) {
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
