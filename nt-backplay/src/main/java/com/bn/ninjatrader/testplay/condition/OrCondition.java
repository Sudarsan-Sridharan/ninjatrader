package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.BarParameters;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brad on 8/5/16.
 */
public class OrCondition implements Condition {

  private Condition[] conditions;

  public OrCondition(Condition condition1, Condition condition2) {
    conditions = new Condition[] { condition1, condition2 };
  }

  public OrCondition(Condition condition1, Condition condition2, Condition ... conditionArray) {
    List<Condition> conditionList = Lists.newArrayList(condition1, condition2);
    conditionList.addAll(Arrays.asList(conditionArray));
    conditions = conditionList.toArray(new Condition[0]);
  }

  @Override
  public boolean isMatch(BarParameters barParameters) {
    for (Condition condition : conditions) {
      if (condition.isMatch(barParameters)) {
        return true;
      }
    }
    return false;
  }
}
