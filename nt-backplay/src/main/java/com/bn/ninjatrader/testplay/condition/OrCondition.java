package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.Parameters;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Brad on 8/5/16.
 */
public class OrCondition implements Condition {

  private Condition[] conditions;

  public OrCondition(Condition condition) {
    conditions = new Condition[] { condition };
  }

  public OrCondition(Condition condition1, Condition condition2) {
    conditions = new Condition[] { condition1, condition2 };
  }

  public OrCondition(Condition condition1, Condition condition2, Condition conditionArray) {
    List<Condition> ruleList = Lists.newArrayList(condition1, condition2, conditionArray);
    conditions = ruleList.toArray(new Condition[0]);
  }

  @Override
  public boolean isMatch(Parameters parameters) {
    for (Condition condition : conditions) {
      if (condition.isMatch(parameters)) {
        return true;
      }
    }
    return false;
  }
}
