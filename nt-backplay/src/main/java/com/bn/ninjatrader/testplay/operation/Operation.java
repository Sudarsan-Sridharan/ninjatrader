package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.parameter.BarParameters;

/**
 * Created by Brad on 8/2/16.
 */
public interface Operation {
  double getValue(BarParameters barParameters);
}
