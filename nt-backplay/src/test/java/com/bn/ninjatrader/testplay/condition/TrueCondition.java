package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.BarParameters;

/**
 * Created by Brad on 8/5/16.
 */
public class TrueCondition implements Condition {

  @Override
  public boolean isMatch(BarParameters barParameters) {
    return true;
  }
}
