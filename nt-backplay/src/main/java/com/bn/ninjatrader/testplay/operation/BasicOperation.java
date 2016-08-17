package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.parameter.BarParameters;
import com.bn.ninjatrader.testplay.type.Operator;

/**
 * Created by Brad on 8/2/16.
 */
public class BasicOperation implements Operation {

  private Operation lhsOperation;
  private Operation rhsOperation;
  private Operator operator;

  public BasicOperation(Operation lhsOperation, Operator operator, Operation rhsOperation) {
    this.lhsOperation = lhsOperation;
    this.rhsOperation = rhsOperation;
    this.operator = operator;
  }

  @Override
  public double getValue(BarParameters barParameters) {
    return operator.exec(lhsOperation, rhsOperation, barParameters);
  }
}
