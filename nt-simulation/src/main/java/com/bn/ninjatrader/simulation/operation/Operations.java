package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.DataType;

/**
 * Created by Brad on 8/2/16.
 */
public class Operations {

  private Operations() {}

  public static Operation of(Variable variable) {
    return UnaryOperation.of(variable);
  }

  public static Operation of(double constant) {
    return UnaryOperation.of(constant);
  }

  public static ArithmeticOperation create(DataType dataType) {
    return new ArithmeticOperation(dataType);
  }

  public static ArithmeticOperation create(double constant) {
    return new ArithmeticOperation(constant);
  }

  public static ArithmeticOperation create(Operation operation) {
    return new ArithmeticOperation(operation);
  }
}
