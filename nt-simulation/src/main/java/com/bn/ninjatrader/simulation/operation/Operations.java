package com.bn.ninjatrader.simulation.operation;

/**
 * Created by Brad on 8/2/16.
 */
public class Operations {

  private Operations() {}

  public static Operation of(double constant) {
    return Constant.of(constant);
  }

  public static ArithmeticOperation create(double constant) {
    return new ArithmeticOperation(constant);
  }

  public static ArithmeticOperation create(Operation operation) {
    return new ArithmeticOperation(operation);
  }
}
