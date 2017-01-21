package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 8/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
  public boolean isMatch(final BarData barData) {
    if (getConditions().isEmpty()) {
      return true;
    }

    for (final Condition condition : getConditions()) {
      if (condition.isMatch(barData)) {
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
