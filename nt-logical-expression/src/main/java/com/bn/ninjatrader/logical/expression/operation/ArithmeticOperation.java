package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


/**
 * Created by Brad on 8/2/16.
 */
public class ArithmeticOperation<T extends Data> implements Operation<T> {
  private static final Logger LOG = LoggerFactory.getLogger(ArithmeticOperation.class);

  private Operation operation;

  public ArithmeticOperation(final Operation lhs) {
    this.operation = lhs;
  }

  public ArithmeticOperation(final double constant) {
    operation = Constant.of(constant);
  }

  @Override
  public double getValue(final T t) {
    return operation.getValue(t);
  }

  @Override
  public Set<Variable> getVariables() {
    return operation.getVariables();
  }

  public ArithmeticOperation plus(final Operation rhs) {
    return basicOperation(Operator.PLUS, rhs);
  }

  public ArithmeticOperation plus(final double rhs) {
    return basicOperation(Operator.PLUS, Constant.of(rhs));
  }

  public ArithmeticOperation minus(final Operation rhs) {
    return basicOperation(Operator.MINUS, rhs);
  }

  public ArithmeticOperation minus(final double rhs) {
    return basicOperation(Operator.MINUS, Constant.of(rhs));
  }

  public ArithmeticOperation mult(final Operation rhs) {
    return basicOperation(Operator.MULTIPLY, rhs);
  }

  public ArithmeticOperation mult(final double rhs) {
    return basicOperation(Operator.MULTIPLY, Constant.of(rhs));
  }

  public ArithmeticOperation div(final Operation rhs) {
    return basicOperation(Operator.DIVIDE, rhs);
  }

  public ArithmeticOperation div(final double rhs) {
    return basicOperation(Operator.DIVIDE, Constant.of(rhs));
  }

  private ArithmeticOperation basicOperation(final Operator operator, final Operation rhs) {
    operation = new BinaryOperation(operation, operator, rhs);
    return this;
  }
}
