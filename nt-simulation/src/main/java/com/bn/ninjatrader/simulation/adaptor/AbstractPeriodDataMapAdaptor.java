package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataMap;

/**
 * Created by Brad on 8/17/16.
 */
public abstract class AbstractPeriodDataMapAdaptor implements DataMapAdaptor<Value> {

  private final int period;

  public AbstractPeriodDataMapAdaptor(int period) {
    this.period = period;
  }

  @Override
  public DataMap toDataMap(final Value value) {
    DataMap dataMap = new DataMap();
    dataMap.put(Variable.of(getDataType()).withPeriod(period), value.getValue());
    return dataMap;
  }

  public abstract String getDataType();
}
