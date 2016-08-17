package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.BarParameters;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brad on 8/5/16.
 */
public class AndCondition implements Condition {

  private List<Condition> conditions;

  public AndCondition() {
    conditions = Lists.newArrayList();
  }

  public AndCondition(Condition condition1, Condition condition2) {
    this();
    conditions.add(condition1);
    conditions.add(condition2);
  }

  public AndCondition(Condition condition1, Condition condition2, Condition ... moreConditions) {
    this(condition1, condition2);
    conditions.addAll(Arrays.asList(moreConditions));
  }

  @Override
  public boolean isMatch(BarParameters barParameters) {
    if (conditions.isEmpty()) {
      return true;
    }

    for (Condition condition : conditions) {
      if (!condition.isMatch(barParameters)) {
        return false;
      }
    }
    return true;
  }

  public AndCondition add(Condition condition) {
    if (condition != null) {
      conditions.add(condition);
    }
    return this;
  }
}
