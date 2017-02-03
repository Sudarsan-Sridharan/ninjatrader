package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operator.Operator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


/**
 * Created by Brad on 8/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArithmeticOperation<T extends Data> implements Operation<T> {
  private static final Logger LOG = LoggerFactory.getLogger(ArithmeticOperation.class);

  public static <T extends Data> ArithmeticOperation<T> startWith(final double constant) {
    return new ArithmeticOperation<>(constant);
  }

  public static <T extends Data> ArithmeticOperation<T> startWith(final Operation<T> operation) {
    return new ArithmeticOperation<>(operation);
  }

  @JsonProperty("operation")
  private Operation operation;

  public ArithmeticOperation(@JsonProperty("operation") final Operation lhs) {
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

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof ArithmeticOperation)) { return false; }
    if (obj == this) { return true; }
    final ArithmeticOperation rhs = (ArithmeticOperation) obj;
    return Objects.equal(operation, rhs.operation);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(operation);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).addValue(operation).toString();
  }

  @Override
  public String toString(final T t) {
    return operation.toString(t);
  }
}
