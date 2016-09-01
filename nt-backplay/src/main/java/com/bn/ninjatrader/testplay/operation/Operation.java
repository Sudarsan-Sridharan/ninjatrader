package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataType;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
public interface Operation {
  double getValue(BarData barData);

  Set<DataType> getDataTypes();
}
