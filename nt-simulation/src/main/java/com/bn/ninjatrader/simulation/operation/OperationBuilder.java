package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.type.Operator;

/**
 * Created by Brad on 8/5/16.
 */
public class OperationBuilder {

  private Operation operation;


  public static OperationBuilder startWith(Operation operation) {
    return new OperationBuilder(operation);
  }

  public static OperationBuilder startWith(double constant) {
    return new OperationBuilder(constant);
  }

  private OperationBuilder(Operation operation) {
    this.operation = operation;
  }

  private OperationBuilder(double constant) {
    this.operation = Constant.of(constant);
  }

  public OperationBuilder plus(double constant) {
    return plus(Constant.of(constant));
  }

  public OperationBuilder plus(Operation operation) {
    this.operation = new BinaryOperation(this.operation, Operator.PLUS, operation);
    return this;
  }

  public OperationBuilder minus(double constant) {
    return minus(Constant.of(constant));
  }

  public OperationBuilder minus(Operation operation) {
    this.operation = new BinaryOperation(this.operation, Operator.MINUS, operation);
    return this;
  }

  public Operation build() {
    return operation;
  }
}
