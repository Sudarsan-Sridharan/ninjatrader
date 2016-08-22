package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.testplay.type.InequalityOperator;

import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public class BasicCondition implements Condition {

  private final Operation lhsOperation;
  private final Operation rhsOperation;
  private final InequalityOperator operator;

  public BasicCondition(Operation lhsOperation, InequalityOperator operator, Operation rhsOperation) {
    this.lhsOperation = lhsOperation;
    this.rhsOperation = rhsOperation;
    this.operator = operator;
  }

  @Override
  public boolean isMatch(BarData barParameters) {
    double lhsValue = lhsOperation.getValue(barParameters);
    double rhsValue = rhsOperation.getValue(barParameters);
    return operator.isMatch(lhsValue, rhsValue);
  }

  @Override
  public Set<DataType> getDataTypes() {
    Set<DataType> dataTypes = lhsOperation.getDataTypes();
    dataTypes.addAll(rhsOperation.getDataTypes());
    return dataTypes;
  }
}
