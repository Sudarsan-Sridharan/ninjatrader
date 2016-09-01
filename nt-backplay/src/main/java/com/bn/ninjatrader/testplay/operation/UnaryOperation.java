package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
public class UnaryOperation implements Operation {

  private final DataType dataType;
  private double constant;

  public static UnaryOperation of(double constantValue) {
    return new UnaryOperation(constantValue);
  }

  public static UnaryOperation of(DataType dataType) {
    return new UnaryOperation(dataType);
  }

  public UnaryOperation(DataType dataType) {
    this.dataType = dataType;
  }

  public UnaryOperation(double constantValue) {
    this.dataType = DataType.CONSTANT;
    this.constant = constantValue;
  }

  @Override
  public double getValue(BarData barParameters) {
    if (dataType == DataType.CONSTANT){
      return constant;
    }
    return barParameters.get(dataType);
  }

  @Override
  public Set<DataType> getDataTypes() {
    return Sets.newHashSet(dataType);
  }
}
