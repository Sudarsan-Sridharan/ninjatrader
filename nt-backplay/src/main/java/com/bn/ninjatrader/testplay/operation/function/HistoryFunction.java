package com.bn.ninjatrader.testplay.operation.function;

import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.google.common.base.Optional;

import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryFunction implements Operation {

  private final int numOfBarsAgo;
  private final Operation operation;

  public HistoryFunction(Operation operation, int numOfBarsAgo) {
    this.numOfBarsAgo = numOfBarsAgo;
    this.operation = operation;
  }

  @Override
  public double getValue(BarData barData) {
    Optional<BarData> pastBarData = barData.getNBarsAgo(numOfBarsAgo);
    if (pastBarData.isPresent()) {
      return operation.getValue(pastBarData.get());
    }
    return 0;
  }

  @Override
  public Set<DataType> getDataTypes() {
    return operation.getDataTypes();
  }
}
