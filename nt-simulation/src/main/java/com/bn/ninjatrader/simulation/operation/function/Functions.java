package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.operation.Operation;

/**
 * Created by Brad on 8/29/16.
 */
public class Functions {

  private Functions() {}

  public static HistoryFunction barsAgo(final Operation operation,
                                        final int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }
}
