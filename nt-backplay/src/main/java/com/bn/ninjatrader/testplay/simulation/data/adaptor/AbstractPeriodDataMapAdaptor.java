package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.DataType;

/**
 * Created by Brad on 8/17/16.
 */
public abstract class AbstractPeriodDataMapAdaptor implements DataMapAdaptor<Value> {

  private final int period;

  public AbstractPeriodDataMapAdaptor(int period) {
    this.period = period;
  }

  @Override
  public DataMap toDataMap(Value value) {
    DataMap dataMap = new DataMap();
    DataType dataType = DataType.valueOf(getDataTypePrefix() + period);

    dataMap.put(dataType, value.getValue());
    return dataMap;
  }

  public abstract String getDataTypePrefix();
}
