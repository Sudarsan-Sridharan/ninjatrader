package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.type.Operator;

import java.util.Set;

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
  public double getValue(BarData barParameters) {
    return operator.exec(lhsOperation, rhsOperation, barParameters);
  }

  @Override
  public Set<DataType> getDataTypes() {
    Set<DataType> dataTypes = lhsOperation.getDataTypes();
    dataTypes.addAll(rhsOperation.getDataTypes());
    return dataTypes;
  }
}
