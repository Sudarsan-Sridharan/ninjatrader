package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.type.Operator;

/**
 * Created by Brad on 8/5/16.
 */
public class OperationBuilder {

  private Operation operation;

  public static OperationBuilder startWith(DataType dataType) {
    return new OperationBuilder(dataType);
  }

  public static OperationBuilder startWith(Operation operation) {
    return new OperationBuilder(operation);
  }

  public static OperationBuilder startWith(double constant) {
    return new OperationBuilder(constant);
  }

  private OperationBuilder(DataType dataType) {
    this.operation = UnaryOperation.of(dataType);
  }

  private OperationBuilder(Operation operation) {
    this.operation = operation;
  }

  private OperationBuilder(double constant) {
    this.operation = UnaryOperation.of(constant);
  }

  public OperationBuilder plus(DataType dataType) {
    return plus(UnaryOperation.of(dataType));
  }

  public OperationBuilder plus(double constant) {
    return plus(UnaryOperation.of(constant));
  }

  public OperationBuilder plus(Operation operation) {
    this.operation = new BasicOperation(this.operation, Operator.PLUS, operation);
    return this;
  }

  public OperationBuilder minus(DataType dataType) {
    return minus(UnaryOperation.of(dataType));
  }

  public OperationBuilder minus(double constant) {
    return minus(UnaryOperation.of(constant));
  }

  public OperationBuilder minus(Operation operation) {
    this.operation = new BasicOperation(this.operation, Operator.MINUS, operation);
    return this;
  }

  public Operation build() {
    return operation;
  }
}
