package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.operation.Operation;

/**
 * Created by Brad on 8/29/16.
 */
public class Functions {

  private Functions() {}

  public static HistoryFunction barsAgo(Operation operation, int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }
  public static HighestInNBarsAgoFunction highestInBarsAgo(Operation operation, int numOfBarsAgo) {
    return new HighestInNBarsAgoFunction(operation, numOfBarsAgo);
  }
}
