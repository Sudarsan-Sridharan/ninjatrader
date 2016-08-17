package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.parameter.BarParameters;

/**
 * Created by Brad on 8/2/16.
 */
public interface Condition {

  boolean isMatch(BarParameters barParameters);
}
