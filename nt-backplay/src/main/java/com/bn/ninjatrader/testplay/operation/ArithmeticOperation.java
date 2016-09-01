package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.type.Operator;

import java.util.Set;

import static com.bn.ninjatrader.testplay.type.Operator.*;

/**
 * Created by Brad on 8/2/16.
 */
public class ArithmeticOperation implements Operation {

  private Operation operation;

  public ArithmeticOperation(Operation lhsOperand) {
    operation = operation;
  }

  public ArithmeticOperation(DataType dataType) {
    operation = UnaryOperation.of(dataType);
  }

  public ArithmeticOperation(double constant) {
    operation = UnaryOperation.of(constant);
  }

  @Override
  public double getValue(BarData barParameters) {
    return operation.getValue(barParameters);
  }

  @Override
  public Set<DataType> getDataTypes() {
    return operation.getDataTypes();
  }

  public ArithmeticOperation plus(Operation rhs) {
    return basicOperation(PLUS, rhs);
  }

  public ArithmeticOperation plus(double rhs) {
    return basicOperation(PLUS, UnaryOperation.of(rhs));
  }

  public ArithmeticOperation minus(Operation rhs) {
    return basicOperation(MINUS, rhs);
  }

  public ArithmeticOperation minus(double rhs) {
    return basicOperation(MINUS, UnaryOperation.of(rhs));
  }

  public ArithmeticOperation mult(Operation rhs) {
    return basicOperation(MULTIPLY, rhs);
  }

  public ArithmeticOperation mult(double rhs) {
    return basicOperation(MULTIPLY, UnaryOperation.of(rhs));
  }

  public ArithmeticOperation div(Operation rhs) {
    return basicOperation(DIVIDE, rhs);
  }

  public ArithmeticOperation div(double rhs) {
    return basicOperation(DIVIDE, UnaryOperation.of(rhs));
  }

  private ArithmeticOperation basicOperation(Operator operator, Operation rhs) {
    operation = new BasicOperation(operation, operator, rhs);
    return this;
  }
}
