package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.operation.BarOperation;

/**
 * Created by Brad on 8/29/16.
 */
public class Functions {

  private Functions() {}

  public static HistoryFunction barsAgo(final BarOperation operation,
                                        final int numOfBarsAgo) {
    return new HistoryFunction(operation, numOfBarsAgo);
  }

  public static HighestFunction highest(final BarOperation operation) {
    return HighestFunction.of(operation);
  }
}
