package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataType;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public class FalseCondition implements Condition {
  
  @Override
  public boolean isMatch(BarData barParameters) {
    return false;
  }

  @Override
  public Set<DataType> getDataTypes() {
    return Collections.emptySet();
  }
}
