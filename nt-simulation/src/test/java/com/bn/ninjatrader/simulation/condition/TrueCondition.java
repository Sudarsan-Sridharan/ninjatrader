package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public class TrueCondition implements Condition {

  @Override
  public boolean isMatch(BarData barParameters) {
    return true;
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public ConditionType getConditionType() {
    return ConditionType.TRUE;
  }
}
