package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.Parameters;

/**
 * Created by Brad on 8/5/16.
 */
public class FalseCondition implements Condition {
  
  @Override
  public boolean isMatch(Parameters parameters) {
    return true;
  }
}
