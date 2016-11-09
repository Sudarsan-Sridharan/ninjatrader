package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.type.Operator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
public class BinaryOperation implements Operation {

  private static final Logger LOG = LoggerFactory.getLogger(BinaryOperation.class);

  private Operation lhsOperation;
  private Operation rhsOperation;
  private Operator operator;

  public BinaryOperation(Operation lhsOperation, Operator operator, Operation rhsOperation) {
    this.lhsOperation = lhsOperation;
    this.rhsOperation = rhsOperation;
    this.operator = operator;
  }

  @Override
  public double getValue(BarData barParameters) {
    LOG.info("{} - {}", lhsOperation, rhsOperation);
    return operator.exec(lhsOperation, rhsOperation, barParameters);
  }

  @Override
  public Set<Variable> getVariables() {
    Set<Variable> dataTypes = lhsOperation.getVariables();
    dataTypes.addAll(rhsOperation.getVariables());
    return dataTypes;
  }

  @Override
  @JsonProperty("type")
  public OperationType getOperationType() {
    return OperationType.BINARY;
  }
}
