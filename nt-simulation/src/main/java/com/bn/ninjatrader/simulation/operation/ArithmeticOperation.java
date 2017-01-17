package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.type.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.bn.ninjatrader.simulation.type.Operator.*;

/**
 * Created by Brad on 8/2/16.
 */
public class ArithmeticOperation implements Operation {
  private static final Logger LOG = LoggerFactory.getLogger(ArithmeticOperation.class);

  private Operation operation;

  public ArithmeticOperation(Operation lhs) {
    this.operation = lhs;
  }

  public ArithmeticOperation(double constant) {
    operation = Constant.of(constant);
  }

  @Override
  public double getValue(BarData barParameters) {
    return operation.getValue(barParameters);
  }

  @Override
  public Set<Variable> getVariables() {
    return operation.getVariables();
  }

  public ArithmeticOperation plus(Operation rhs) {
    return basicOperation(PLUS, rhs);
  }

  public ArithmeticOperation plus(double rhs) {
    return basicOperation(PLUS, Constant.of(rhs));
  }

  public ArithmeticOperation minus(Operation rhs) {
    return basicOperation(MINUS, rhs);
  }

  public ArithmeticOperation minus(double rhs) {
    return basicOperation(MINUS, Constant.of(rhs));
  }

  public ArithmeticOperation mult(Operation rhs) {
    return basicOperation(MULTIPLY, rhs);
  }

  public ArithmeticOperation mult(double rhs) {
    return basicOperation(MULTIPLY, Constant.of(rhs));
  }

  public ArithmeticOperation div(Operation rhs) {
    return basicOperation(DIVIDE, rhs);
  }

  public ArithmeticOperation div(double rhs) {
    return basicOperation(DIVIDE, Constant.of(rhs));
  }

  private ArithmeticOperation basicOperation(Operator operator, Operation rhs) {
    operation = new BinaryOperation(operation, operator, rhs);
    return this;
  }
}
