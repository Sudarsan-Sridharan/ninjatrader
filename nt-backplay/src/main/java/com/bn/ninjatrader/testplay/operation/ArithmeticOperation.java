package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.parameter.Parameters;
import com.bn.ninjatrader.testplay.type.Operator;

/**
 * Created by Brad on 8/2/16.
 */
public class ArithmeticOperation implements Operation {

  private static final long DECIMAL_PRECISION = 100000000;

  private Operation lhsOperand;
  private Operation rhsOperand;
  private Operator operator;

  public ArithmeticOperation(Operation lhsOperand, Operator operator, Operation rhsOperand) {
    this.lhsOperand = lhsOperand;
    this.rhsOperand = rhsOperand;
    this.operator = operator;
  }

  @Override
  public double getValue(Parameters parameters) {
    switch (operator) {
      case PLUS: return add(parameters);
      case MINUS: return lhsOperand.getValue(parameters) - rhsOperand.getValue(parameters);
    }
    return 0;
  }

  private double add(Parameters parameters) {
    long lhsValue = (long) (lhsOperand.getValue(parameters) * DECIMAL_PRECISION);
    long rhsValue = (long) (rhsOperand.getValue(parameters) * DECIMAL_PRECISION);

    return ((double) (lhsValue + rhsValue)) / DECIMAL_PRECISION;
  }
}
