package com.bn.ninjatrader.testplay.operation.function;

import com.bn.ninjatrader.testplay.operation.Operation;

/**
 * Created by Brad on 8/29/16.
 */
public class Functions {

  private Functions() {}

  public static HistoryFunction history(Operation operation, int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }

  public static HighestInLastNBarsFunction highestInLastNBars(Operation operation, int numOfBarsAgo) {
    return new HighestInLastNBarsFunction(operation, numOfBarsAgo);
  }
}
