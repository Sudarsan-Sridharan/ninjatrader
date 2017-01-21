package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public class TrueCondition implements Condition {

  private static TrueCondition INSTANCE;

  public static final TrueCondition instance() {
    if (INSTANCE == null) {
      INSTANCE = new TrueCondition();
    }
    return INSTANCE;
  }

  @Override
  public boolean isMatch(BarData barParameters) {
    return true;
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }
}
