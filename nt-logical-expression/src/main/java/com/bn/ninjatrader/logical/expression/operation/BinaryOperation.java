package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
public class BinaryOperation<T extends Data> implements Operation<T> {
  private static final Logger LOG = LoggerFactory.getLogger(BinaryOperation.class);

  public static final BinaryOperation of(final Operation lhs, final Operator operator, final Operation rhs) {
    return new BinaryOperation(lhs, operator, rhs);
  }
  public static final BinaryOperation of(final double lhs, final Operator operator, final Operation rhs) {
    return new BinaryOperation(Constant.of(lhs), operator, rhs);
  }
  public static final BinaryOperation of(final Operation lhs, final Operator operator, final double rhs) {
    return new BinaryOperation(lhs, operator, Constant.of(rhs));
  }
  public static final BinaryOperation of(final double lhs, final Operator operator, final double rhs) {
    return new BinaryOperation(Constant.of(lhs), operator, Constant.of(rhs));
  }

  private final Operation lhs; // Left-hand side
  private final Operation rhs; // Right-hand side
  private final Operator operator;

  public BinaryOperation(final Operation lhs, final Operator operator, final Operation rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  @Override
  public double getValue(final T t) {
    return operator.exec(lhs, rhs, t);
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> dataTypes = lhs.getVariables();
    dataTypes.addAll(rhs.getVariables());
    return dataTypes;
  }
}
