package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataType;

/**
 * Created by Brad on 8/29/16.
 */
public class Functions {

  private Functions() {}

  public static HistoryFunction barsAgo(Operation operation, int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }

  public static HighestInLastNBarsFunction highestInLastNBars(DataType dataType, int numOfBarsAgo) {
    return new HighestInLastNBarsFunction(Variable.of(dataType), numOfBarsAgo);
  }
  public static HighestInLastNBarsFunction highestInLastNBars(Operation operation, int numOfBarsAgo) {
    return new HighestInLastNBarsFunction(operation, numOfBarsAgo);
  }
}
