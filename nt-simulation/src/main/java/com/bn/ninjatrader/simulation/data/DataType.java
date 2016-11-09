package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.OperationType;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
public enum DataType implements Operation {

  BAR_INDEX,
  BAR_LAST_BUY_INDEX,
  BAR_LAST_SELL_INDEX,

  CONSTANT,

  PRICE_OPEN,
  PRICE_HIGH,
  PRICE_LOW,
  PRICE_CLOSE,
  VOLUME,

  TENKAN,
  KIJUN,
  SENKOU_A,
  SENKOU_B,
  CHIKOU,

  SMA,
  RSI
  ;

  public double getValue(BarData barData) {
    return barData.get(Variable.of(this));
  }

  public Set<Variable> getVariables() {
    return Sets.newHashSet(Variable.of(this));
  }

  public OperationType getOperationType() {
    return OperationType.DATATYPE;
  }
}
